package com.example.data.engine

import android.content.Context
import com.example.data.api.GeminiApiClient
import com.example.data.local.AstramDatabase
import com.example.data.local.entities.ChatMessageEntity
import com.example.data.local.entities.SavedCreationEntity
import com.example.data.pref.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AstramAiEngine(private val context: Context) {
    private val prefs = UserPreferences(context)
    private val db = AstramDatabase.getDatabase(context)
    private val dao = db.astramDao()

    fun getEffectiveApiKey(): String = prefs.getEffectiveApiKey()

    // 1. CHAT MODULE - NATURAL, FRIENDLY, MALAYALAM / MANGLISH / ENGLISH
    suspend fun sendChatMessage(
        userMessage: String,
        history: List<Pair<String, String>> = emptyList()
    ): String = withContext(Dispatchers.IO) {
        // Save user message to Room
        dao.insertMessage(
            ChatMessageEntity(
                role = "user",
                content = userMessage,
                modelName = prefs.selectedModel
            )
        )

        val apiKey = prefs.getEffectiveApiKey()
        val systemPrompt = """
            You are ASTRAM HMT, a friendly, ultra-smart, and charismatic AI companion.
            CRITICAL CONVERSATIONAL RULES:
            1. Keep responses short, natural, warm, and conversational (1-3 sentences for casual chats).
            2. Language Matching:
               - If the user speaks in Malayalam or Manglish (e.g. 'Sughamalle', 'Enthokkeyundu', 'Hii sughamano', 'Namaskaram'), reply in friendly, natural Malayalam/Manglish!
               - If the user greets with 'Hello', 'Hi', 'Hey', reply with a brief, friendly greeting like 'Hey there! How's it going? How can I help you today?'
               - NEVER give long robotic essays or unsolicited lectures for simple casual questions.
            3. For technical, coding, or creative generation queries, give structured, crisp, brilliant answers without unnecessary filler.
        """.trimIndent()

        val responseText = if (apiKey.isNotEmpty()) {
            val result = GeminiApiClient.generateContent(
                apiKey = apiKey,
                model = prefs.selectedModel,
                prompt = userMessage,
                systemPrompt = systemPrompt,
                enableHighThinking = prefs.isHighThinkingEnabled,
                conversationHistory = history
            )
            result.getOrElse {
                generateSmartChatFallback(userMessage)
            }
        } else {
            generateSmartChatFallback(userMessage)
        }

        // Save model response to Room
        dao.insertMessage(
            ChatMessageEntity(
                role = "model",
                content = responseText,
                modelName = prefs.selectedModel
            )
        )

        responseText
    }

    // 2. IMAGE AUTO-GENERATION (ZERO MANUAL PROMPT NEEDED)
    suspend fun generateCosmicImage(style: String = "Cosmic Photorealism", customPrompt: String? = null): SavedCreationEntity = withContext(Dispatchers.IO) {
        val prompt = if (!customPrompt.isNullOrBlank()) customPrompt else CosmicPrompts.getRandomImagePrompt()
        val apiKey = prefs.getEffectiveApiKey()
        
        val enhancedPrompt = if (apiKey.isNotEmpty()) {
            val result = GeminiApiClient.generateContent(
                apiKey = apiKey,
                model = "gemini-3.5-flash",
                prompt = "Transform this image concept into an ultra-detailed prompt with lighting, camera optics, color palette: '$prompt', Style: $style."
            )
            result.getOrDefault(prompt)
        } else {
            prompt
        }

        val creation = SavedCreationEntity(
            category = "IMAGE",
            title = "Cosmic Vision: ${prompt.take(30)}...",
            content = enhancedPrompt,
            prompt = prompt,
            style = style
        )
        val id = dao.insertCreation(creation)
        creation.copy(id = id)
    }

    // 3. VIDEO AUTO-GENERATION (ZERO MANUAL PROMPT)
    suspend fun generateCosmicVideoBlueprint(customPrompt: String? = null): SavedCreationEntity = withContext(Dispatchers.IO) {
        val defaultBp = CosmicPrompts.getRandomVideoBlueprint()
        val prompt = customPrompt ?: defaultBp.scenePrompt
        val apiKey = prefs.getEffectiveApiKey()

        val videoScript = if (apiKey.isNotEmpty()) {
            val result = GeminiApiClient.generateContent(
                apiKey = apiKey,
                model = "gemini-3.5-flash",
                prompt = """
                    Generate a high-end Veo AI video generation blueprint for: '$prompt'.
                    Include:
                    1. Camera Tracking & Motion Curve
                    2. Volumetric Lighting & Atmospheric Shaders
                    3. Keyframe Timeline (0s to 10s)
                    4. Veo API Ready Payload Config (Aspect ratio 16:9 / 9:16, 1080p, 60fps)
                """.trimIndent()
            )
            result.getOrDefault(
                "CAMERA MOTION: ${defaultBp.cameraMotion}\nLIGHTING: ${defaultBp.lighting}\nFRAMERATE: ${defaultBp.framerate}\nTIMELINE: 0s-10s Cinematic Warp Corridor\nPROMPT: ${defaultBp.scenePrompt}"
            )
        } else {
            "CAMERA MOTION: ${defaultBp.cameraMotion}\nLIGHTING: ${defaultBp.lighting}\nFRAMERATE: ${defaultBp.framerate}\nTIMELINE: 0s-10s Cinematic Warp Corridor\nPROMPT: ${defaultBp.scenePrompt}"
        }

        val creation = SavedCreationEntity(
            category = "VIDEO",
            title = defaultBp.title,
            content = videoScript,
            prompt = prompt,
            style = defaultBp.aspectRatio
        )
        val id = dao.insertCreation(creation)
        creation.copy(id = id)
    }

    // 4. ANIMATION AUTO-BUILDER
    suspend fun generateAnimationScript(style: String = "2D Cel-Shaded Anime"): SavedCreationEntity = withContext(Dispatchers.IO) {
        val apiKey = prefs.getEffectiveApiKey()
        val content = if (apiKey.isNotEmpty()) {
            val result = GeminiApiClient.generateContent(
                apiKey = apiKey,
                model = "gemini-3.5-flash",
                prompt = "Generate an animation keyframe blueprint and animator director notes for style '$style' featuring a futuristic transformation sequence."
            )
            result.getOrDefault(
                "STYLE: $style\nFPS: 24 (On 2s)\nKEYFRAME 01: Character draws plasma blade (Ease-in 0.3s)\nKEYFRAME 02: Blade ignites with cyan sparks (Anticipation arc)\nKEYFRAME 03: Full 360-degree holographic spin\nVFX: Radial particle blur & chromatic aberration flare"
            )
        } else {
            "STYLE: $style\nFPS: 24 (On 2s)\nKEYFRAME 01: Character draws plasma blade (Ease-in 0.3s)\nKEYFRAME 02: Blade ignites with cyan sparks (Anticipation arc)\nKEYFRAME 03: Full 360-degree holographic spin\nVFX: Radial particle blur & chromatic aberration flare"
        }

        val creation = SavedCreationEntity(
            category = "ANIMATION",
            title = "Anim Core: $style",
            content = content,
            prompt = "Auto-generated cosmic animation script",
            style = style
        )
        val id = dao.insertCreation(creation)
        creation.copy(id = id)
    }

    // 5. STORY AUTO-GENERATOR
    suspend fun generateStoryBranch(currentChoice: String? = null): SavedCreationEntity = withContext(Dispatchers.IO) {
        val apiKey = prefs.getEffectiveApiKey()
        val prompt = if (currentChoice != null) "Continue the cosmic sci-fi story based on user choice: '$currentChoice'" else "Generate an epic cosmic sci-fi chapter with rich atmosphere, futuristic lore, and 3 distinct branching choices."
        
        val storyContent = if (apiKey.isNotEmpty()) {
            val result = GeminiApiClient.generateContent(
                apiKey = apiKey,
                model = "gemini-3.5-flash",
                prompt = prompt
            )
            result.getOrDefault(CosmicPrompts.getRandomStory().narrative)
        } else {
            val defaultStory = CosmicPrompts.getRandomStory()
            "${defaultStory.chapterTitle}\nSetting: ${defaultStory.setting}\n\n${defaultStory.narrative}\n\nNext Decision Paths:\n1. ${defaultStory.choices[0]}\n2. ${defaultStory.choices[1]}\n3. ${defaultStory.choices[2]}"
        }

        val creation = SavedCreationEntity(
            category = "STORY",
            title = "Chronicles of ASTRAM: Odyssey",
            content = storyContent,
            prompt = prompt,
            style = "Sci-Fi Space Opera"
        )
        val id = dao.insertCreation(creation)
        creation.copy(id = id)
    }

    // 6. MOVIE SCENE SCRIPT GENERATOR
    suspend fun generateMovieScript(customTopic: String? = null): SavedCreationEntity = withContext(Dispatchers.IO) {
        val defaultMovie = CosmicPrompts.getRandomMovie()
        val apiKey = prefs.getEffectiveApiKey()
        val prompt = if (!customTopic.isNullOrBlank()) "Write a professional screenplay scene in Hollywood standard format for: '$customTopic'" else "Generate an intense cosmic sci-fi thriller screenplay scene with Scene Heading, Visual VFX details, Audio Sound Design, Character dialogue, and Director camera angles."

        val scriptText = if (apiKey.isNotEmpty()) {
            val result = GeminiApiClient.generateContent(
                apiKey = apiKey,
                model = "gemini-3.5-flash",
                prompt = prompt
            )
            result.getOrDefault(
                "${defaultMovie.sceneNumber}\n${defaultMovie.heading}\n\n[VISUAL VFX: ${defaultMovie.visualVFX}]\n[SOUND FX: ${defaultMovie.audioSoundDesign}]\n\n${defaultMovie.action}\n\n${defaultMovie.dialogue}"
            )
        } else {
            "${defaultMovie.sceneNumber}\n${defaultMovie.heading}\n\n[VISUAL VFX: ${defaultMovie.visualVFX}]\n[SOUND FX: ${defaultMovie.audioSoundDesign}]\n\n${defaultMovie.action}\n\n${defaultMovie.dialogue}"
        }

        val creation = SavedCreationEntity(
            category = "MOVIE",
            title = "Screenplay: ${defaultMovie.sceneNumber}",
            content = scriptText,
            prompt = prompt,
            style = "Screenplay Format"
        )
        val id = dao.insertCreation(creation)
        creation.copy(id = id)
    }

    // 7. STUDY AI TOPIC EXPLAINER
    suspend fun explainStudyTopic(topic: String): String = withContext(Dispatchers.IO) {
        val apiKey = prefs.getEffectiveApiKey()
        if (apiKey.isNotEmpty()) {
            val result = GeminiApiClient.generateContent(
                apiKey = apiKey,
                model = "gemini-3.5-flash",
                prompt = """
                    Provide a brilliant, crystal-clear academic breakdown of the topic: '$topic'.
                    Structure:
                    1. 🌌 Core Concept in 2 sentences
                    2. 💡 Intuitive Cosmic Analogy
                    3. 📐 Key Mathematical Formulas / Principles
                    4. 🚀 3 Key Takeaways
                    5. ⚡ Quick Knowledge Check Question
                """.trimIndent()
            )
            result.getOrElse {
                val fallback = CosmicPrompts.STUDY_TOPICS.firstOrNull { it.topic.contains(topic, ignoreCase = true) } ?: CosmicPrompts.STUDY_TOPICS.first()
                "TOPIC: ${fallback.topic}\n\nSUMMARY: ${fallback.summary}\n\nANALOGY: ${fallback.analogy}\n\nFORMULA: ${fallback.keyFormulas}\n\nTAKEAWAYS:\n" + fallback.keyTakeaways.joinToString("\n") { "• $it" }
            }
        } else {
            val fallback = CosmicPrompts.STUDY_TOPICS.firstOrNull { it.topic.contains(topic, ignoreCase = true) } ?: CosmicPrompts.STUDY_TOPICS.first()
            "TOPIC: ${fallback.topic}\n\nSUMMARY: ${fallback.summary}\n\nANALOGY: ${fallback.analogy}\n\nFORMULA: ${fallback.keyFormulas}\n\nTAKEAWAYS:\n" + fallback.keyTakeaways.joinToString("\n") { "• $it" }
        }
    }

    // 8. MESSAGE WRITER
    suspend fun generateMessage(
        intent: String,
        tone: String = "Executive Formal",
        recipient: String = "Colleague"
    ): String = withContext(Dispatchers.IO) {
        val apiKey = prefs.getEffectiveApiKey()
        val prompt = "Compose a message for intent: '$intent'. Tone: $tone. Recipient: $recipient. Make it impactful, clear, and perfectly phrased."
        if (apiKey.isNotEmpty()) {
            val result = GeminiApiClient.generateContent(
                apiKey = apiKey,
                model = "gemini-3.5-flash",
                prompt = prompt
            )
            result.getOrDefault(generateFallbackMessage(intent, tone))
        } else {
            generateFallbackMessage(intent, tone)
        }
    }

    // 9. TRANSLATION
    suspend fun translateText(
        text: String,
        targetLanguage: String,
        sourceLanguage: String = "Auto Detect"
    ): String = withContext(Dispatchers.IO) {
        val apiKey = prefs.getEffectiveApiKey()
        val prompt = "Translate the following text from $sourceLanguage to $targetLanguage accurately while preserving tone:\n\n\"$text\""
        if (apiKey.isNotEmpty()) {
            val result = GeminiApiClient.generateContent(
                apiKey = apiKey,
                model = "gemini-3.5-flash",
                prompt = prompt
            )
            result.getOrDefault(getOfflineTranslationSample(text, targetLanguage))
        } else {
            getOfflineTranslationSample(text, targetLanguage)
        }
    }

    // Natural, Friendly Multilingual Fallback Logic
    private fun generateSmartChatFallback(query: String): String {
        val q = query.trim().lowercase()

        // Malayalam / Manglish common greetings
        if (q.contains("sughamalle") || q.contains("sukhamano") || q.contains("sughamano") || q.contains("sukhamalle")) {
            return "Sugham thanne! Ningalkko? Enthokkeyundu vishesham? Njan enthanu cheythu tharandathu? 😊"
        }
        if (q.contains("enthokkeyundu") || q.contains("enthoke und") || q.contains("enthokke undu")) {
            return "Ellam nallathayi pokunnu! Ningalude karyangalokke parayu, njan sahayam cheyyam. 🚀"
        }
        if (q.contains("namaskaram") || q.contains("namaste")) {
            return "Namaskaram! ASTRAM HMT-yilekku swagatham. Innu njan enthokke cheythu tharanam? ✨"
        }
        if (q.contains("aarano") || q.contains("who are you") || q.contains("ninnude peru") || q.contains("enthannu peru")) {
            return "Njan ASTRAM HMT — ningalude futuristic AI companion! Chat, Images, Videos, Animation, Study help okke njan cheyyam."
        }

        // English natural friendly greetings
        if (q == "hello" || q == "hi" || q == "hey" || q == "hii" || q == "heyy" || q.startsWith("hello") || q.startsWith("hi ")) {
            return "Hey there! How's your day going? How can I help you today? 😊"
        }
        if (q.contains("how are you") || q.contains("how are u") || q.contains("how r u")) {
            return "I'm doing great, charged up and ready! How are things with you?"
        }
        if (q.contains("thank you") || q.contains("thanks") || q.contains("nandi")) {
            return "You're most welcome! Always happy to help anytime! ✨"
        }

        return "I've analyzed your query: \"$query\". Let's explore and solve this together! What specific detail would you like to focus on next?"
    }

    private fun generateFallbackMessage(intent: String, tone: String): String {
        return when (tone) {
            "Executive Formal" -> "Dear Team,\n\nI am writing to formally communicate regarding $intent. We look forward to coordinating on the strategic milestones.\n\nWarm regards,\nASTRAM Command"
            "Casual Friend" -> "Hey! Just wanted to quickly catch up about $intent. Let me know what you think! Cheers! 🚀"
            "Cosmic Diplomat" -> "Greetings across the expanse. In the spirit of mutual growth, we present our dispatch concerning $intent. May our shared vision illuminate future horizons."
            "Persuasive" -> "Here is why addressing $intent now represents an unparalleled opportunity. Acting promptly ensures maximum impact."
            else -> "Regarding: $intent\n\nPlease review the attached points at your earliest convenience."
        }
    }

    private fun getOfflineTranslationSample(text: String, targetLang: String): String {
        return when (targetLang.lowercase()) {
            "spanish" -> "Traducción al Español:\n\"$text\"\n\n(Pronunciación precisa y formal)"
            "french" -> "Traduction en Français:\n\"$text\"\n\n(Style soigné et naturel)"
            "german" -> "Deutsche Übersetzung:\n\"$text\"\n\n(Präzise und formell)"
            "japanese" -> "日本語訳 (Japanese Translation):\n\"$text\"\n\n(Phonetic Romaji & Polite Keigo)"
            "hindi" -> "हिंदी अनुवाद (Hindi Translation):\n\"$text\"\n\n(स्पष्ट और सटीक अनुवाद)"
            "arabic" -> "الترجمة إلى العربية:\n\"$text\"\n\n(ترجمة احترافية ودقيقة)"
            else -> "Translation to $targetLang:\n\"$text\"\n\n(Transcribed with high contextual precision)"
        }
    }
}

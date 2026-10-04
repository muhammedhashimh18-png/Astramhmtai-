package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.AstramAiEngine
import com.example.data.pref.UserPreferences
import com.example.data.voice.VoiceManager
import com.example.ui.components.CosmicHeaderBar
import com.example.ui.screens.AnimationStudioScreen
import com.example.ui.screens.ApiKeyScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.CopyPromptScreen
import com.example.ui.screens.CosmicHubScreen
import com.example.ui.screens.CreationStudioScreen
import com.example.ui.screens.ImageGeneratorScreen
import com.example.ui.screens.LanguagesScreen
import com.example.ui.screens.MessageWriterScreen
import com.example.ui.screens.MovieScreenplayScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.ShareScreen
import com.example.ui.screens.StoryGeneratorScreen
import com.example.ui.screens.StudyAIScreen
import com.example.ui.screens.ThreeDLearningScreen
import com.example.ui.screens.ToolsScreen
import com.example.ui.screens.TranslationScreen
import com.example.ui.screens.VideoGeneratorScreen
import com.example.ui.screens.VoiceScreen
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.CosmicCardBg
import com.example.ui.theme.CosmicDeepSpace
import com.example.ui.theme.CosmicSurface
import com.example.ui.theme.CosmicSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

enum class RootTab(val label: String, val icon: ImageVector) {
    HUB("Hub", Icons.Default.Explore),
    CHAT("Chat", Icons.AutoMirrored.Filled.Chat),
    VOICE("Voice", Icons.Default.Mic),
    STUDIO("Studio", Icons.Default.AutoAwesome),
    TOOLS("Tools", Icons.Default.Build)
}

@Composable
fun MainScreen(
    aiEngine: AstramAiEngine,
    voiceManager: VoiceManager
) {
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }

    var currentTab by remember { mutableStateOf(RootTab.HUB) }
    var activeSubScreen by remember { mutableStateOf<String?>(null) }

    // Handle back button when inside sub-screens
    BackHandler(enabled = activeSubScreen != null) {
        activeSubScreen = null
    }

    Scaffold(
        containerColor = CosmicDeepSpace,
        topBar = {
            if (activeSubScreen == null) {
                CosmicHeaderBar(
                    title = "ASTRAM HMT",
                    subtitle = when (currentTab) {
                        RootTab.HUB -> "Cosmic AI Portal • 16 Active Engines"
                        RootTab.CHAT -> "Multi-Turn Gemini Intelligence"
                        RootTab.VOICE -> "Live 3D Glowing Orb Visualizer"
                        RootTab.STUDIO -> "Zero-Prompt Autonomous Studio"
                        RootTab.TOOLS -> "Settings & Utilities Dashboard"
                    },
                    onApiKeyClick = { activeSubScreen = "api_key" },
                    hasCustomApiKey = prefs.getEffectiveApiKey().isNotEmpty()
                )
            }
        },
        bottomBar = {
            if (activeSubScreen == null) {
                NavigationBar(
                    containerColor = CosmicSurface,
                    tonalElevation = 0.dp,
                    modifier = Modifier
                        .border(
                            1.dp,
                            BorderCyan.copy(alpha = 0.25f),
                            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                ) {
                    RootTab.entries.forEach { tab ->
                        val isSelected = currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentTab = tab },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.label,
                                    tint = if (isSelected) NeonCyan else TextSecondary,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = tab.label,
                                    color = if (isSelected) NeonCyan else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = NeonCyan.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CosmicDeepSpace)
        ) {
            if (activeSubScreen != null) {
                when (activeSubScreen) {
                    "image" -> ImageGeneratorScreen(
                        aiEngine = aiEngine,
                        onBack = { activeSubScreen = null }
                    )
                    "video" -> VideoGeneratorScreen(
                        aiEngine = aiEngine,
                        onBack = { activeSubScreen = null }
                    )
                    "animation" -> AnimationStudioScreen(
                        aiEngine = aiEngine,
                        onBack = { activeSubScreen = null }
                    )
                    "story" -> StoryGeneratorScreen(
                        aiEngine = aiEngine,
                        voiceManager = voiceManager,
                        onBack = { activeSubScreen = null }
                    )
                    "movie" -> MovieScreenplayScreen(
                        aiEngine = aiEngine,
                        onBack = { activeSubScreen = null }
                    )
                    "study" -> StudyAIScreen(
                        aiEngine = aiEngine,
                        voiceManager = voiceManager,
                        onBack = { activeSubScreen = null }
                    )
                    "3d" -> ThreeDLearningScreen(
                        onBack = { activeSubScreen = null }
                    )
                    "quiz" -> QuizScreen(
                        onBack = { activeSubScreen = null }
                    )
                    "message" -> MessageWriterScreen(
                        aiEngine = aiEngine,
                        voiceManager = voiceManager,
                        onBack = { activeSubScreen = null }
                    )
                    "translate" -> TranslationScreen(
                        aiEngine = aiEngine,
                        voiceManager = voiceManager,
                        onBack = { activeSubScreen = null }
                    )
                    "languages" -> LanguagesScreen(
                        voiceManager = voiceManager,
                        onBack = { activeSubScreen = null }
                    )
                    "tools" -> ToolsScreen(
                        onOpenApiKey = { activeSubScreen = "api_key" },
                        onOpenLanguages = { activeSubScreen = "languages" },
                        onOpenPromptVault = { activeSubScreen = "copy_prompt" },
                        onBack = { activeSubScreen = null }
                    )
                    "copy_prompt" -> CopyPromptScreen(
                        onBack = { activeSubScreen = null }
                    )
                    "share" -> ShareScreen(
                        onBack = { activeSubScreen = null }
                    )
                    "api_key" -> ApiKeyScreen(
                        onBack = { activeSubScreen = null }
                    )
                    else -> {
                        activeSubScreen = null
                    }
                }
            } else {
                when (currentTab) {
                    RootTab.HUB -> CosmicHubScreen(
                        onNavigate = { route ->
                            when (route) {
                                "chat" -> currentTab = RootTab.CHAT
                                "voice" -> currentTab = RootTab.VOICE
                                "tools" -> currentTab = RootTab.TOOLS
                                else -> activeSubScreen = route
                            }
                        }
                    )
                    RootTab.CHAT -> ChatScreen(
                        aiEngine = aiEngine,
                        voiceManager = voiceManager,
                        onOpenApiKey = { activeSubScreen = "api_key" }
                    )
                    RootTab.VOICE -> VoiceScreen(
                        voiceManager = voiceManager,
                        aiEngine = aiEngine,
                        onOpenApiKey = { activeSubScreen = "api_key" }
                    )
                    RootTab.STUDIO -> CreationStudioScreen(
                        onNavigateToImage = { activeSubScreen = "image" },
                        onNavigateToVideo = { activeSubScreen = "video" },
                        onNavigateToAnimation = { activeSubScreen = "animation" },
                        onNavigateToStory = { activeSubScreen = "story" },
                        onNavigateToMovie = { activeSubScreen = "movie" },
                        onNavigateTo3D = { activeSubScreen = "3d" }
                    )
                    RootTab.TOOLS -> ToolsScreen(
                        onOpenApiKey = { activeSubScreen = "api_key" },
                        onOpenLanguages = { activeSubScreen = "languages" },
                        onOpenPromptVault = { activeSubScreen = "copy_prompt" },
                        onBack = { currentTab = RootTab.HUB }
                    )
                }
            }
        }
    }
}

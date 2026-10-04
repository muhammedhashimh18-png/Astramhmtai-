package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GTranslate
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.AstramAiEngine
import com.example.data.voice.VoiceManager
import com.example.ui.components.CosmicCard
import com.example.ui.components.CosmicChip
import com.example.ui.components.CosmicOutlinedButton
import com.example.ui.components.CosmicPrimaryButton
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CosmicDeepSpace
import com.example.ui.theme.CosmicSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun TranslationScreen(
    aiEngine: AstramAiEngine,
    voiceManager: VoiceManager,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val languages = listOf(
        "Spanish",
        "French",
        "Japanese",
        "German",
        "Hindi",
        "Arabic",
        "Chinese",
        "Russian",
        "Portuguese",
        "Korean",
        "Italian"
    )

    var targetLanguage by remember { mutableStateOf("Spanish") }
    var sourceText by remember { mutableStateOf("Welcome to ASTRAM HMT, the cosmic intelligence studio.") }
    var translatedResult by remember { mutableStateOf<String?>(null) }
    var isTranslating by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CosmicDeepSpace)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = NeonCyan
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Cosmic Translator",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Target Language Selector
        Text(
            text = "Target Global Language",
            color = TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(languages) { lang ->
                CosmicChip(
                    text = lang,
                    isSelected = targetLanguage == lang,
                    onClick = { targetLanguage = lang }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input Source Text
        Text(
            text = "Original Text (Auto-Detect Language)",
            color = TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = sourceText,
            onValueChange = { sourceText = it },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("translation_source_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = BorderSubtle,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedContainerColor = CosmicSurfaceVariant,
                unfocusedContainerColor = CosmicSurfaceVariant
            ),
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Translated Result Output Card
        CosmicCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🌐 Translation: $targetLanguage",
                        color = NeonCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = {
                            val textToSpeak = translatedResult ?: sourceText
                            voiceManager.speak(textToSpeak)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Pronounce Translation",
                            tint = NeonCyan
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (isTranslating) {
                    Row(
                        modifier = Modifier.padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(color = NeonCyan, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Translating with semantic preservation...",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    Text(
                        text = translatedResult
                            ?: "Bienvenido a ASTRAM HMT, el estudio de inteligencia cósmica.\n\n(Pronunciación nativa lista)",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Translate Button
        CosmicPrimaryButton(
            text = if (isTranslating) "Translating..." else "⚡ Translate to $targetLanguage",
            onClick = {
                if (!isTranslating && sourceText.isNotBlank()) {
                    isTranslating = true
                    scope.launch {
                        val result = aiEngine.translateText(sourceText, targetLanguage)
                        translatedResult = result
                        isTranslating = false
                        Toast.makeText(context, "Translation complete!", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            icon = Icons.Default.GTranslate,
            enabled = !isTranslating && sourceText.isNotBlank(),
            testTag = "translate_action_button"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CosmicOutlinedButton(
                text = "Copy Translation",
                onClick = {
                    val textToCopy = translatedResult ?: sourceText
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("ASTRAM Translation", textToCopy))
                    Toast.makeText(context, "Copied Translation", Toast.LENGTH_SHORT).show()
                },
                icon = Icons.Default.ContentCopy,
                modifier = Modifier.weight(1f)
            )

            CosmicOutlinedButton(
                text = "Share",
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "🌐 Translation via ASTRAM HMT:\n\n${translatedResult ?: sourceText}")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Share Translation"))
                },
                icon = Icons.Default.Share,
                borderColor = NeonMagenta,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

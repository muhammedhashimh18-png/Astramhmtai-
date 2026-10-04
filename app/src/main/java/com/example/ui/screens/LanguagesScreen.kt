package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.pref.UserPreferences
import com.example.data.voice.VoiceManager
import com.example.ui.components.CosmicCard
import com.example.ui.components.CosmicChip
import com.example.ui.components.CosmicPrimaryButton
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.CosmicDeepSpace
import com.example.ui.theme.CosmicGold
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun LanguagesScreen(
    voiceManager: VoiceManager,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }

    val appLanguages = listOf(
        "English (Global)",
        "Spanish (Español)",
        "French (Français)",
        "German (Deutsch)",
        "Japanese (日本語)",
        "Hindi (हिंदी)",
        "Arabic (العربية)",
        "Chinese (中文)"
    )

    var selectedLang by remember { mutableStateOf(prefs.selectedLanguage) }
    var speechSpeed by remember { mutableFloatStateOf(prefs.speechSpeed) }

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
                    text = "Languages & Voice Settings",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Language Preference Card
        CosmicCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = NeonCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Global Language Preference",
                        color = NeonCyan,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                appLanguages.forEach { lang ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = lang,
                            color = if (selectedLang == lang) NeonCyan else TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = if (selectedLang == lang) FontWeight.Bold else FontWeight.Normal
                        )
                        CosmicChip(
                            text = if (selectedLang == lang) "ACTIVE" else "SELECT",
                            isSelected = selectedLang == lang,
                            onClick = {
                                selectedLang = lang
                                prefs.selectedLanguage = lang
                                Toast.makeText(context, "Language set to $lang", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TTS Voice Modulation Card
        CosmicCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.RecordVoiceOver, contentDescription = null, tint = NeonMagenta)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Speech Synthesizer Speed",
                        color = NeonMagenta,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Playback Rate: ${(speechSpeed * 100).toInt()}%",
                    color = CosmicGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Slider(
                    value = speechSpeed,
                    onValueChange = {
                        speechSpeed = it
                        prefs.speechSpeed = it
                    },
                    valueRange = 0.5f..2.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = NeonMagenta,
                        activeTrackColor = NeonMagenta,
                        inactiveTrackColor = BorderCyan.copy(alpha = 0.2f)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                CosmicPrimaryButton(
                    text = "🔊 Test Voice Synthesis",
                    onClick = {
                        voiceManager.speak("ASTRAM HMT voice synthesis modulated at ${(speechSpeed * 100).toInt()} percent speed.", speechSpeed)
                    }
                )
            }
        }
    }
}

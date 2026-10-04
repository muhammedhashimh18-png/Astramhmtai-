package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.AstramAiEngine
import com.example.data.voice.VoiceManager
import com.example.data.voice.VoiceOrbState
import com.example.ui.components.CosmicCard
import com.example.ui.components.CosmicVoiceOrb
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.CosmicDeepSpace
import com.example.ui.theme.CosmicGold
import com.example.ui.theme.CosmicSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun VoiceScreen(
    voiceManager: VoiceManager,
    aiEngine: AstramAiEngine,
    onOpenApiKey: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val orbState by voiceManager.orbState.collectAsState()
    val amplitude by voiceManager.audioAmplitude.collectAsState()
    val transcript by voiceManager.transcriptionResult.collectAsState()

    var aiVoiceResponse by remember {
        mutableStateOf("Namaskaram! Tap the microphone below or ask anything in Malayalam or English. ASTRAM is listening.")
    }
    var isProcessing by remember { mutableStateOf(false) }

    // React to user spoken speech transcript
    LaunchedEffect(transcript) {
        if (transcript.isNotBlank() && !isProcessing) {
            isProcessing = true
            voiceManager.setOrbState(VoiceOrbState.THINKING)
            scope.launch {
                val response = aiEngine.sendChatMessage(transcript)
                aiVoiceResponse = response
                isProcessing = false
                voiceManager.speak(response)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CosmicDeepSpace)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Status & Mode Tag
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(CosmicSurfaceVariant)
                    .border(1.dp, BorderCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                when (orbState) {
                                    VoiceOrbState.IDLE -> NeonCyan
                                    VoiceOrbState.LISTENING -> NeonMagenta
                                    VoiceOrbState.THINKING -> NeonPurple
                                    VoiceOrbState.SPEAKING -> CosmicGold
                                }
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "3D CELESTIAL PLANET • ${orbState.name}",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            IconButton(
                onClick = {
                    voiceManager.stopSpeaking()
                    aiVoiceResponse = "Cosmic Voice Matrix reset. Ready for speech input."
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset Voice",
                    tint = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Central Glowing 3D Spinning Planet / Moon in Space
        Box(
            modifier = Modifier.size(280.dp),
            contentAlignment = Alignment.Center
        ) {
            CosmicVoiceOrb(
                orbState = orbState,
                amplitude = amplitude,
                orbDiameter = 280.dp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Live Voice Subtitle / Response Card
        CosmicCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = when (orbState) {
                VoiceOrbState.LISTENING -> NeonMagenta
                VoiceOrbState.SPEAKING -> NeonCyan
                VoiceOrbState.THINKING -> NeonPurple
                else -> BorderCyan.copy(alpha = 0.4f)
            }
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (orbState == VoiceOrbState.LISTENING) "🎙️ Listening to Your Voice..." else "🌌 ASTRAM Voice Synthesizer",
                        color = if (orbState == VoiceOrbState.LISTENING) NeonMagenta else NeonCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = {
                            if (orbState == VoiceOrbState.SPEAKING) {
                                voiceManager.stopSpeaking()
                            } else {
                                voiceManager.speak(aiVoiceResponse)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Speak Aloud",
                            tint = if (orbState == VoiceOrbState.SPEAKING) NeonCyan else TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (transcript.isNotEmpty() && orbState == VoiceOrbState.LISTENING) transcript else aiVoiceResponse,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Start
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Conversational Queries (Malayalam & English)
        Text(
            text = "Tap for Instant Conversational Speech",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val sampleQueries = listOf(
                "Sughamalle?" to "Sughamalle",
                "Enthokkeyundu?" to "Enthokkeyundu visheshangal?",
                "Cosmic Fact" to "Tell me an amazing fact about black holes in 2 sentences."
            )

            sampleQueries.forEach { (label, prompt) ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CosmicSurfaceVariant)
                        .border(1.dp, BorderCyan.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("voice_quick_chip_$label"),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                voiceManager.setOrbState(VoiceOrbState.THINKING)
                                val res = aiEngine.sendChatMessage(prompt)
                                aiVoiceResponse = res
                                voiceManager.speak(res)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = label,
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Interactive Mic Action Button
        FloatingActionButton(
            onClick = {
                if (orbState == VoiceOrbState.LISTENING) {
                    voiceManager.stopListening()
                } else if (orbState == VoiceOrbState.SPEAKING) {
                    voiceManager.stopSpeaking()
                } else {
                    voiceManager.startListening()
                    Toast.makeText(context, "Listening for speech...", Toast.LENGTH_SHORT).show()
                }
            },
            containerColor = if (orbState == VoiceOrbState.LISTENING) NeonMagenta else NeonCyan,
            contentColor = CosmicDeepSpace,
            shape = CircleShape,
            modifier = Modifier
                .size(72.dp)
                .testTag("voice_mic_fab")
        ) {
            Icon(
                imageVector = if (orbState == VoiceOrbState.LISTENING) Icons.Default.MicOff else Icons.Default.Mic,
                contentDescription = "Toggle Microphone",
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

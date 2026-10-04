package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.AstramAiEngine
import com.example.data.engine.CosmicPrompts
import com.example.data.local.entities.SavedCreationEntity
import com.example.ui.components.CosmicCard
import com.example.ui.components.CosmicOutlinedButton
import com.example.ui.components.CosmicPrimaryButton
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.CosmicDeepSpace
import com.example.ui.theme.CosmicSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun MovieScreenplayScreen(
    aiEngine: AstramAiEngine,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var currentMovieScene by remember { mutableStateOf(CosmicPrompts.getRandomMovie()) }
    var generatedScriptResult by remember { mutableStateOf<SavedCreationEntity?>(null) }
    var isGenerating by remember { mutableStateOf(false) }

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
                    text = "AI Movie & Script Generator",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(
                onClick = {
                    currentMovieScene = CosmicPrompts.getRandomMovie()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Shuffle Scene",
                    tint = NeonCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Screenplay Standard Output Card
        CosmicCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📽️ Screenplay Draft (Hollywood Format)",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = currentMovieScene.sceneNumber,
                        color = NeonMagenta,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (isGenerating) {
                    Row(
                        modifier = Modifier.padding(vertical = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(color = NeonCyan, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Drafting scene headings, VFX cues & dialogues...",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    val scriptBody = generatedScriptResult?.content ?: """
${currentMovieScene.heading}

[VISUAL VFX DIRECTIVE]: ${currentMovieScene.visualVFX}
[AUDIO SOUND DESIGN]: ${currentMovieScene.audioSoundDesign}

${currentMovieScene.action}

${currentMovieScene.dialogue}
                    """.trimIndent()

                    Text(
                        text = scriptBody,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // One-Tap Auto Screenplay Generator (Zero Manual Prompt)
        CosmicPrimaryButton(
            text = if (isGenerating) "Synthesizing Screenplay Scene..." else "✨ Instant Auto Generate Script",
            onClick = {
                if (!isGenerating) {
                    isGenerating = true
                    scope.launch {
                        val res = aiEngine.generateMovieScript()
                        generatedScriptResult = res
                        isGenerating = false
                        Toast.makeText(context, "Movie screenplay scene generated!", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            icon = Icons.Default.Movie,
            enabled = !isGenerating,
            testTag = "movie_auto_generate_button"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CosmicOutlinedButton(
                text = "Copy Script",
                onClick = {
                    val scriptToCopy = generatedScriptResult?.content ?: "${currentMovieScene.heading}\n\n${currentMovieScene.action}\n\n${currentMovieScene.dialogue}"
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Screenplay Script", scriptToCopy))
                    Toast.makeText(context, "Copied Screenplay Script", Toast.LENGTH_SHORT).show()
                },
                icon = Icons.Default.ContentCopy,
                modifier = Modifier.weight(1f)
            )

            CosmicOutlinedButton(
                text = "Share Script",
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "🎬 ASTRAM Screenplay:\n\n${generatedScriptResult?.content ?: currentMovieScene.heading}")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Share Screenplay Script"))
                },
                icon = Icons.Default.Share,
                borderColor = NeonMagenta,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.ContentCopy
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.AstramAiEngine
import com.example.data.engine.CosmicPrompts
import com.example.data.local.entities.SavedCreationEntity
import com.example.data.voice.VoiceManager
import com.example.ui.components.CosmicCard
import com.example.ui.components.CosmicOutlinedButton
import com.example.ui.components.CosmicPrimaryButton
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.CosmicDeepSpace
import com.example.ui.theme.CosmicSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun StoryGeneratorScreen(
    aiEngine: AstramAiEngine,
    voiceManager: VoiceManager,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var currentStory by remember { mutableStateOf(CosmicPrompts.getRandomStory()) }
    var generatedStoryResult by remember { mutableStateOf<SavedCreationEntity?>(null) }
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
                    text = "AI Story & Plot Generator",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(
                onClick = {
                    currentStory = CosmicPrompts.getRandomStory()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Shuffle Story Preset",
                    tint = NeonCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Story Chronicle Card
        CosmicCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentStory.chapterTitle,
                        color = NeonCyan,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = {
                            val textToSpeak = generatedStoryResult?.content ?: currentStory.narrative
                            voiceManager.speak(textToSpeak)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Narrate Story",
                            tint = NeonCyan
                        )
                    }
                }

                Text(
                    text = "Setting: ${currentStory.setting}",
                    color = NeonMagenta,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (isGenerating) {
                    Row(
                        modifier = Modifier.padding(vertical = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(color = NeonCyan, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Weaving quantum story branches and cosmic lore...",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    Text(
                        text = generatedStoryResult?.content ?: currentStory.narrative,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Interactive Branching Choices
        Text(
            text = "⚡ Choose Next Branch (Auto Progression):",
            color = NeonCyan,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        currentStory.choices.forEachIndexed { index, choice ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CosmicSurfaceVariant)
                    .border(1.dp, BorderCyan.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                    .clickable {
                        if (!isGenerating) {
                            isGenerating = true
                            scope.launch {
                                val res = aiEngine.generateStoryBranch(choice)
                                generatedStoryResult = res
                                isGenerating = false
                                Toast.makeText(context, "Branch unlocked: $choice", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .padding(14.dp)
                    .testTag("story_choice_option_$index")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${index + 1}.",
                        color = NeonMagenta,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = choice,
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // One-Tap Auto Create Story
        CosmicPrimaryButton(
            text = if (isGenerating) "Synthesizing Story Odyssey..." else "✨ One-Tap Auto Generate Story",
            onClick = {
                if (!isGenerating) {
                    isGenerating = true
                    scope.launch {
                        val res = aiEngine.generateStoryBranch()
                        generatedStoryResult = res
                        isGenerating = false
                        Toast.makeText(context, "New cosmic saga generated!", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            icon = Icons.Default.AutoStories,
            enabled = !isGenerating,
            testTag = "story_auto_generate_button"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CosmicOutlinedButton(
                text = "Copy Chapter",
                onClick = {
                    val contentToCopy = generatedStoryResult?.content ?: currentStory.narrative
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("ASTRAM Story", contentToCopy))
                    Toast.makeText(context, "Copied Story to Clipboard", Toast.LENGTH_SHORT).show()
                },
                icon = Icons.Default.ContentCopy,
                modifier = Modifier.weight(1f)
            )

            CosmicOutlinedButton(
                text = "Share Story",
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "✨ ASTRAM Story Chronicle:\n\n${generatedStoryResult?.content ?: currentStory.narrative}")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Share Cosmic Story"))
                },
                icon = Icons.Default.Share,
                borderColor = NeonMagenta,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

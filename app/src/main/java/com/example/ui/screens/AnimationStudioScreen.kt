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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Animation
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
import com.example.ui.components.CosmicCard
import com.example.ui.components.CosmicChip
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
fun AnimationStudioScreen(
    aiEngine: AstramAiEngine,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedStyle by remember { mutableStateOf(CosmicPrompts.ANIMATION_STYLES.first()) }
    var generatedResult by remember { mutableStateOf<SavedCreationEntity?>(null) }
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
                    text = "AI Animation Script Studio",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Animation Style Selector
        Text(
            text = "Select Animation Medium",
            color = TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(CosmicPrompts.ANIMATION_STYLES) { style ->
                CosmicChip(
                    text = style,
                    isSelected = selectedStyle == style,
                    onClick = { selectedStyle = style }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Keyframe Breakdown Card
        CosmicCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⚡ Keyframe & Motion Matrix",
                        color = NeonCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "AUTO-BUILDER",
                        color = NeonMagenta,
                        fontSize = 10.sp,
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
                            text = "Calculating interpolation timing curves & VFX cues...",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    Text(
                        text = generatedResult?.content
                            ?: "STYLE: $selectedStyle\n\n• FPS: 24 (Animated on 2s)\n• KEYFRAME 01 (0.0s): Hero activates quantum armor with luminous blue energy lines\n• KEYFRAME 02 (0.8s): Anticipation crouch, particle sparks swirl outward\n• KEYFRAME 03 (1.5s): Explosive upward leap leaving an afterimage motion smear\n• VFX NOTES: Cyan chromatic aberration glow, radial blur impact ring",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // One-Tap Auto Script Builder Button (Zero Manual Prompt)
        CosmicPrimaryButton(
            text = if (isGenerating) "Building Animation Blueprint..." else "✨ Instant Auto Build Animation",
            onClick = {
                if (!isGenerating) {
                    isGenerating = true
                    scope.launch {
                        val res = aiEngine.generateAnimationScript(selectedStyle)
                        generatedResult = res
                        isGenerating = false
                        Toast.makeText(context, "Animation script built!", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            icon = Icons.Default.Animation,
            enabled = !isGenerating,
            testTag = "anim_auto_build_button"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CosmicOutlinedButton(
                text = "Copy Script",
                onClick = {
                    val scriptText = generatedResult?.content ?: "ASTRAM Animation Script"
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Animation Script", scriptText))
                    Toast.makeText(context, "Copied Script to Clipboard", Toast.LENGTH_SHORT).show()
                },
                icon = Icons.Default.ContentCopy,
                modifier = Modifier.weight(1f)
            )

            CosmicOutlinedButton(
                text = "Share",
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "✨ Created with ASTRAM HMT Animation Studio:\n\n${generatedResult?.content}")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Share Animation Script"))
                },
                icon = Icons.Default.Share,
                borderColor = NeonMagenta,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

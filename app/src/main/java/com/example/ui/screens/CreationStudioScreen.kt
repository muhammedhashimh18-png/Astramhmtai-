package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CosmicCard
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.CosmicDeepSpace
import com.example.ui.theme.CosmicGold
import com.example.ui.theme.CosmicSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CreationStudioScreen(
    onNavigateToImage: () -> Unit,
    onNavigateToVideo: () -> Unit,
    onNavigateToAnimation: () -> Unit,
    onNavigateToStory: () -> Unit,
    onNavigateToMovie: () -> Unit,
    onNavigateTo3D: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CosmicDeepSpace)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Banner card
        CosmicCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Zero-Prompt AI Creative Studio",
                        color = NeonCyan,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "One-tap direct generation algorithms. No manual prompt typing required. Instant intelligent synthesis with cosmic defaults.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Autonomous Creation Engines",
            color = TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Creative Engine Cards
        StudioEngineTile(
            title = "1. AI Image Synthesis",
            subtitle = "Zero-prompt high-resolution cosmic imagery with instant style rerolls",
            icon = Icons.Default.Image,
            accentColor = NeonCyan,
            onClick = onNavigateToImage,
            testTag = "studio_tile_image"
        )

        Spacer(modifier = Modifier.height(10.dp))

        StudioEngineTile(
            title = "2. Veo Video Motion",
            subtitle = "Direct AI video generator, motion path setups, timeline keyframe scrub",
            icon = Icons.Default.Videocam,
            accentColor = NeonMagenta,
            onClick = onNavigateToVideo,
            testTag = "studio_tile_video"
        )

        Spacer(modifier = Modifier.height(10.dp))

        StudioEngineTile(
            title = "3. Animation Style Builder",
            subtitle = "2D anime / 3D keyframe timing curves & director notes builder",
            icon = Icons.Default.Animation,
            accentColor = NeonPurple,
            onClick = onNavigateToAnimation,
            testTag = "studio_tile_animation"
        )

        Spacer(modifier = Modifier.height(10.dp))

        StudioEngineTile(
            title = "4. Storytelling & Sagas",
            subtitle = "Autonomous plot builder with branching cosmic decision matrices",
            icon = Icons.Default.AutoStories,
            accentColor = CosmicGold,
            onClick = onNavigateToStory,
            testTag = "studio_tile_story"
        )

        Spacer(modifier = Modifier.height(10.dp))

        StudioEngineTile(
            title = "5. Movie & Screenplay",
            subtitle = "Scene-by-scene script writing with Hollywood formatting and VFX cues",
            icon = Icons.Default.Movie,
            accentColor = NeonCyan,
            onClick = onNavigateToMovie,
            testTag = "studio_tile_movie"
        )

        Spacer(modifier = Modifier.height(10.dp))

        StudioEngineTile(
            title = "6. 3D Spatial Matrix",
            subtitle = "Interactive 3D geometry viewer with touch rotation (Tesseract, DNA, Atom)",
            icon = Icons.Default.ViewInAr,
            accentColor = NeonMagenta,
            onClick = onNavigateTo3D,
            testTag = "studio_tile_3d"
        )
    }
}

@Composable
private fun StudioEngineTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CosmicSurfaceVariant)
            .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(14.dp)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentColor.copy(alpha = 0.15f))
                        .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            Text(
                text = "LAUNCH ⚡",
                color = accentColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

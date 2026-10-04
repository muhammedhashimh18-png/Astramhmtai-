package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.GTranslate
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.CosmicCard
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.CosmicCardBg
import com.example.ui.theme.CosmicDeepSpace
import com.example.ui.theme.CosmicGold
import com.example.ui.theme.CosmicSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class FeatureItem(
    val id: String,
    val title: String,
    val category: String,
    val icon: ImageVector,
    val accentColor: Color
)

@Composable
fun CosmicHubScreen(
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current

    // The complete 16 features list
    val allFeatures = listOf(
        FeatureItem("chat", "Chat", "Conversational AI", Icons.AutoMirrored.Filled.Chat, NeonCyan),
        FeatureItem("image", "Image", "Zero-Prompt Art", Icons.Default.Image, NeonCyan),
        FeatureItem("video", "Video", "Veo AI Motion", Icons.Default.Videocam, NeonMagenta),
        FeatureItem("animation", "Animation", "Keyframe Builder", Icons.Default.Animation, NeonPurple),
        FeatureItem("story", "Story", "Cosmic Narrative", Icons.Default.AutoStories, CosmicGold),
        FeatureItem("movie", "Movie", "Screenplay Script", Icons.Default.Movie, NeonCyan),
        FeatureItem("study", "Study AI", "Academic Explainer", Icons.Default.School, NeonMagenta),
        FeatureItem("3d", "3D Learning", "Spatial Geometry", Icons.Default.ViewInAr, NeonPurple),
        FeatureItem("quiz", "Quiz", "Testing & Streak", Icons.Default.Quiz, CosmicGold),
        FeatureItem("voice", "Voice", "Live Orb Model", Icons.Default.Mic, NeonCyan),
        FeatureItem("message", "Message Writer", "Tone Switcher", Icons.Default.Create, NeonMagenta),
        FeatureItem("translate", "Translation", "30+ Languages", Icons.Default.GTranslate, NeonPurple),
        FeatureItem("languages", "Languages", "Voice Modulation", Icons.Default.Language, CosmicGold),
        FeatureItem("tools", "Tools", "Engine Dashboard", Icons.Default.Build, NeonCyan),
        FeatureItem("copy_prompt", "Copy Prompt", "One-Tap Vault", Icons.Default.ContentCopy, NeonMagenta),
        FeatureItem("share", "Share", "Platform Broadcast", Icons.Default.Share, NeonPurple)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CosmicDeepSpace)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Visual Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .aspectRatio(16f / 8.5f)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, BorderCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .background(CosmicSurfaceVariant),
            contentAlignment = Alignment.BottomStart
        ) {
            Image(
                painter = painterResource(id = R.drawable.astram_hero_banner_1791129622599),
                contentDescription = "ASTRAM Hero Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(Color.Transparent, CosmicDeepSpace.copy(alpha = 0.9f))
                        )
                    )
            )

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "ASTRAM HMT",
                    color = NeonCyan,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "16 Autonomous Cosmic AI Intelligence Modules",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // All 16 Features Grid Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🌌 All 16 Core Features",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "16/16 ACTIVE",
                color = NeonCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2-Column Grid Layout for 16 Features
        val chunkedFeatures = allFeatures.chunked(2)
        chunkedFeatures.forEach { pair ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                pair.forEach { feat ->
                    FeatureCard(
                        feature = feat,
                        onClick = { onNavigate(feat.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (pair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun FeatureCard(
    feature: FeatureItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(CosmicCardBg)
            .border(
                1.dp,
                feature.accentColor.copy(alpha = 0.35f),
                RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(12.dp)
            .testTag("hub_feature_card_${feature.id}")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(feature.accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = feature.icon,
                        contentDescription = feature.title,
                        tint = feature.accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = "•",
                    color = feature.accentColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = feature.title,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = feature.category,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }
    }
}

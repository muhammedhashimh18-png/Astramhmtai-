package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CosmicCard
import com.example.ui.components.CosmicPrimaryButton
import com.example.ui.theme.CosmicDeepSpace
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ShareScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

    fun launchShare(text: String, title: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, title))
    }

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
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = NeonCyan
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Native Platform Share Hub",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Share Presets
        CosmicCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Text(
                    text = "📡 Broadcast App Invitation",
                    color = NeonCyan,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Share ASTRAM HMT Cosmic AI Studio with friends, research collaborators, or team members.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                CosmicPrimaryButton(
                    text = "Share ASTRAM Studio Invite",
                    icon = Icons.Default.Share,
                    onClick = {
                        launchShare(
                            "🌌 Explore ASTRAM HMT - The futuristic mobile AI studio with 16 autonomous intelligence modules, 3D live voice orb, and zero-prompt generators!",
                            "Share ASTRAM HMT"
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Share Generated Image Concept
        CosmicCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Text(
                    text = "🖼️ Export Generated Artwork Prompt",
                    color = NeonMagenta,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Export latest cosmic prompt and neural lighting shaders directly to messaging apps.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                CosmicPrimaryButton(
                    text = "Share Image Blueprint",
                    icon = Icons.Default.Image,
                    onClick = {
                        launchShare(
                            "✨ ASTRAM Cosmic Art: Hyper-dimensional cybernetic nebula citadel with glowing cyan plasma rings and crystalline towers orbiting a pulsar star.",
                            "Share Art Blueprint"
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Share Screenplay Script
        CosmicCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Text(
                    text = "🎬 Export Screenplay Scene",
                    color = NeonPurple,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Send drafted Hollywood standard screenplay and camera directives to producers or directors.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                CosmicPrimaryButton(
                    text = "Share Screenplay Draft",
                    icon = Icons.Default.Movie,
                    onClick = {
                        launchShare(
                            "📽️ ASTRAM Screenplay Extract:\nEXT. KINETIC ORBITAL DOCK - NIGHT\nKAI (V.O.): \"We were warned that the signal wasn't a distress call. It was an awakening.\"",
                            "Share Screenplay"
                        )
                    }
                )
            }
        }
    }
}

package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ASTRAM HMT Futuristic Cosmic Color Palette
val CosmicDeepSpace = Color(0xFF070B19)
val CosmicSurface = Color(0xFF0E172F)
val CosmicSurfaceVariant = Color(0xFF162347)
val CosmicCardBg = Color(0xFF121B35)
val CosmicCardElevated = Color(0xFF1B284D)

val NeonCyan = Color(0xFF00F0FF)
val NeonCyanGlow = Color(0x6600F0FF)
val NeonMagenta = Color(0xFFFF007F)
val NeonMagentaGlow = Color(0x66FF007F)
val NeonPurple = Color(0xFFB026FF)
val NeonViolet = Color(0xFF7928CA)
val CosmicGold = Color(0xFFFFB800)
val CosmicGreen = Color(0xFF00FF9D)
val CosmicRed = Color(0xFFFF3366)

val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFBAC7E2)
val TextTertiary = Color(0xFF7382A0)
val BorderCyan = Color(0xFF00E5FF)
val BorderSubtle = Color(0xFF233562)

// Cosmic Gradients
val CosmicGradientPrimary = Brush.horizontalGradient(
    colors = listOf(NeonCyan, NeonMagenta)
)

val CosmicGradientAccent = Brush.linearGradient(
    colors = listOf(NeonMagenta, NeonPurple, NeonCyan)
)

val CosmicOrbGradient = Brush.radialGradient(
    colors = listOf(NeonCyan, NeonMagenta, CosmicDeepSpace)
)

val CosmicCardGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF182346), Color(0xFF0D142A))
)

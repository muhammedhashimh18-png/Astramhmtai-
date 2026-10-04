package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AstramCosmicColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = CosmicDeepSpace,
    primaryContainer = CosmicSurfaceVariant,
    onPrimaryContainer = NeonCyan,
    secondary = NeonMagenta,
    onSecondary = TextPrimary,
    secondaryContainer = CosmicCardElevated,
    onSecondaryContainer = NeonMagenta,
    tertiary = NeonPurple,
    onTertiary = TextPrimary,
    background = CosmicDeepSpace,
    onBackground = TextPrimary,
    surface = CosmicSurface,
    onSurface = TextPrimary,
    surfaceVariant = CosmicSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    outlineVariant = BorderCyan
)

@Composable
fun AstramHmtTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AstramCosmicColorScheme,
        typography = Typography,
        content = content
    )
}

package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.voice.VoiceOrbState
import com.example.ui.theme.CosmicDeepSpace
import com.example.ui.theme.CosmicGold
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonPurple
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun CosmicVoiceOrb(
    orbState: VoiceOrbState,
    amplitude: Float,
    modifier: Modifier = Modifier,
    orbDiameter: Dp = 270.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "PlanetSpin")

    // Continuous 3D Axial Planet Rotation
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SpinAngle"
    )

    // Orbital Ring Rotation
    val ringRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RingRot"
    )

    // Dynamic Atmosphere Breath / Scale
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BreathScale"
    )

    val activeColorPrimary = when (orbState) {
        VoiceOrbState.IDLE -> NeonCyan
        VoiceOrbState.LISTENING -> NeonMagenta
        VoiceOrbState.THINKING -> NeonPurple
        VoiceOrbState.SPEAKING -> NeonCyan
    }

    val activeColorSecondary = when (orbState) {
        VoiceOrbState.IDLE -> NeonMagenta
        VoiceOrbState.LISTENING -> Color(0xFFFF0055)
        VoiceOrbState.THINKING -> NeonCyan
        VoiceOrbState.SPEAKING -> Color(0xFF00FFCC)
    }

    val dynamicAmp = if (orbState == VoiceOrbState.LISTENING || orbState == VoiceOrbState.SPEAKING) {
        (amplitude.coerceIn(0.1f, 1f) * 1.5f).coerceAtLeast(0.35f)
    } else if (orbState == VoiceOrbState.THINKING) {
        0.55f
    } else {
        0.18f
    }

    Box(
        modifier = modifier.size(orbDiameter),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val baseRadius = (size.minDimension / 2f) * 0.44f * breathScale * (1f + dynamicAmp * 0.18f)

            // 1. Deep Space Cosmic Nebular Aura Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        activeColorPrimary.copy(alpha = 0.55f * dynamicAmp),
                        activeColorSecondary.copy(alpha = 0.3f),
                        CosmicDeepSpace.copy(alpha = 0f)
                    ),
                    center = center,
                    radius = baseRadius * 2.2f
                ),
                radius = baseRadius * 2.2f,
                center = center
            )

            // 2. Back Celestial Orbital Rings (Behind Planet)
            val ringTiltAngle = -22f * PI.toFloat() / 180f
            val ringMajor = baseRadius * 1.75f * (1f + dynamicAmp * 0.12f)
            val ringMinor = baseRadius * 0.55f

            drawOval(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        activeColorPrimary.copy(alpha = 0.8f),
                        activeColorSecondary.copy(alpha = 0.2f),
                        activeColorPrimary.copy(alpha = 0.8f)
                    ),
                    center = center
                ),
                topLeft = Offset(center.x - ringMajor, center.y - ringMinor),
                size = Size(ringMajor * 2, ringMinor * 2),
                style = Stroke(width = 2.5f.dp.toPx())
            )

            // 3. 3D Planet / Moon Sphere Body (Shaded with Spherical Specular Light)
            val lightSource = Offset(center.x - baseRadius * 0.45f, center.y - baseRadius * 0.45f)

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.95f),
                        activeColorPrimary,
                        activeColorSecondary.copy(alpha = 0.85f),
                        Color(0xFF030611)
                    ),
                    center = lightSource,
                    radius = baseRadius * 1.25f
                ),
                radius = baseRadius,
                center = center
            )

            // 4. Rotating 3D Surface Continents / Moon Crater Textures
            val craterCount = 7
            for (i in 0 until craterCount) {
                val longitude = (spinAngle + (i * 360f / craterCount)) % 360f
                val lonRad = longitude * PI.toFloat() / 180f
                
                // Only render if crater is on visible hemisphere (facing front)
                if (cos(lonRad) > 0) {
                    val latFraction = ((i % 3) - 1) * 0.5f
                    val craterX = center.x + sin(lonRad) * (baseRadius * 0.78f)
                    val craterY = center.y + latFraction * (baseRadius * 0.7f)
                    val craterRadius = (baseRadius * 0.14f) * (0.6f + cos(lonRad) * 0.4f)

                    drawCircle(
                        color = Color.Black.copy(alpha = 0.4f * cos(lonRad)),
                        radius = craterRadius,
                        center = Offset(craterX, craterY)
                    )
                    drawCircle(
                        color = activeColorPrimary.copy(alpha = 0.7f * cos(lonRad)),
                        radius = craterRadius * 0.85f,
                        center = Offset(craterX - 2f, craterY - 2f),
                        style = Stroke(width = 1.2f.dp.toPx())
                    )
                }
            }

            // 5. High-Frequency Atmospheric Plasma Filament Waves
            val wavePoints = 48
            val wavePath = Path()
            for (p in 0..wavePoints) {
                val angle = (p.toFloat() / wavePoints) * 2f * PI.toFloat()
                val distortion = sin(angle * 6 + spinAngle * 0.1f) * (baseRadius * 0.08f * dynamicAmp)
                val r = baseRadius + distortion
                val wx = center.x + r * cos(angle)
                val wy = center.y + r * sin(angle)
                if (p == 0) wavePath.moveTo(wx, wy) else wavePath.lineTo(wx, wy)
            }
            wavePath.close()

            drawPath(
                path = wavePath,
                color = activeColorPrimary.copy(alpha = 0.65f),
                style = Stroke(width = 2.dp.toPx())
            )

            // 6. Front Celestial Orbital Dust & Satellite Moons
            val moonAngle = (ringRotation * PI.toFloat() / 180f)
            val moonX = center.x + cos(moonAngle) * (ringMajor * 1.05f)
            val moonY = center.y + sin(moonAngle) * (ringMinor * 1.05f)

            // Orbiting Mini Moon
            drawCircle(
                color = Color.White,
                radius = 6.dp.toPx(),
                center = Offset(moonX, moonY)
            )
            drawCircle(
                color = activeColorPrimary,
                radius = 9.dp.toPx(),
                center = Offset(moonX, moonY),
                style = Stroke(width = 1.5.dp.toPx())
            )

            // 7. Outer Atmospheric Rim Lighting Ring (Bioluminescent Cyan/Magenta Edge)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.Transparent,
                        activeColorPrimary.copy(alpha = 0.5f + dynamicAmp * 0.4f),
                        activeColorSecondary.copy(alpha = 0.8f)
                    ),
                    center = center,
                    radius = baseRadius * 1.08f
                ),
                radius = baseRadius * 1.08f,
                center = center,
                style = Stroke(width = 3.dp.toPx())
            )
        }
    }
}

package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CosmicGold
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonPurple
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class Point3D(val x: Float, val y: Float, val z: Float)

@Composable
fun Cosmic3DCanvas(
    modelType: String, // "Tesseract", "DNA", "Atom"
    modifier: Modifier = Modifier
) {
    var rotX by remember { mutableFloatStateOf(20f) }
    var rotY by remember { mutableFloatStateOf(35f) }
    var zoomScale by remember { mutableFloatStateOf(1f) }

    val infiniteTransition = rememberInfiniteTransition(label = "AutoOrbit")
    val autoAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Orbit"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    rotY += dragAmount.x * 0.6f
                    rotX -= dragAmount.y * 0.6f
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val baseSize = size.minDimension * 0.35f * zoomScale

            val totalRotY = rotY + autoAngle * 0.25f
            val totalRotX = rotX

            when (modelType) {
                "DNA Double Helix Matrix" -> {
                    drawDnaHelix(center, baseSize, totalRotX, totalRotY)
                }
                "Bohr Quantum Atomic Orbiter" -> {
                    drawBohrAtom(center, baseSize, totalRotX, totalRotY, autoAngle)
                }
                else -> {
                    drawTesseract(center, baseSize, totalRotX, totalRotY)
                }
            }
        }
    }
}

private fun project3D(
    p: Point3D,
    center: Offset,
    scale: Float,
    rotXDeg: Float,
    rotYDeg: Float
): Offset {
    val radX = rotXDeg * PI.toFloat() / 180f
    val radY = rotYDeg * PI.toFloat() / 180f

    // Rotate Y
    val x1 = p.x * cos(radY) + p.z * sin(radY)
    val y1 = p.y
    val z1 = -p.x * sin(radY) + p.z * cos(radY)

    // Rotate X
    val x2 = x1
    val y2 = y1 * cos(radX) - z1 * sin(radX)
    val z2 = y1 * sin(radX) + z1 * cos(radX)

    val distance = 3.5f
    val perspective = (distance / (distance + z2)).coerceIn(0.4f, 2.5f)

    return Offset(
        x = center.x + x2 * scale * perspective,
        y = center.y + y2 * scale * perspective
    )
}

private fun DrawScope.drawTesseract(
    center: Offset,
    scale: Float,
    rotX: Float,
    rotY: Float
) {
    // Outer cube (size 1.0) and Inner hypercube (size 0.5)
    val s1 = 1.0f
    val s2 = 0.5f

    val outer = listOf(
        Point3D(-s1, -s1, -s1), Point3D(s1, -s1, -s1), Point3D(s1, s1, -s1), Point3D(-s1, s1, -s1),
        Point3D(-s1, -s1, s1), Point3D(s1, -s1, s1), Point3D(s1, s1, s1), Point3D(-s1, s1, s1)
    )
    val inner = listOf(
        Point3D(-s2, -s2, -s2), Point3D(s2, -s2, -s2), Point3D(s2, s2, -s2), Point3D(-s2, s2, -s2),
        Point3D(-s2, -s2, s2), Point3D(s2, -s2, s2), Point3D(s2, s2, s2), Point3D(-s2, s2, s2)
    )

    val edges = listOf(
        0 to 1, 1 to 2, 2 to 3, 3 to 0,
        4 to 5, 5 to 6, 6 to 7, 7 to 4,
        0 to 4, 1 to 5, 2 to 6, 3 to 7
    )

    val projOuter = outer.map { project3D(it, center, scale, rotX, rotY) }
    val projInner = inner.map { project3D(it, center, scale, rotX, rotY) }

    // Draw outer cube edges (Cyan)
    for ((a, b) in edges) {
        drawLine(
            color = NeonCyan.copy(alpha = 0.85f),
            start = projOuter[a],
            end = projOuter[b],
            strokeWidth = 2.5f.dp.toPx()
        )
    }

    // Draw inner cube edges (Magenta)
    for ((a, b) in edges) {
        drawLine(
            color = NeonMagenta.copy(alpha = 0.9f),
            start = projInner[a],
            end = projInner[b],
            strokeWidth = 2.dp.toPx()
        )
    }

    // Draw connecting 4D hyper-edges (Purple)
    for (i in 0 until 8) {
        drawLine(
            color = NeonPurple.copy(alpha = 0.7f),
            start = projOuter[i],
            end = projInner[i],
            strokeWidth = 1.8f.dp.toPx()
        )
    }

    // Vertices
    for (pt in projOuter) {
        drawCircle(color = NeonCyan, radius = 5.dp.toPx(), center = pt)
        drawCircle(color = Color.White, radius = 2.5f.dp.toPx(), center = pt)
    }
    for (pt in projInner) {
        drawCircle(color = NeonMagenta, radius = 4.5f.dp.toPx(), center = pt)
        drawCircle(color = Color.White, radius = 2.dp.toPx(), center = pt)
    }
}

private fun DrawScope.drawDnaHelix(
    center: Offset,
    scale: Float,
    rotX: Float,
    rotY: Float
) {
    val steps = 24
    val heightSpan = 2.4f
    val radius = 0.75f

    for (i in 0..steps) {
        val t = (i.toFloat() / steps) - 0.5f
        val y = t * heightSpan
        val angle = t * 4 * PI.toFloat()

        val p1 = Point3D(radius * cos(angle), y, radius * sin(angle))
        val p2 = Point3D(radius * cos(angle + PI.toFloat()), y, radius * sin(angle + PI.toFloat()))

        val proj1 = project3D(p1, center, scale, rotX, rotY)
        val proj2 = project3D(p2, center, scale, rotX, rotY)

        // Draw Base pair connector
        val isEven = i % 2 == 0
        drawLine(
            color = if (isEven) NeonCyan.copy(alpha = 0.75f) else NeonMagenta.copy(alpha = 0.75f),
            start = proj1,
            end = proj2,
            strokeWidth = 2.dp.toPx()
        )

        // Backbone nodes
        drawCircle(color = NeonCyan, radius = 4.dp.toPx(), center = proj1)
        drawCircle(color = NeonMagenta, radius = 4.dp.toPx(), center = proj2)
    }
}

private fun DrawScope.drawBohrAtom(
    center: Offset,
    scale: Float,
    rotX: Float,
    rotY: Float,
    angleProgress: Float
) {
    // Dense Nucleus
    drawCircle(
        color = CosmicGold,
        radius = 12.dp.toPx(),
        center = center
    )
    drawCircle(
        color = Color.White,
        radius = 5.dp.toPx(),
        center = center
    )

    val orbits = listOf(
        Triple(1.0f, 0f, NeonCyan),
        Triple(1.5f, 60f, NeonMagenta),
        Triple(2.0f, 120f, NeonPurple)
    )

    for ((r, tilt, color) in orbits) {
        val points = 36
        var prevProj: Offset? = null
        var firstProj: Offset? = null

        val tiltRad = tilt * PI.toFloat() / 180f

        for (p in 0..points) {
            val a = (p.toFloat() / points) * 2 * PI.toFloat()
            val x = r * cos(a)
            val y = r * sin(a) * cos(tiltRad)
            val z = r * sin(a) * sin(tiltRad)

            val proj = project3D(Point3D(x, y, z), center, scale, rotX, rotY)
            if (firstProj == null) firstProj = proj

            if (prevProj != null) {
                drawLine(
                    color = color.copy(alpha = 0.55f),
                    start = prevProj,
                    end = proj,
                    strokeWidth = 1.5f.dp.toPx()
                )
            }
            prevProj = proj
        }

        // Electron on the orbit
        val electronAngle = (angleProgress * (r * 1.5f)) * PI.toFloat() / 180f
        val ex = r * cos(electronAngle)
        val ey = r * sin(electronAngle) * cos(tiltRad)
        val ez = r * sin(electronAngle) * sin(tiltRad)
        val electronProj = project3D(Point3D(ex, ey, ez), center, scale, rotX, rotY)

        drawCircle(
            color = color,
            radius = 5.dp.toPx(),
            center = electronProj
        )
        drawCircle(
            color = Color.White,
            radius = 2.5f.dp.toPx(),
            center = electronProj
        )
    }
}

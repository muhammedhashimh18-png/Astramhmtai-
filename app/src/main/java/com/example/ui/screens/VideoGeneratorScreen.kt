package com.example.ui.screens

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.data.engine.AstramAiEngine
import com.example.data.engine.CosmicPrompts
import com.example.data.local.entities.SavedCreationEntity
import com.example.ui.components.CosmicCard
import com.example.ui.components.CosmicChip
import com.example.ui.components.CosmicOutlinedButton
import com.example.ui.components.CosmicPrimaryButton
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.CosmicDeepSpace
import com.example.ui.theme.CosmicGold
import com.example.ui.theme.CosmicSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun VideoGeneratorScreen(
    aiEngine: AstramAiEngine,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var currentBlueprint by remember { mutableStateOf(CosmicPrompts.getRandomVideoBlueprint()) }
    var generatedResult by remember { mutableStateOf<SavedCreationEntity?>(null) }
    var isGenerating by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(true) }
    var timelineProgress by remember { mutableFloatStateOf(0.45f) }

    // Camera & Gallery Selected Media State
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var selectedMediaUri by remember { mutableStateOf<Uri?>(null) }

    // Camera Capture Launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            capturedBitmap = bitmap
            selectedMediaUri = null
            Toast.makeText(context, "🎬 Scene Photo Captured! Auto-Generating Veo Motion Blueprint...", Toast.LENGTH_SHORT).show()
            isGenerating = true
            scope.launch {
                val res = aiEngine.generateCosmicVideoBlueprint(
                    "Animate captured scene with dynamic 3D camera pan, cinematic lighting, and 60fps Veo particle trails"
                )
                generatedResult = res
                isGenerating = false
            }
        }
    }

    // Camera Permission Launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Camera permission needed for scene capture", Toast.LENGTH_SHORT).show()
        }
    }

    // Gallery Picker Launcher (Images or Videos)
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedMediaUri = uri
            capturedBitmap = null
            Toast.makeText(context, "🎞️ Gallery Media Loaded! Constructing Motion Path...", Toast.LENGTH_SHORT).show()
            isGenerating = true
            scope.launch {
                val res = aiEngine.generateCosmicVideoBlueprint(
                    "Transform imported gallery media into cinematic Veo motion sequence with volumetric lighting"
                )
                generatedResult = res
                isGenerating = false
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "VideoAnim")
    val warpSweep by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "WarpSweep"
    )

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
                    text = "AI Video & Motion Studio",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(
                onClick = {
                    currentBlueprint = CosmicPrompts.getRandomVideoBlueprint()
                    capturedBitmap = null
                    selectedMediaUri = null
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Shuffle Video Blueprint",
                    tint = NeonCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Direct Camera & Gallery One-Tap Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CosmicOutlinedButton(
                text = "📸 Scene Camera",
                onClick = {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(null)
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                icon = Icons.Default.CameraAlt,
                borderColor = NeonCyan,
                modifier = Modifier
                    .weight(1f)
                    .testTag("video_camera_button")
            )

            CosmicOutlinedButton(
                text = "🎞️ Import Media",
                onClick = {
                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                },
                icon = Icons.Default.Collections,
                borderColor = NeonMagenta,
                modifier = Modifier
                    .weight(1f)
                    .testTag("video_gallery_button")
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Simulated Veo Video Motion Canvas with Media Overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, BorderCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .background(Color(0xFF060914)),
            contentAlignment = Alignment.Center
        ) {
            // Render captured or gallery media if present
            when {
                capturedBitmap != null -> {
                    Image(
                        bitmap = capturedBitmap!!.asImageBitmap(),
                        contentDescription = "Captured Scene",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                selectedMediaUri != null -> {
                    AsyncImage(
                        model = selectedMediaUri,
                        contentDescription = "Selected Media",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Veo Warp Motion Overlay
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (capturedBitmap != null || selectedMediaUri != null) Modifier.background(Color.Black.copy(alpha = 0.35f)) else Modifier
                    )
            ) {
                val w = size.width
                val h = size.height
                val center = Offset(w / 2f, h / 2f)

                // Warp lines simulation
                val lineCount = 30
                for (i in 0 until lineCount) {
                    val angle = (i.toFloat() / lineCount) * 2 * Math.PI.toFloat()
                    val progress = (warpSweep + (i.toFloat() / lineCount)) % 1f
                    val r1 = 20f + progress * (w * 0.5f)
                    val r2 = r1 + 40f

                    val startX = center.x + r1 * kotlin.math.cos(angle)
                    val startY = center.y + r1 * kotlin.math.sin(angle)
                    val endX = center.x + r2 * kotlin.math.cos(angle)
                    val endY = center.y + r2 * kotlin.math.sin(angle)

                    val color = if (i % 2 == 0) NeonCyan else NeonMagenta
                    drawLine(
                        color = color.copy(alpha = progress * 0.8f),
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = (progress * 4f).dp.toPx()
                    )
                }

                // Center singularity
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color.White, NeonCyan, Color.Transparent),
                        center = center,
                        radius = 40f
                    ),
                    radius = 40f,
                    center = center
                )
            }

            // Veo HUD overlay
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Black.copy(alpha = 0.7f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (capturedBitmap != null || selectedMediaUri != null) "VEO MEDIA MOTION ACTIVE" else "MODEL: veo-3.1-fast-generate",
                            color = NeonCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonMagenta.copy(alpha = 0.4f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = currentBlueprint.aspectRatio,
                            color = NeonMagenta,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isPlaying) Color.Red else Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "REC • 00:0${(warpSweep * 8).toInt()}:14",
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Text(
                        text = currentBlueprint.framerate,
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }
            }

            if (isGenerating) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.75f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = NeonMagenta, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Rendering Veo Volumetric Space-Time...",
                            color = NeonMagenta,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Scrubber Timeline Slider
        Slider(
            value = timelineProgress,
            onValueChange = { timelineProgress = it },
            colors = SliderDefaults.colors(
                thumbColor = NeonCyan,
                activeTrackColor = NeonCyan,
                inactiveTrackColor = BorderCyan.copy(alpha = 0.2f)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Video Blueprint Details Card
        CosmicCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Text(
                    text = "🎬 ${currentBlueprint.title}",
                    color = NeonCyan,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "• Camera Motion: ${currentBlueprint.cameraMotion}",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Text(
                    text = "• Atmospheric Lighting: ${currentBlueprint.lighting}",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Text(
                    text = "• Duration Target: ${currentBlueprint.duration}",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = generatedResult?.content ?: currentBlueprint.scenePrompt,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // One-Tap Auto Create Button
        CosmicPrimaryButton(
            text = if (isGenerating) "Synthesizing Veo Video..." else "🚀 One-Tap Auto Video Generation",
            onClick = {
                if (!isGenerating) {
                    isGenerating = true
                    scope.launch {
                        val res = aiEngine.generateCosmicVideoBlueprint(currentBlueprint.scenePrompt)
                        generatedResult = res
                        isGenerating = false
                        Toast.makeText(context, "Veo Video Blueprint Generated!", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            icon = Icons.Default.Videocam,
            enabled = !isGenerating,
            testTag = "video_auto_generate_button"
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CosmicOutlinedButton(
                text = "Copy Config",
                onClick = {
                    val textToCopy = generatedResult?.content ?: currentBlueprint.scenePrompt
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Veo Video Config", textToCopy))
                    Toast.makeText(context, "Video Payload Copied!", Toast.LENGTH_SHORT).show()
                },
                icon = Icons.Default.ContentCopy,
                modifier = Modifier.weight(1f)
            )

            CosmicOutlinedButton(
                text = "Share Video",
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "🎬 Generated with ASTRAM HMT Veo AI:\n\n${generatedResult?.content ?: currentBlueprint.scenePrompt}")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Share Veo Blueprint"))
                },
                icon = Icons.Default.Share,
                borderColor = NeonMagenta,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

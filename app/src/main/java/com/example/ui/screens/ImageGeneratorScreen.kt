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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Collections
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.R
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
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun ImageGeneratorScreen(
    aiEngine: AstramAiEngine,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val styles = listOf(
        "Cosmic Photorealism",
        "Synthwave Cyberpunk",
        "Anime Space Opera",
        "Holographic Blueprint",
        "Dark Nebula Fantasy"
    )

    var selectedStyle by remember { mutableStateOf(styles.first()) }
    var currentPrompt by remember { mutableStateOf(CosmicPrompts.getRandomImagePrompt()) }
    var generatedResult by remember { mutableStateOf<SavedCreationEntity?>(null) }
    var isGenerating by remember { mutableStateOf(false) }

    // Camera & Gallery Selected Media State
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var selectedGalleryUri by remember { mutableStateOf<Uri?>(null) }

    // Camera Capture Launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            capturedBitmap = bitmap
            selectedGalleryUri = null
            Toast.makeText(context, "📸 Photo Captured! Synthesizing Cosmic Vision...", Toast.LENGTH_SHORT).show()
            isGenerating = true
            scope.launch {
                val result = aiEngine.generateCosmicImage(
                    style = selectedStyle,
                    customPrompt = "Hyper-detailed cosmic transformation of captured photo subject with neon cyan and magenta aura shaders in $selectedStyle"
                )
                generatedResult = result
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
            Toast.makeText(context, "Camera permission needed to take photos", Toast.LENGTH_SHORT).show()
        }
    }

    // Gallery Picker Launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedGalleryUri = uri
            capturedBitmap = null
            Toast.makeText(context, "🖼️ Image Selected from Gallery! Auto-Enhancing...", Toast.LENGTH_SHORT).show()
            isGenerating = true
            scope.launch {
                val result = aiEngine.generateCosmicImage(
                    style = selectedStyle,
                    customPrompt = "Multimodal AI cosmic styling of imported gallery artwork with cinematic volumetric lighting in $selectedStyle"
                )
                generatedResult = result
                isGenerating = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CosmicDeepSpace)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Header
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
                    text = "AI Cosmic Image Generator",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(
                onClick = {
                    currentPrompt = CosmicPrompts.getRandomImagePrompt()
                    capturedBitmap = null
                    selectedGalleryUri = null
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Shuffle Cosmic Prompt",
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
                text = "📸 Camera Snap",
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
                    .testTag("image_camera_button")
            )

            CosmicOutlinedButton(
                text = "🖼️ Pick Gallery",
                onClick = {
                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                icon = Icons.Default.Collections,
                borderColor = NeonMagenta,
                modifier = Modifier
                    .weight(1f)
                    .testTag("image_gallery_button")
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Visual Display Frame (Live Captured / Gallery Media / Hero Banner)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, BorderCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .background(CosmicSurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            when {
                capturedBitmap != null -> {
                    Image(
                        bitmap = capturedBitmap!!.asImageBitmap(),
                        contentDescription = "Camera Capture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                selectedGalleryUri != null -> {
                    AsyncImage(
                        model = selectedGalleryUri,
                        contentDescription = "Gallery Selection",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> {
                    Image(
                        painter = painterResource(id = R.drawable.astram_hero_banner_1791129622599),
                        contentDescription = "Cosmic AI Render",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Tag overlay if custom media is loaded
            if (capturedBitmap != null || selectedGalleryUri != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (capturedBitmap != null) "LIVE CAMERA SOURCE" else "IMPORTED GALLERY MEDIA",
                        color = NeonCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (isGenerating) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.65f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = NeonCyan, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Synthesizing High-Dimensional Photons...",
                            color = NeonCyan,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Style Selector Chips
        Text(
            text = "Aesthetic Dimensions",
            color = TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(styles) { style ->
                CosmicChip(
                    text = style,
                    isSelected = selectedStyle == style,
                    onClick = { selectedStyle = style }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Auto Prompt Display (Zero-Prompt Rule: Ready to tap without manual typing)
        CosmicCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⚡ Zero-Prompt Auto Synthesizer",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = selectedStyle,
                        color = NeonMagenta,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = generatedResult?.content ?: currentPrompt,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons: One-Tap Auto Create
        CosmicPrimaryButton(
            text = if (isGenerating) "Generating Cosmic Art..." else "✨ Instant Auto Generate",
            onClick = {
                if (!isGenerating) {
                    isGenerating = true
                    scope.launch {
                        val result = aiEngine.generateCosmicImage(
                            style = selectedStyle,
                            customPrompt = currentPrompt
                        )
                        generatedResult = result
                        isGenerating = false
                        Toast.makeText(context, "Image prompt & render synthesized!", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            icon = Icons.Default.AutoAwesome,
            enabled = !isGenerating,
            testTag = "image_auto_generate_button"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Utility Actions: Copy Prompt, Platform Share
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CosmicOutlinedButton(
                text = "Copy Prompt",
                onClick = {
                    val promptToCopy = generatedResult?.content ?: currentPrompt
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("ASTRAM Image Prompt", promptToCopy))
                    Toast.makeText(context, "Copied Prompt to Clipboard", Toast.LENGTH_SHORT).show()
                },
                icon = Icons.Default.ContentCopy,
                modifier = Modifier.weight(1f)
            )

            CosmicOutlinedButton(
                text = "Share",
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "✨ Created with ASTRAM HMT Cosmic Studio:\n\n${generatedResult?.content ?: currentPrompt}")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Share Cosmic Image Concept"))
                },
                icon = Icons.Default.Share,
                borderColor = NeonMagenta,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

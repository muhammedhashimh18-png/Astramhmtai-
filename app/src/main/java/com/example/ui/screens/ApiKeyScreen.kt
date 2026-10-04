package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.GeminiApiClient
import com.example.data.pref.UserPreferences
import com.example.ui.components.CosmicCard
import com.example.ui.components.CosmicOutlinedButton
import com.example.ui.components.CosmicPrimaryButton
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CosmicDeepSpace
import com.example.ui.theme.CosmicGold
import com.example.ui.theme.CosmicGreen
import com.example.ui.theme.CosmicRed
import com.example.ui.theme.CosmicSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun ApiKeyScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }

    val activeKey = prefs.getEffectiveApiKey()
    var keyInput by remember { mutableStateOf(prefs.customApiKey.ifEmpty { activeKey }) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isTestingConnection by remember { mutableStateOf(false) }
    var testResultStatus by remember { mutableStateOf<String?>(null) }
    var isTestSuccessful by remember { mutableStateOf(false) }

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
                    text = "Gemini API Engine & Key",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Security & Status Banner Card
        CosmicCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = if (activeKey.isNotEmpty()) NeonCyan else CosmicGold
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = if (activeKey.isNotEmpty()) NeonCyan else CosmicGold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (activeKey.isNotEmpty()) "Cloud Reasoning Active" else "Local Smart Standby Mode",
                        color = if (activeKey.isNotEmpty()) NeonCyan else CosmicGold,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (activeKey.isNotEmpty())
                        "Your API key is securely encrypted and stored locally on your device for direct Gemini AI cloud requests."
                    else
                        "ASTRAM HMT is currently operating with built-in instant local templates. Add your Google Gemini API key below to unlock limitless real-time cloud thinking!",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Input Field
        Text(
            text = "Google Gemini API Key (AI Studio)",
            color = TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = keyInput,
            onValueChange = { keyInput = it },
            placeholder = { Text("AIzaSy...", color = TextSecondary, fontSize = 14.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("gemini_api_key_input"),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle key visibility",
                        tint = TextSecondary
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = BorderSubtle,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedContainerColor = CosmicSurfaceVariant,
                unfocusedContainerColor = CosmicSurfaceVariant
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
        CosmicPrimaryButton(
            text = "💾 Save Key Locally",
            onClick = {
                prefs.customApiKey = keyInput.trim()
                testResultStatus = null
                Toast.makeText(context, "API Key saved securely on device!", Toast.LENGTH_SHORT).show()
            },
            testTag = "save_api_key_button"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Test Connection Button
        CosmicOutlinedButton(
            text = if (isTestingConnection) "Testing Quantum Ping..." else "⚡ Test API Connection",
            onClick = {
                val keyToTest = keyInput.trim().ifEmpty { prefs.getEffectiveApiKey() }
                if (keyToTest.isEmpty()) {
                    Toast.makeText(context, "Please enter an API key first", Toast.LENGTH_SHORT).show()
                    return@CosmicOutlinedButton
                }
                isTestingConnection = true
                testResultStatus = null
                scope.launch {
                    val result = GeminiApiClient.generateContent(
                        apiKey = keyToTest,
                        model = "gemini-3.5-flash",
                        prompt = "Hello ASTRAM! Respond with 5 words confirming connection."
                    )
                    isTestingConnection = false
                    result.onSuccess { response ->
                        isTestSuccessful = true
                        testResultStatus = "✅ Connection Verified: \"$response\""
                    }.onFailure { error ->
                        isTestSuccessful = false
                        testResultStatus = "❌ Connection Failed: ${error.localizedMessage}"
                    }
                }
            },
            enabled = !isTestingConnection,
            modifier = Modifier.fillMaxWidth(),
            testTag = "test_api_connection_button"
        )

        if (testResultStatus != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isTestSuccessful) CosmicGreen.copy(alpha = 0.15f) else CosmicRed.copy(alpha = 0.15f))
                    .border(
                        1.dp,
                        if (isTestSuccessful) CosmicGreen else CosmicRed,
                        RoundedCornerShape(10.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = testResultStatus!!,
                    color = if (isTestSuccessful) CosmicGreen else CosmicRed,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

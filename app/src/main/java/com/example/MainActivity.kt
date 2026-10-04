package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.data.engine.AstramAiEngine
import com.example.data.voice.VoiceManager
import com.example.ui.navigation.MainScreen
import com.example.ui.theme.AstramHmtTheme

class MainActivity : ComponentActivity() {
    private lateinit var voiceManager: VoiceManager
    private lateinit var aiEngine: AstramAiEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        voiceManager = VoiceManager(this)
        aiEngine = AstramAiEngine(this)

        setContent {
            AstramHmtTheme {
                MainScreen(
                    aiEngine = aiEngine,
                    voiceManager = voiceManager
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceManager.shutdown()
    }
}

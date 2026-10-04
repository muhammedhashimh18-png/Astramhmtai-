package com.example.data.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

enum class VoiceOrbState {
    IDLE,
    LISTENING,
    THINKING,
    SPEAKING
}

class VoiceManager(private val context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    private var speechRecognizer: SpeechRecognizer? = null
    private var isRecognizerAvailable = false

    private val _orbState = MutableStateFlow(VoiceOrbState.IDLE)
    val orbState: StateFlow<VoiceOrbState> = _orbState.asStateFlow()

    private val _audioAmplitude = MutableStateFlow(0f)
    val audioAmplitude: StateFlow<Float> = _audioAmplitude.asStateFlow()

    private val _transcriptionResult = MutableStateFlow("")
    val transcriptionResult: StateFlow<String> = _transcriptionResult.asStateFlow()

    init {
        tts = TextToSpeech(context.applicationContext, this)
        isRecognizerAvailable = SpeechRecognizer.isRecognitionAvailable(context)
        if (isRecognizerAvailable) {
            initSpeechRecognizer()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                isTtsReady = true
            }
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _orbState.value = VoiceOrbState.SPEAKING
                }

                override fun onDone(utteranceId: String?) {
                    _orbState.value = VoiceOrbState.IDLE
                    _audioAmplitude.value = 0f
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _orbState.value = VoiceOrbState.IDLE
                    _audioAmplitude.value = 0f
                }
            })
        }
    }

    private fun initSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    _orbState.value = VoiceOrbState.LISTENING
                }

                override fun onBeginningOfSpeech() {
                    _orbState.value = VoiceOrbState.LISTENING
                }

                override fun onRmsChanged(rmsdB: Float) {
                    // Normalize rmsdB (typically -2 to 10) to 0.0 .. 1.0 amplitude
                    val normalized = ((rmsdB + 2f) / 12f).coerceIn(0.1f, 1.0f)
                    _audioAmplitude.value = normalized
                }

                override fun onBufferReceived(buffer: ByteArray?) {}

                override fun onEndOfSpeech() {
                    _orbState.value = VoiceOrbState.THINKING
                }

                override fun onError(error: Int) {
                    _orbState.value = VoiceOrbState.IDLE
                    _audioAmplitude.value = 0f
                }

                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val spokenText = matches?.firstOrNull() ?: ""
                    _transcriptionResult.value = spokenText
                    _orbState.value = VoiceOrbState.IDLE
                    _audioAmplitude.value = 0f
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val partial = matches?.firstOrNull() ?: ""
                    if (partial.isNotEmpty()) {
                        _transcriptionResult.value = partial
                    }
                }

                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }
    }

    fun startListening() {
        if (!isRecognizerAvailable || speechRecognizer == null) {
            // Simulate voice input if on emulator/restricted environment
            _orbState.value = VoiceOrbState.LISTENING
            return
        }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        try {
            speechRecognizer?.startListening(intent)
            _orbState.value = VoiceOrbState.LISTENING
        } catch (e: Exception) {
            Log.e("VoiceManager", "Error starting listening", e)
            _orbState.value = VoiceOrbState.IDLE
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
        } catch (e: Exception) {
            Log.e("VoiceManager", "Error stopping listening", e)
        }
        _orbState.value = VoiceOrbState.IDLE
    }

    fun speak(text: String, speed: Float = 1.0f) {
        if (!isTtsReady || tts == null) {
            _orbState.value = VoiceOrbState.SPEAKING
            return
        }
        tts?.setSpeechRate(speed)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "AstramUtterance_${System.currentTimeMillis()}")
    }

    fun stopSpeaking() {
        tts?.stop()
        _orbState.value = VoiceOrbState.IDLE
        _audioAmplitude.value = 0f
    }

    fun setOrbState(state: VoiceOrbState) {
        _orbState.value = state
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        speechRecognizer?.destroy()
    }
}

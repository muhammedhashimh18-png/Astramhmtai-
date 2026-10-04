package com.example.data.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiApiClient {
    private const val TAG = "GeminiApiClient"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/"

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun generateContent(
        apiKey: String,
        model: String = "gemini-3.5-flash",
        prompt: String,
        systemPrompt: String? = null,
        enableHighThinking: Boolean = false,
        conversationHistory: List<Pair<String, String>> = emptyList()
    ): Result<String> = withContext(Dispatchers.IO) {
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(
                IllegalStateException("Gemini API key is not configured. Please add your key in the API Key settings tab.")
            )
        }

        try {
            val url = "$BASE_URL$model:generateContent?key=$apiKey"

            val contentsArray = JSONArray()

            // Add conversation history if available
            for ((role, text) in conversationHistory) {
                val turnObj = JSONObject()
                turnObj.put("role", if (role.lowercase() == "user") "user" else "model")
                val parts = JSONArray()
                parts.put(JSONObject().put("text", text))
                turnObj.put("parts", parts)
                contentsArray.put(turnObj)
            }

            // Current prompt
            val currentTurn = JSONObject()
            currentTurn.put("role", "user")
            val currentParts = JSONArray()
            currentParts.put(JSONObject().put("text", prompt))
            currentTurn.put("parts", currentParts)
            contentsArray.put(currentTurn)

            val rootJson = JSONObject()
            rootJson.put("contents", contentsArray)

            // System prompt
            if (!systemPrompt.isNullOrBlank()) {
                val sysInstruction = JSONObject()
                val sysParts = JSONArray()
                sysParts.put(JSONObject().put("text", systemPrompt))
                sysInstruction.put("parts", sysParts)
                rootJson.put("systemInstruction", sysInstruction)
            }

            // Generation config
            val genConfig = JSONObject()
            genConfig.put("temperature", 0.7)
            if (enableHighThinking && model.contains("gemini-3.1-pro")) {
                val thinkingConfig = JSONObject()
                thinkingConfig.put("thinkingLevel", "HIGH")
                genConfig.put("thinkingConfig", thinkingConfig)
            }
            rootJson.put("generationConfig", genConfig)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = rootJson.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBodyString = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                val errorMessage = try {
                    val errorJson = JSONObject(responseBodyString)
                    errorJson.optJSONObject("error")?.optString("message") ?: "HTTP ${response.code}"
                } catch (e: Exception) {
                    "HTTP ${response.code}: $responseBodyString"
                }
                return@withContext Result.failure(Exception(errorMessage))
            }

            val json = JSONObject(responseBodyString)
            val candidates = json.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val contentObj = firstCandidate.optJSONObject("content")
                val parts = contentObj?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    val sb = StringBuilder()
                    for (i in 0 until parts.length()) {
                        val part = parts.getJSONObject(i)
                        val text = part.optString("text", "")
                        sb.append(text)
                    }
                    return@withContext Result.success(sb.toString().trim())
                }
            }

            Result.failure(Exception("No content returned in response from model."))
        } catch (e: Exception) {
            Log.e(TAG, "Error generating content", e)
            Result.failure(e)
        }
    }
}

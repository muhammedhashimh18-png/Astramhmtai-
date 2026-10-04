package com.example.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.CosmicPrompts
import com.example.data.local.AstramDatabase
import com.example.data.local.entities.QuizRecordEntity
import com.example.ui.components.CosmicCard
import com.example.ui.components.CosmicPrimaryButton
import com.example.ui.theme.BorderCyan
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
fun QuizScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = remember { AstramDatabase.getDatabase(context) }
    val dao = remember { db.astramDao() }

    val questions = remember { CosmicPrompts.QUIZ_PRESETS }
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var isQuizCompleted by remember { mutableStateOf(false) }

    val currentQ = questions[currentIndex]

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
                    text = "Cosmic Intelligence Quiz",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(CosmicSurfaceVariant)
                    .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "SCORE: $score / ${questions.size}",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!isQuizCompleted) {
            // Question Progress
            Text(
                text = "Question ${currentIndex + 1} of ${questions.size} • ${currentQ.topic}",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Question Card
            CosmicCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = currentQ.question,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Options
            currentQ.options.forEachIndexed { optIndex, optionText ->
                val isSelected = selectedOptionIndex == optIndex
                val isCorrect = optIndex == currentQ.correctAnswerIndex

                val borderColor = when {
                    isSubmitted && isCorrect -> CosmicGreen
                    isSubmitted && isSelected && !isCorrect -> CosmicRed
                    isSelected -> NeonCyan
                    else -> BorderCyan.copy(alpha = 0.3f)
                }

                val bgColor = when {
                    isSubmitted && isCorrect -> CosmicGreen.copy(alpha = 0.15f)
                    isSubmitted && isSelected && !isCorrect -> CosmicRed.copy(alpha = 0.15f)
                    isSelected -> CosmicSurfaceVariant
                    else -> CosmicDeepSpace
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(bgColor)
                        .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
                        .clickable(enabled = !isSubmitted) {
                            selectedOptionIndex = optIndex
                        }
                        .padding(14.dp)
                        .testTag("quiz_option_$optIndex")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${('A'.code + optIndex).toChar()}.  $optionText",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSubmitted && isCorrect) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Correct",
                                tint = CosmicGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isSubmitted) {
                // Explanation Card
                CosmicCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = if (selectedOptionIndex == currentQ.correctAnswerIndex) CosmicGreen else NeonMagenta
                ) {
                    Column {
                        Text(
                            text = if (selectedOptionIndex == currentQ.correctAnswerIndex) "✨ Correct Answer!" else "❌ Anomaly Detected",
                            color = if (selectedOptionIndex == currentQ.correctAnswerIndex) CosmicGreen else CosmicRed,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentQ.explanation,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                CosmicPrimaryButton(
                    text = if (currentIndex + 1 < questions.size) "Next Cosmic Question ➡️" else "Finish & Record Score 🏆",
                    onClick = {
                        if (currentIndex + 1 < questions.size) {
                            currentIndex++
                            selectedOptionIndex = null
                            isSubmitted = false
                        } else {
                            isQuizCompleted = true
                            scope.launch {
                                dao.insertQuizRecord(
                                    QuizRecordEntity(
                                        topic = "Cosmic AI & Astrophysics",
                                        score = score,
                                        totalQuestions = questions.size,
                                        percentage = (score.toFloat() / questions.size) * 100f
                                    )
                                )
                            }
                        }
                    }
                )
            } else {
                CosmicPrimaryButton(
                    text = "Submit Answer",
                    enabled = selectedOptionIndex != null,
                    onClick = {
                        isSubmitted = true
                        if (selectedOptionIndex == currentQ.correctAnswerIndex) {
                            score++
                        }
                    },
                    testTag = "quiz_submit_button"
                )
            }
        } else {
            // Completed Summary Screen
            CosmicCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🏆 Mastery Assessment Complete!",
                        color = CosmicGold,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "$score / ${questions.size} Points (${((score.toFloat() / questions.size) * 100).toInt()}%)",
                        color = NeonCyan,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (score >= 3) "Excellent quantum mastery! Your neural pathways are operating at peak efficiency." else "Good effort! Review the study modules to sharpen your cosmic knowledge.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            CosmicPrimaryButton(
                text = "Retake Quiz",
                icon = Icons.Default.Refresh,
                onClick = {
                    currentIndex = 0
                    selectedOptionIndex = null
                    isSubmitted = false
                    score = 0
                    isQuizCompleted = false
                }
            )
        }
    }
}

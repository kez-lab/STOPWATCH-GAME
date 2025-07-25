package io.github.kez_lab.stopwatch_game.ui.screens.game.play

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kez_lab.stopwatch_game.model.GameType
import io.github.kez_lab.stopwatch_game.ui.components.TimerDisplay
import io.github.kez_lab.stopwatch_game.ui.viewmodel.LocalAppViewModel
import io.github.kez_lab.stopwatch_game.viewmodel.AppViewModel
import kotlinx.coroutines.delay

@Composable
internal fun ResultScreen(
    game: GameType,
    onNextPlayer: () -> Unit,
) {
    val appViewModel: AppViewModel = LocalAppViewModel.current
    val appUiState by appViewModel.uiState.collectAsState()
    val gameResult = appUiState.currentPlayer?.gameResults?.lastOrNull()

    // Animation
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    // Haptic feedback
    val hapticFeedback = LocalHapticFeedback.current
    LaunchedEffect(Unit) {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        delay(300)
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (gameResult != null) {
            // Result status text
            Box(
                modifier = Modifier.graphicsLayer {
                    alpha = animationProgress.value
                    scaleX = animationProgress.value
                    scaleY = animationProgress.value
                }
            ) {
                val text = "완료!"
                val color = MaterialTheme.colorScheme.tertiary
                Text(
                    text = text,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = color,
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Result time display
            Box(
                modifier = Modifier.graphicsLayer { alpha = animationProgress.value }
            ) {
                TimerDisplay(
                    displayTime = gameResult.formattedTime,
                    isFinished = true,
                )
            }

            // Game-specific result display
            when (game) {
                GameType.RandomMS -> {
                    ResultCard("끝자리", gameResult.specialValue.toString())
                }

                GameType.CONG_PAT -> {
                    val first = gameResult.specialValue
                    val second = gameResult.specialValue2 ?: -1
                    val total = if (first != -1 && second != -1) first * second else 0

                    if (first != -1 && second != -1) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ResultCard("첫번째 소수점", first.toString())
                            Spacer(modifier = Modifier.width(16.dp))
                            ResultCard("두번째 소수점", second.toString())
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        ResultCard("곱셈 결과", "$first × $second = $total")
                    } else {
                        ResultCard("오류", "게임 데이터를 불러올 수 없습니다", isEmphasized = true)
                    }
                }
            }
        } else {
            Text("결과를 불러올 수 없습니다.")
        }


        Spacer(modifier = Modifier.height(40.dp))

        // Next button
        Button(
            onClick = onNextPlayer,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp)
                .graphicsLayer { alpha = animationProgress.value },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = "다음 플레이어",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ResultCard(title: String, value: String, isEmphasized: Boolean = false) {
    val containerColor =
        if (isEmphasized) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.secondaryContainer
    val contentColor =
        if (isEmphasized) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSecondaryContainer

    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
            )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            Text(
                text = value,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = contentColor
            )
        }
    }
}


package io.github.kez_lab.stopwatch_game.ui.screens.game.play

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import io.github.kez_lab.stopwatch_game.viewmodel.GameTimerViewModel
import kotlinx.coroutines.delay

@Composable
internal fun CountdownScreen(
    navController: NavController,
    timerViewModel: GameTimerViewModel,
) {
    var countdown by remember { mutableStateOf(3) }
    val hapticFeedback = LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        while (countdown > 0) {
            delay(1000)
            countdown--
        }
        timerViewModel.startTimer()
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        navController.navigate(GamePlayRoutes.Playing.route) {
            popUpTo(GamePlayRoutes.Countdown.route) { inclusive = true }
        }
    }

    // Animation
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(countdown) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
        )
    }

    val scale = lerp(2f, 1f, animationProgress.value)
    val alpha = lerp(0f, 1f, animationProgress.value)
    val countdownColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawCircle(
                    color = countdownColor.copy(alpha = 0.1f * animationProgress.value),
                    radius = size.minDimension * 0.6f * (1f + (1f - animationProgress.value))
                )
                drawCircle(
                    color = countdownColor.copy(alpha = 0.05f),
                    radius = size.minDimension * 0.5f * (1f + (1f - animationProgress.value)),
                    style = Stroke(width = 8f)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "준비...",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.alpha(alpha)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (countdown > 0) countdown.toString() else "GO!",
                fontSize = 160.sp,
                fontWeight = FontWeight.ExtraBold,
                color = countdownColor,
                modifier = Modifier
                    .scale(scale)
                    .alpha(alpha)
                    .drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    countdownColor.copy(alpha = 0.3f),
                                    Color.Transparent
                                ),
                                radius = size.minDimension * 0.8f
                            ),
                            radius = size.minDimension * 0.5f
                        )
                    }
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Cancel button
            Button(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    navController.popBackStack() // Go back to ReadyScreen
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                modifier = Modifier
                    .alpha(alpha)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "준비 취소",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

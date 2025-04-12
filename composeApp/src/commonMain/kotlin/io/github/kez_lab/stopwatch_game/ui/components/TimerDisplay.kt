package io.github.kez_lab.stopwatch_game.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 타이머 디스플레이 컴포넌트
 * @param time 표시할 시간 (밀리초)
 * @param targetTime 목표 시간 (밀리초)
 * @param isRunning 실행 중 여부
 * @param isFinished 완료 여부
 * @param isTimeout 시간 초과 여부
 * @param timerColor 타이머 색상
 */
@Composable
fun TimerDisplay(
    displayTime: String,
    isRunning: Boolean = false,
    isFinished: Boolean = false,
    timerColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    // 시간 상태 (애니메이션 생략으로 깜빡임 최소화)
    val displayedTime by rememberUpdatedState(displayTime)

    // 화면 너비에 따라 폰트 크기 조절
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        val fontSize = if (maxWidth < 400.dp) 48.sp else 64.sp

        val scale by animateFloatAsState(
            targetValue = if (isRunning && !isFinished) 1.03f else 1f,
            animationSpec = tween(durationMillis = 500),
            label = "Pulse Scale"
        )

        Surface(
            modifier = Modifier
                .scale(scale)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            tonalElevation = if (isRunning) 4.dp else 1.dp,
            border = if (isRunning || isFinished) {
                BorderStroke(2.dp, timerColor)
            } else null,
            color = MaterialTheme.colorScheme.surface,
            contentColor = timerColor
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayedTime,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = timerColor,
                    textAlign = TextAlign.Center,
                    letterSpacing = 2.sp,
                    modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

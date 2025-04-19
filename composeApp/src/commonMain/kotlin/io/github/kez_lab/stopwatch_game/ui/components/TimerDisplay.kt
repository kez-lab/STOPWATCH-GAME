package io.github.kez_lab.stopwatch_game.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 게임용 타이머 디스플레이 컴포넌트
 * @param displayTime 표시할 시간
 * @param isRunning 실행 중 여부
 * @param isFinished 완료 여부
 * @param modifier 수정자
 */
@Composable
fun TimerDisplay(
    displayTime: String,
    isRunning: Boolean = false,
    isFinished: Boolean = false,
    modifier: Modifier = Modifier
) {
    // 타이머 색상
    val timerColor by animateColorAsState(
        targetValue = when {
            isFinished && displayTime.startsWith("TIME") -> MaterialTheme.colorScheme.error
            isFinished -> MaterialTheme.colorScheme.tertiary
            isRunning -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        },
        label = "Timer color animation"
    )
    val colorScheme = MaterialTheme.colorScheme
    // 디지털 타이머 글로우 효과를 위한 네온 컬러
    val glowColor = remember(timerColor) {
        when {
            isFinished && displayTime.startsWith("TIME") -> colorScheme.error.copy(
                alpha = 0.2f
            )

            isFinished -> colorScheme.tertiary.copy(alpha = 0.2f)
            isRunning -> colorScheme.primary.copy(alpha = 0.2f)
            else -> colorScheme.onSurface.copy(alpha = 0.1f)
        }
    }

    // 시간 상태 (애니메이션 생략으로 깜빡임 최소화)
    val displayedTime by rememberUpdatedState(displayTime)

    // 진동/펄스 애니메이션 (실행 중일 때)
    val pulse by animateFloatAsState(
        targetValue = if (isRunning && !isFinished) 1.03f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Pulse Animation"
    )

    // 화면 너비에 따라 폰트 크기 조절
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {

        Surface(
            modifier = Modifier
                .scale(pulse)
                .fillMaxWidth()
                .shadow(
                    elevation = if (isRunning) 16.dp else 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = timerColor.copy(alpha = 0.3f)
                ),
            shape = RoundedCornerShape(24.dp),
            tonalElevation = if (isRunning) 8.dp else 2.dp,
            border = if (isRunning || isFinished) {
                BorderStroke(3.dp, timerColor)
            } else null,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
            contentColor = timerColor
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp, horizontal = 16.dp)
                    .drawBehind {
                        // 게임 느낌의 디지털 타이머 배경 그리기
                        if (isRunning || isFinished) {
                            // 내부 글로우 효과
                            drawRoundRect(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        glowColor,
                                        Color.Transparent,
                                    ),
                                    center = Offset(size.width / 2, size.height / 2),
                                    radius = size.width * 0.8f
                                ),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f),
                                alpha = 0.7f
                            )

                            // 디지털 타이머 그리드 라인
                            val gridLineColor = timerColor.copy(alpha = 0.1f)
                            val gridSpacing = size.height / 8

                            // 수평선
                            for (i in 0..8) {
                                val y = i * gridSpacing
                                drawLine(
                                    color = gridLineColor,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = 1f
                                )
                            }

                            // 수직선
                            val verticalSpacing = size.width / 10
                            for (i in 0..10) {
                                val x = i * verticalSpacing
                                drawLine(
                                    color = gridLineColor,
                                    start = Offset(x, 0f),
                                    end = Offset(x, size.height),
                                    strokeWidth = 1f
                                )
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayedTime,
                    fontSize = 40.sp,
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

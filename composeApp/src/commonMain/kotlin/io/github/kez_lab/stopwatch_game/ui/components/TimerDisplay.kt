package io.github.kez_lab.stopwatch_game.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
 * 개선된 타이머 디스플레이 컴포넌트
 * @param time 표시할 시간 문자열
 * @param modifier 수정자
 * @param isLarge 큰 디스플레이 여부
 * @param isRunning 실행 중 여부
 * @param isTimeout 시간 초과 여부
 * @param isFinished 완료 여부
 */
@Composable
fun TimerDisplay(
    time: String,
    modifier: Modifier = Modifier,
    isLarge: Boolean = true,
    isRunning: Boolean = false,
    isTimeout: Boolean = false,
    isFinished: Boolean = false
) {
    // 이전 시간 값 기억 (애니메이션 최적화용)
    var previousTime by remember { mutableStateOf(time) }

    // 시간 값이 변경된 경우에만 업데이트 (불필요한 재구성 방지)
    SideEffect {
        if (time != previousTime && !isFinished) {
            previousTime = time
        }
    }

    // 화면 너비에 따라 폰트 크기 조절
    BoxWithConstraints {
        val fontSize = if (isLarge) {
            if (maxWidth < 400.dp) 48.sp else 64.sp
        } else {
            if (maxWidth < 400.dp) 32.sp else 42.sp
        }
        
        // 색상 애니메이션 (더 부드럽게)
        val textColor by animateColorAsState(
            targetValue = when {
                isTimeout -> MaterialTheme.colorScheme.error
                isFinished -> MaterialTheme.colorScheme.tertiary
                isRunning -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.onSurface
            },
            animationSpec = tween(500), 
            label = "Timer text color animation"
        )
        
        // 실행 중일 때 부드러운 깜빡이는 효과
        val infiniteTransition = rememberInfiniteTransition(label = "Pulse animation")
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = if (isRunning) 1.03f else 1f,  // 미묘한 크기 변화로 더 부드럽게
            animationSpec = infiniteRepeatable(
                animation = tween(1000),  // 더 긴 시간으로 부드럽게
                repeatMode = RepeatMode.Reverse
            ),
            label = "Scale animation"
        )
        
        // 카드 사용으로 더 일관성 있는 디자인
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .scale(if (isRunning && !isFinished) scale else 1f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = textColor
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (isRunning) 6.dp else 2.dp,
                focusedElevation = 8.dp
            ),
            border = if (isRunning || isFinished) {
                androidx.compose.foundation.BorderStroke(
                    width = 2.dp,
                    color = if (isFinished) {
                        if (isTimeout) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            } else null
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                // 시간 표시 (부드러운 애니메이션 적용)
                AnimatedContent(
                    targetState = if (isFinished) time else previousTime,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(200)) togetherWith 
                                fadeOut(animationSpec = tween(200))
                    },
                    label = "Timer content animation"
                ) { targetTime ->
                    Text(
                        text = targetTime,
                        fontSize = fontSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = textColor,
                        textAlign = TextAlign.Center,
                        letterSpacing = 2.sp,
                        modifier = Modifier.wrapContentSize(Alignment.Center)
                    )
                }
            }
        }
    }
}

/**
 * 작은 타이머 디스플레이 컴포넌트
 */
@Composable
fun SmallTimerDisplay(
    time: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = color
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        BoxWithConstraints {
            val fontSize = if (maxWidth < 300.dp) 18.sp else 24.sp
            
            Text(
                text = time,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontSize = fontSize,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Monospace,
                color = color,
                textAlign = TextAlign.Center
            )
        }
    }
} 
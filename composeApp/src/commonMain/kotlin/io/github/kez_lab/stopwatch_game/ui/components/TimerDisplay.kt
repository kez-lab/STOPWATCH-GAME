package io.github.kez_lab.stopwatch_game.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay

/**
 * 타이머 디스플레이 컴포넌트
 * @param time 표시할 시간 문자열
 * @param modifier 수정자
 * @param isLarge 큰 디스플레이 여부
 * @param isRunning 실행 중 여부
 * @param isTimeout 시간 초과 여부
 */
@Composable
fun TimerDisplay(
    time: String,
    modifier: Modifier = Modifier,
    isLarge: Boolean = true,
    isRunning: Boolean = false,
    isTimeout: Boolean = false
) {
    // 화면 너비에 따라 폰트 크기 조절
    BoxWithConstraints {
        val fontSize = if (isLarge) {
            if (maxWidth < 400.dp) 48.sp else 64.sp
        } else {
            if (maxWidth < 400.dp) 32.sp else 42.sp
        }
        
        // 색상 애니메이션
        val textColor by animateColorAsState(
            targetValue = when {
                isTimeout -> MaterialTheme.colorScheme.error
                isRunning -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.onSurface
            },
            animationSpec = tween(300), 
            label = "Timer text color animation"
        )
        
        // 실행 중일 때 깜빡이는 효과
        val infiniteTransition = rememberInfiniteTransition(label = "Pulse animation")
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = if (isRunning) 1.05f else 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(700),
                repeatMode = RepeatMode.Reverse
            ),
            label = "Scale animation"
        )
        
        // 중앙에 확실히 표시되도록 Surface를 사용하여 배경 추가
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .scale(if (isRunning) scale else 1f),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(12.dp),
            border = if (isRunning) {
                androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            } else null,
            shadowElevation = if (isRunning) 4.dp else 2.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                // 시간 표시 (애니메이션 적용)
                AnimatedContent(
                    targetState = time,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(150)) togetherWith 
                                fadeOut(animationSpec = tween(150))
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
                        letterSpacing = 2.sp,  // 더 가독성 좋게 글자 간격 추가
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
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.padding(4.dp),
        shadowElevation = 1.dp
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
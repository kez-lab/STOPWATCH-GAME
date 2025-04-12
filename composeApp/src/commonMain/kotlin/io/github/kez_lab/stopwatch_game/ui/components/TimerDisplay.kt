package io.github.kez_lab.stopwatch_game.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    val textColor by animateColorAsState(
        targetValue = when {
            isTimeout -> MaterialTheme.colorScheme.error
            isRunning -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.onSurface
        },
        animationSpec = tween(300), 
        label = "Timer text color animation"
    )
    
    // 중앙에 확실히 표시되도록 Surface를 사용하여 배경 추가
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp),
        border = if (isRunning) {
            androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else null,
        shadowElevation = 2.dp
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
                    fontSize = if (isLarge) 64.sp else 42.sp,
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
        shape = RoundedCornerShape(4.dp),
        modifier = modifier.padding(4.dp)
    ) {
        Text(
            text = time,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Monospace,
            color = color,
            textAlign = TextAlign.Center
        )
    }
} 
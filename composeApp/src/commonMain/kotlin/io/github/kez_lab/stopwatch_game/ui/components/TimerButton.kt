package io.github.kez_lab.stopwatch_game.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Play
import compose.icons.feathericons.Square

/**
 * 타이머 제어 버튼 컴포넌트
 * @param isRunning 실행 중 여부
 * @param onClick 클릭 이벤트 핸들러
 * @param modifier 수정자
 * @param isEnabled 활성화 여부
 */
@Composable
fun TimerButton(
    isRunning: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    val buttonColor by animateColorAsState(
        targetValue = if (isRunning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        animationSpec = tween(300),
        label = "Button color animation"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (isRunning) 1.1f else 1.0f,
        animationSpec = tween(300),
        label = "Button scale animation"
    )
    
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .size(120.dp)
                .scale(scale),
            shape = CircleShape,
            enabled = isEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor
            )
        ) {
            AnimatedContent(
                targetState = isRunning,
                label = "Button content animation"
            ) { running ->
                if (running) {
                    // 정지 버튼
                    Icon(
                        imageVector = FeatherIcons.Square,
                        contentDescription = "정지",
                        modifier = Modifier.size(32.dp)
                    )
                } else {
                    // 시작 버튼
                    Icon(
                        imageVector = FeatherIcons.Play,
                        contentDescription = "시작",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

/**
 * 작은 타이머 제어 버튼 컴포넌트
 */
@Composable
fun SmallTimerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    isEnabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
} 
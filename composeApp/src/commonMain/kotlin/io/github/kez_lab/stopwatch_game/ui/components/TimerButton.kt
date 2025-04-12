package io.github.kez_lab.stopwatch_game.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
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
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // 화면 크기에 따라 버튼 크기 조절
        val buttonSize = if (maxWidth < 400.dp) 100.dp else 120.dp
        val iconSize = if (maxWidth < 400.dp) 28.dp else 32.dp
        
        // 인터랙션 소스 (눌림 상태 감지)
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        
        // 색상 애니메이션
        val buttonColor by animateColorAsState(
            targetValue = when {
                !isEnabled -> MaterialTheme.colorScheme.surfaceVariant
                isRunning -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.primary
            },
            animationSpec = tween(300),
            label = "Button color animation"
        )
        
        // 크기 애니메이션
        val scale by animateFloatAsState(
            targetValue = when {
                isPressed -> 0.95f
                isRunning -> 1.1f
                else -> 1.0f
            },
            animationSpec = tween(150),
            label = "Button scale animation"
        )
        
        // 회전 애니메이션 (실행 중일 때 약간 회전)
        val rotation by animateFloatAsState(
            targetValue = if (isRunning) 0f else -90f,
            animationSpec = tween(300),
            label = "Button rotation animation"
        )
        
        Button(
            onClick = onClick,
            modifier = Modifier
                .size(buttonSize)
                .scale(scale)
                .graphicsLayer {
                    if (!isRunning) {
                        rotationZ = rotation
                    }
                },
            shape = CircleShape,
            enabled = isEnabled,
            interactionSource = interactionSource,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp,
                focusedElevation = 6.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
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
                        modifier = Modifier.size(iconSize)
                    )
                } else {
                    // 시작 버튼
                    Icon(
                        imageVector = FeatherIcons.Play,
                        contentDescription = "시작",
                        modifier = Modifier.size(iconSize)
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
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        // 인터랙션 소스 (눌림 상태 감지)
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        
        // 크기에 따른 패딩 및 높이 조정
        val buttonHeight = if (maxWidth < 400.dp) 50.dp else 60.dp
        val horizontalPadding = if (maxWidth < 400.dp) 12.dp else 16.dp
        val verticalPadding = if (maxWidth < 400.dp) 6.dp else 8.dp
        val fontSize = if (maxWidth < 400.dp) 14.sp else 16.sp
        
        // 스케일 애니메이션
        val scale by animateFloatAsState(
            targetValue = if (isPressed) 0.98f else 1f,
            animationSpec = tween(150),
            label = "Small button scale animation"
        )
        
        Button(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .height(buttonHeight)
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
                .scale(scale),
            enabled = isEnabled,
            interactionSource = interactionSource,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp,
                pressedElevation = 0.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = color,
                disabledContainerColor = color.copy(alpha = 0.5f)
            )
        ) {
            Text(
                text = text,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
        }
    }
} 
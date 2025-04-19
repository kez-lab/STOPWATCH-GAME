package io.github.kez_lab.stopwatch_game.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.User
import io.github.kez_lab.stopwatch_game.model.Player

/**
 * 공통 앱바 컴포넌트
 *
 * @param title 앱바 제목
 * @param onBackClick 뒤로가기 버튼 클릭 핸들러
 * @param modifier 수정자
 * @param navigationType 네비게이션 타입 (BACK, NONE)
 * @param actions 우측 액션 버튼들 (선택 사항)
 * @param currentPlayer 현재 플레이어 (선택 사항)
 * @param showPlayerInfo 플레이어 정보 표시 여부
 */
@Composable
fun AppBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigationType: NavigationType = NavigationType.BACK,
    actions: @Composable () -> Unit = {},
    currentPlayer: Player? = null,
    showPlayerInfo: Boolean = false
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // 상단 앱바 (제목 및 네비게이션)
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 왼쪽 영역 (네비게이션 버튼)
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                when (navigationType) {
                    NavigationType.BACK -> {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = FeatherIcons.ArrowLeft,
                                contentDescription = "뒤로 가기"
                            )
                        }
                    }
                    NavigationType.NONE -> {
                        // 네비게이션 없음, 빈 공간으로 패딩 유지
                    }
                }
            }
            
            // 중앙 영역 (제목)
            Box(
                modifier = Modifier.weight(2f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            
            // 오른쪽 영역 (액션 버튼들)
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                actions()
            }
        }

        // 플레이어 정보 카드 (표시하도록 설정된 경우)
        if (showPlayerInfo && currentPlayer != null) {
            Spacer(modifier = Modifier.height(8.dp))
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = FeatherIcons.User,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.size(12.dp))

                    Text(
                        text = currentPlayer.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

/**
 * 커스텀 액션 아이템 컴포넌트
 */
@Composable
fun AppBarActionItem(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    IconButton(
        onClick = onClick,
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (enabled) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            }
        )
    }
}

/**
 * 네비게이션 타입 열거형
 */
enum class NavigationType {
    BACK,   // 뒤로가기 버튼
    NONE    // 네비게이션 없음
} 
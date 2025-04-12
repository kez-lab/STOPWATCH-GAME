package io.github.kez_lab.stopwatch_game.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Clock
import compose.icons.feathericons.AlertCircle
import compose.icons.feathericons.Star
import compose.icons.feathericons.Users
import compose.icons.feathericons.ZapOff
import io.github.kez_lab.stopwatch_game.model.Game
import io.github.kez_lab.stopwatch_game.model.GameType

/**
 * 게임 선택 카드 컴포넌트
 * @param game 게임 정보
 * @param onClick 클릭 이벤트 핸들러
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameCard(
    game: Game,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 게임 아이콘
            Icon(
                imageVector = getGameIcon(game.gameType),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = getGameColor(game.gameType)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 게임 이름
                Text(
                    text = game.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // 게임 설명
                Text(
                    text = game.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 난이도 표시
                Row {
                    repeat(5) { index ->
                        val filled = index < game.difficultyLevel
                        Icon(
                            imageVector = FeatherIcons.Star,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (filled) MaterialTheme.colorScheme.primary 
                                  else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                }
            }
        }
    }
}

/**
 * 게임 타입에 따른 아이콘 반환
 */
@Composable
private fun getGameIcon(gameType: GameType) = when (gameType) {
    GameType.EXACT_STOP -> FeatherIcons.Clock
    GameType.SLOWEST_STOP -> FeatherIcons.ZapOff
    GameType.RANDOM_MATCH -> FeatherIcons.AlertCircle
    GameType.LAST_PERSON -> FeatherIcons.Users
    GameType.MS_DIGIT -> FeatherIcons.Clock
}

/**
 * 게임 타입에 따른 색상 반환
 */
@Composable
private fun getGameColor(gameType: GameType): Color = when (gameType) {
    GameType.EXACT_STOP -> MaterialTheme.colorScheme.primary
    GameType.SLOWEST_STOP -> MaterialTheme.colorScheme.tertiary
    GameType.RANDOM_MATCH -> MaterialTheme.colorScheme.secondary
    GameType.LAST_PERSON -> MaterialTheme.colorScheme.primary
    GameType.MS_DIGIT -> MaterialTheme.colorScheme.tertiary
} 
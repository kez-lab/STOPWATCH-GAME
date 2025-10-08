package io.github.kez_lab.stopwatch_game.ui.screens.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Users
import io.github.kez_lab.stopwatch_game.model.Player

@Composable
fun ColumnScope.PlayerListSection(
    players: List<Player>,
    onRemove: (Player) -> Unit,
    modifier: Modifier = Modifier
) {
    if (players.isEmpty()) {
        EmptyPlayerHint(modifier = modifier)
    } else {
        PlayerList(
            players = players,
            onRemove = onRemove,
            modifier = modifier
        )
    }
}

@Composable
private fun ColumnScope.PlayerList(
    players: List<Player>,
    onRemove: (Player) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .weight(1f)
            .fillMaxWidth()
    ) {
        items(players) { player ->
            PlayerItem(
                player = player,
                onRemove = { onRemove(player) }
            )
        }
    }
}

@Composable
private fun EmptyPlayerHint(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.weight(0.3f))
            Icon(
                imageVector = FeatherIcons.Users,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "게임에 참여할 친구를 등록해 주세요",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.60f)
            )
            Text(
                text = "최소 2명 이상의 플레이어가 필요합니다",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.60f),
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(Modifier.weight(0.7f))
        }
    }
}
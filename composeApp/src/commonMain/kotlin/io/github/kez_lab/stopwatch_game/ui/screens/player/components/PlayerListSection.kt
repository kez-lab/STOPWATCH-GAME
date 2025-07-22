package io.github.kez_lab.stopwatch_game.ui.screens.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.sp
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
            Spacer(Modifier.weight(0.4f))
            Icon(
                imageVector = FeatherIcons.Users,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "플레이어를 추가해 주세요",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Spacer(Modifier.weight(0.6f))
        }
    }
}
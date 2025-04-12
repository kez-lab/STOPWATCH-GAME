package io.github.kez_lab.stopwatch_game.ui.screens.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowRight
import compose.icons.feathericons.Plus
import compose.icons.feathericons.Trash2
import compose.icons.feathericons.User
import compose.icons.feathericons.UserPlus
import compose.icons.feathericons.Users
import io.github.kez_lab.stopwatch_game.model.Player
import io.github.kez_lab.stopwatch_game.ui.navigation.Routes
import io.github.kez_lab.stopwatch_game.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerRegistrationScreen(navController: NavHostController) {
    val appViewModel: AppViewModel = viewModel()

    val players = remember { mutableStateListOf<Player>() }
    var newPlayerName by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "플레이어 등록",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            )
        },
        bottomBar = {
            if (players.size >= 2) {
                Button(
                    onClick = {
                        appViewModel.registerPlayers(players)
                        navController.navigate(Routes.GAME_SELECTION) {
                            popUpTo(Routes.HOME) {
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(60.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "다음으로 이동",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = FeatherIcons.ArrowRight,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "🎮 게임에 참여할 친구를 등록해 주세요!",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            InputSection(
                name = newPlayerName,
                onNameChange = {
                    newPlayerName = it
                    isError = false
                },
                onAdd = {
                    addPlayer(newPlayerName, players) { newName, error ->
                        newPlayerName = newName
                        isError = error
                    }
                },
                isError = isError,
                enabled = players.size < 6
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (players.isEmpty()) {
                EmptyPlayerHint()
            } else {
                PlayerList(players = players, onRemove = { players.remove(it) })
            }
        }
    }
}

// 플레이어 추가 함수
private fun addPlayer(
    name: String,
    players: MutableList<Player>,
    callback: (String, Boolean) -> Unit
) {
    val trimmedName = name.trim()
    if (trimmedName.isEmpty()) {
        callback("", true)
        return
    }

    // 플레이어 추가
    players.add(Player(name = trimmedName))
    callback("", false) // 입력 필드 초기화
}

@Composable
private fun InputSection(
    name: String,
    onNameChange: (String) -> Unit,
    onAdd: () -> Unit,
    isError: Boolean,
    enabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            modifier = Modifier.weight(1f),
            label = { Text("플레이어 이름") },
            placeholder = { Text("이름을 입력하세요") },
            singleLine = true,
            isError = isError,
            supportingText = if (isError) {
                { Text("⚠ 이름을 입력해주세요") }
            } else null,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onAdd() }),
            leadingIcon = {
                Icon(
                    imageVector = FeatherIcons.UserPlus,
                    contentDescription = null
                )
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onAdd,
            enabled = enabled
        ) {
            Icon(
                imageVector = FeatherIcons.Plus,
                contentDescription = "추가"
            )
        }
    }
}

@Composable
private fun ColumnScope.PlayerList(players: List<Player>, onRemove: (Player) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
    ) {
        items(players) { player ->
            PlayerItem(player = player, onRemove = { onRemove(player) })
        }
    }
}

@Composable
private fun PlayerItem(player: Player, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
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
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = player.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = FeatherIcons.Trash2,
                    contentDescription = "삭제",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.EmptyPlayerHint() {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = FeatherIcons.Users,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "아직 등록된 플레이어가 없습니다",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

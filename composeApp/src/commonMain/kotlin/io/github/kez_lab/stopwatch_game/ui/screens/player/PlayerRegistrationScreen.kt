package io.github.kez_lab.stopwatch_game.ui.screens.player

import androidx.compose.animation.AnimatedVisibility
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
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.ArrowRight
import compose.icons.feathericons.Plus
import compose.icons.feathericons.Trash2
import compose.icons.feathericons.User
import compose.icons.feathericons.UserPlus
import compose.icons.feathericons.Users
import io.github.kez_lab.stopwatch_game.model.Player
import io.github.kez_lab.stopwatch_game.ui.navigation.LocalNavigationController
import io.github.kez_lab.stopwatch_game.ui.navigation.Screen
import io.github.kez_lab.stopwatch_game.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerRegistrationScreen() {
    val navigationController = LocalNavigationController.current
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
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigationController.goBack() }) {
                        Icon(
                            imageVector = FeatherIcons.ArrowLeft,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (players.size >= 2) {
                Button(
                    onClick = {
                        appViewModel.registerPlayers(players)
                        navigationController.navigateTo(Screen.GameSelection)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("다음으로 이동")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(imageVector = FeatherIcons.ArrowRight, contentDescription = null)
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

@Composable
private fun BottomGuide(show: Boolean) {
    AnimatedVisibility(visible = show) {
        Text(
            text = "🎉 모두 등록했어요! 다음을 눌러 게임을 선택해 주세요",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )
    }
}

private fun addPlayer(
    name: String,
    players: MutableList<Player>,
    onResult: (newName: String, error: Boolean) -> Unit
) {
    if (name.isBlank()) {
        onResult(name, true)
        return
    }

    if (players.size >= 6) return

    players.add(Player(name = name.trim()))
    onResult("", false)
}

@Composable
private fun PlayerItem(player: Player, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = player.name,
                fontSize = 18.sp,
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

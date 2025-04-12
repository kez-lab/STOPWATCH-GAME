package io.github.kez_lab.stopwatch_game.ui.screens.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import compose.icons.feathericons.UserPlus
import compose.icons.feathericons.Users
import io.github.kez_lab.stopwatch_game.model.Player
import io.github.kez_lab.stopwatch_game.ui.navigation.LocalNavigationController
import io.github.kez_lab.stopwatch_game.ui.navigation.Screen
import io.github.kez_lab.stopwatch_game.viewmodel.AppViewModel

/**
 * 플레이어 등록 화면
 */
@Composable
fun PlayerRegistrationScreen() {
    val navigationController = LocalNavigationController.current
    val appViewModel: AppViewModel = viewModel()
    
    // 플레이어 목록
    val players = remember { mutableStateListOf<Player>() }
    
    // 새 플레이어 입력 상태
    var newPlayerName by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 뒤로가기 버튼
                IconButton(onClick = { navigationController.goBack() }) {
                    Icon(
                        imageVector = FeatherIcons.ArrowLeft,
                        contentDescription = "뒤로 가기"
                    )
                }
                
                Text(
                    text = "플레이어 등록",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                
                // 다음 단계 버튼 (플레이어가 2명 이상일 때만 활성화)
                IconButton(
                    onClick = { 
                        appViewModel.registerPlayers(players)
                        navigationController.navigateTo(Screen.GameSelection)
                    },
                    enabled = players.size >= 2
                ) {
                    Icon(
                        imageVector = FeatherIcons.ArrowRight,
                        contentDescription = "다음",
                        tint = if (players.size >= 2) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 플레이어 등록 안내 문구
            Text(
                text = "게임에 참여할 플레이어를 등록해주세요 (2~6명)",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // 플레이어 입력 필드
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newPlayerName,
                    onValueChange = { 
                        newPlayerName = it
                        isError = false
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("플레이어 이름") },
                    placeholder = { Text("이름을 입력하세요") },
                    singleLine = true,
                    isError = isError,
                    supportingText = if (isError) {
                        { Text("이름을 입력해주세요") }
                    } else null,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { addPlayer(newPlayerName, players) { newPlayerName = ""; isError = it } }
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = FeatherIcons.UserPlus,
                            contentDescription = null
                        )
                    }
                )
                
                Spacer(modifier = Modifier.size(8.dp))
                
                // 추가 버튼
                Button(
                    onClick = { addPlayer(newPlayerName, players) { newPlayerName = ""; isError = it } },
                    enabled = players.size < 6
                ) {
                    Icon(
                        imageVector = FeatherIcons.Plus,
                        contentDescription = "플레이어 추가"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 등록된 플레이어 목록
            AnimatedVisibility(
                visible = players.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(players) { player ->
                        PlayerItem(
                            player = player,
                            onRemove = { players.remove(player) }
                        )
                    }
                }
            }
            
            // 플레이어가 없을 때 안내
            AnimatedVisibility(
                visible = players.isEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
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
            
            // 하단 안내
            AnimatedVisibility(visible = players.size >= 2) {
                Text(
                    text = "다음 버튼을 눌러 게임을 선택해주세요",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * 플레이어 추가 함수
 */
private fun addPlayer(
    name: String,
    players: MutableList<Player>,
    onResult: (Boolean) -> Unit
) {
    if (name.isBlank()) {
        onResult(true) // 에러 표시
        return
    }
    
    if (players.size >= 6) {
        return
    }
    
    players.add(Player(name = name))
    onResult(false) // 에러 초기화
}

/**
 * 플레이어 아이템 컴포넌트
 */
@Composable
private fun PlayerItem(
    player: Player,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
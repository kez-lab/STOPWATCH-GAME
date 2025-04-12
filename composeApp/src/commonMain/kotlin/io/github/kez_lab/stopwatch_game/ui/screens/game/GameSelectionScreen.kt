package io.github.kez_lab.stopwatch_game.ui.screens.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Shuffle
import io.github.kez_lab.stopwatch_game.model.GameRepository
import io.github.kez_lab.stopwatch_game.ui.components.GameCard
import io.github.kez_lab.stopwatch_game.ui.navigation.Routes
import io.github.kez_lab.stopwatch_game.viewmodel.AppViewModel

/**
 * 게임 선택 화면
 */
@Composable
fun GameSelectionScreen(
    navController: NavHostController,
    appViewModel: AppViewModel
) {
    // 게임 목록
    val games = remember { GameRepository.games }
    
    // 랜덤 선택 버튼 상태
    val showRandomConfirm = remember { mutableStateOf(false) }
    
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
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = FeatherIcons.ArrowLeft,
                        contentDescription = "뒤로 가기"
                    )
                }
                
                Text(
                    text = "게임 선택",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                
                // 랜덤 선택 버튼
                IconButton(
                    onClick = { showRandomConfirm.value = true }
                ) {
                    Icon(
                        imageVector = FeatherIcons.Shuffle,
                        contentDescription = "랜덤 선택",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 안내 문구
            Text(
                text = "플레이할 미니게임을 선택해주세요",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // 랜덤 선택 확인 UI
            AnimatedVisibility(
                visible = showRandomConfirm.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Button(
                        onClick = {
                            val randomGame = games.random()
                            appViewModel.selectGame(randomGame.id)
                            navController.navigate(Routes.GamePlay(randomGame.id))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Icon(
                            imageVector = FeatherIcons.Shuffle,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        
                        Spacer(modifier = Modifier.size(8.dp))
                        
                        Text(
                            text = "랜덤으로 선택하기",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            // 게임 목록
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(games) { game ->
                    GameCard(
                        game = game,
                        onClick = {
                            appViewModel.selectGame(game.id)
                            navController.navigate(Routes.GamePlay(game.id))
                        }
                    )
                }
            }
        }
    }
} 
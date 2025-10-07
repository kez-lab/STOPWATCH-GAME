package io.github.kez_lab.stopwatch_game.ui.screens.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import compose.icons.FeatherIcons
import compose.icons.feathericons.Shuffle
import io.github.kez_lab.stopwatch_game.model.GameType
import io.github.kez_lab.stopwatch_game.ui.components.AppBar
import io.github.kez_lab.stopwatch_game.ui.components.AppBarActionItem
import io.github.kez_lab.stopwatch_game.ui.components.GameCard
import io.github.kez_lab.stopwatch_game.ui.navigation.Routes
import io.github.kez_lab.stopwatch_game.ui.viewmodel.LocalAppViewModel

/**
 * 게임 선택 화면
 */
@Composable
fun GameSelectionScreen(navController: NavHostController) {
    val appViewModel = LocalAppViewModel.current
    val games = remember { GameType.entries.toList() }
    val showRandomConfirm = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AppBar(
                title = "게임 선택",
                onBackClick = { navController.popBackStack() },
                actions = {
                    AppBarActionItem(
                        icon = FeatherIcons.Shuffle,
                        contentDescription = "랜덤 선택",
                        onClick = {
                            showRandomConfirm.value = !showRandomConfirm.value
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

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
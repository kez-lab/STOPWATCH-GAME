package io.github.kez_lab.stopwatch_game.ui.screens.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.github.kez_lab.stopwatch_game.ui.components.AppBar
import io.github.kez_lab.stopwatch_game.ui.components.NavigationType
import io.github.kez_lab.stopwatch_game.ui.navigation.Routes
import io.github.kez_lab.stopwatch_game.ui.screens.player.components.PlayerInputSection
import io.github.kez_lab.stopwatch_game.ui.screens.player.components.PlayerListSection
import io.github.kez_lab.stopwatch_game.ui.screens.player.components.ProceedButton
import io.github.kez_lab.stopwatch_game.ui.viewmodel.LocalAppViewModel
import io.github.kez_lab.stopwatch_game.viewmodel.PlayerRegistrationViewModel

@Composable
fun PlayerRegistrationScreen(navController: NavHostController) {
    val appViewModel = LocalAppViewModel.current
    val viewModel: PlayerRegistrationViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AppBar(
                title = "플레이어 등록",
                onBackClick = { navController.navigateUp() },
                navigationType = NavigationType.BACK
            )
        },
        bottomBar = {
            if (uiState.canProceed) {
                ProceedButton(
                    modifier = Modifier.padding(bottom = 16.dp),
                    onClick = {
                        appViewModel.registerPlayers(uiState.players)
                        navController.navigate(Routes.GameSelection)
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "🎮 게임에 참여할 친구를 등록해 주세요!",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            PlayerInputSection(
                name = uiState.currentPlayerName,
                onNameChange = viewModel::updatePlayerName,
                onAdd = { viewModel.addPlayer() },
                isError = uiState.hasError,
                errorMessage = uiState.inputError ?: ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            PlayerListSection(
                players = uiState.players,
                onRemove = viewModel::removePlayer
            )
        }
    }
}
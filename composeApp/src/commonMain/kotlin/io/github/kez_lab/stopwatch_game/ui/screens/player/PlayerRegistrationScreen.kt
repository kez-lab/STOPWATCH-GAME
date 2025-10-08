package io.github.kez_lab.stopwatch_game.ui.screens.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.github.kez_lab.stopwatch_game.model.Player
import io.github.kez_lab.stopwatch_game.theme.AppTheme
import io.github.kez_lab.stopwatch_game.ui.components.AppBar
import io.github.kez_lab.stopwatch_game.ui.components.NavigationType
import io.github.kez_lab.stopwatch_game.ui.navigation.Routes
import io.github.kez_lab.stopwatch_game.ui.screens.player.components.PlayerInputSection
import io.github.kez_lab.stopwatch_game.ui.screens.player.components.PlayerListSection
import io.github.kez_lab.stopwatch_game.ui.screens.player.components.ProceedButton
import io.github.kez_lab.stopwatch_game.ui.viewmodel.LocalAppViewModel
import io.github.kez_lab.stopwatch_game.viewmodel.PlayerRegistrationUiState
import io.github.kez_lab.stopwatch_game.viewmodel.PlayerRegistrationViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PlayerRegistrationScreen(navController: NavHostController) {
    val appViewModel = LocalAppViewModel.current
    val viewModel: PlayerRegistrationViewModel = viewModel {
        PlayerRegistrationViewModel()
    }

    val uiState by viewModel.uiState.collectAsState()

    PlayerRegistrationContent(
        uiState = uiState,
        onBackClick = { navController.navigateUp() },
        onProceedClick = {
            appViewModel.registerPlayers(uiState.players)
            navController.navigate(Routes.GameSelection)
        },
        onNameChanged = viewModel::updatePlayerName,
        onPlayerAdded = viewModel::addPlayer,
        onPlayerRemoved = viewModel::removePlayer
    )
}

@Composable
private fun PlayerRegistrationContent(
    uiState: PlayerRegistrationUiState,
    onBackClick: () -> Unit,
    onProceedClick: () -> Unit,
    onNameChanged: (String) -> Unit,
    onPlayerAdded: () -> Unit,
    onPlayerRemoved: (Player) -> Unit
) {
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            AppBar(
                title = "플레이어 등록",
                onBackClick = onBackClick,
                navigationType = NavigationType.BACK
            )
        },
        bottomBar = {
            if (uiState.canProceed) {
                ProceedButton(
                    modifier = Modifier.padding(bottom = 16.dp),
                    onClick = onProceedClick
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {
            PlayerInputSection(
                name = uiState.currentPlayerName,
                onNameChange = onNameChanged,
                onAdd = onPlayerAdded,
                isError = uiState.hasError,
                errorMessage = uiState.inputError.orEmpty()
            )

            Spacer(modifier = Modifier.height(12.dp))

            PlayerListSection(
                players = uiState.players,
                onRemove = onPlayerRemoved,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {

        PlayerRegistrationContent(
            uiState = PlayerRegistrationUiState(
                players = listOf(),
                currentPlayerName = "",
                inputError = null,
            ),
            onBackClick = {},
            onProceedClick = {},
            onNameChanged = {},
            onPlayerAdded = {},
            onPlayerRemoved = {}
        )
    }
}

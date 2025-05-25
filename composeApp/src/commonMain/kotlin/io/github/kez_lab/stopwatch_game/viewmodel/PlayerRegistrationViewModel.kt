package io.github.kez_lab.stopwatch_game.viewmodel

import androidx.lifecycle.ViewModel
import io.github.kez_lab.stopwatch_game.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * 플레이어 등록 화면 전용 ViewModel
 */
class PlayerRegistrationViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(PlayerRegistrationUiState())
    val uiState: StateFlow<PlayerRegistrationUiState> = _uiState.asStateFlow()
    
    fun updatePlayerName(name: String) {
        _uiState.update { currentState ->
            currentState.copy(
                currentPlayerName = name,
                inputError = null
            )
        }
    }
    
    fun addPlayer(): Boolean {
        val currentName = _uiState.value.currentPlayerName.trim()
        val currentPlayers = _uiState.value.players
        
        when (val result = validateAndCreatePlayer(currentName, currentPlayers)) {
            is PlayerValidationResult.Success -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        players = currentState.players + result.player,
                        currentPlayerName = "",
                        inputError = null
                    )
                }
                return true
            }
            is PlayerValidationResult.Error -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        inputError = result.message
                    )
                }
                return false
            }
        }
    }
    
    fun removePlayer(player: Player) {
        _uiState.update { currentState ->
            currentState.copy(
                players = currentState.players.filter { it != player }
            )
        }
    }
    
    private fun validateAndCreatePlayer(
        name: String,
        existingPlayers: List<Player>
    ): PlayerValidationResult {
        if (name.isEmpty()) {
            return PlayerValidationResult.Error("이름을 입력해주세요")
        }
        
        if (existingPlayers.any { it.name.equals(name, ignoreCase = true) }) {
            return PlayerValidationResult.Error("이미 등록된 이름입니다")
        }
        
        return PlayerValidationResult.Success(Player(name = name))
    }
}

/**
 * 플레이어 등록 UI 상태
 */
data class PlayerRegistrationUiState(
    val players: List<Player> = emptyList(),
    val currentPlayerName: String = "",
    val inputError: String? = null
) {
    val canProceed: Boolean
        get() = players.size >= 2
    
    val hasError: Boolean
        get() = inputError != null
}

/**
 * 플레이어 검증 결과
 */
sealed class PlayerValidationResult {
    data class Success(val player: Player) : PlayerValidationResult()
    data class Error(val message: String) : PlayerValidationResult()
}
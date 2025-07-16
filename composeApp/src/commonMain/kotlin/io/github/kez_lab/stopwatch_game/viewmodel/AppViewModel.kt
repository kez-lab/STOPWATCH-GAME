package io.github.kez_lab.stopwatch_game.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kez_lab.stopwatch_game.model.GameRepository
import io.github.kez_lab.stopwatch_game.model.GameResult
import io.github.kez_lab.stopwatch_game.model.GameType
import io.github.kez_lab.stopwatch_game.model.Player
import io.github.kez_lab.stopwatch_game.model.Punishment
import io.github.kez_lab.stopwatch_game.model.PunishmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 앱 전체 상태 뷰모델
 */
class AppViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    // 플레이어 등록
    fun registerPlayers(players: List<Player>) {
        _uiState.update { currentState ->
            currentState.copy(
                players = players.shuffled(),
                currentPlayerIndex = 0
            )
        }
    }

    // 게임 선택
    fun selectGame(gameId: String) {
        prepareNewGame()
        val game = GameRepository.getGameById(gameId)
        _uiState.update { currentState ->
            currentState.copy(
                selectedGame = game
            )
        }
    }

    // 다음 플레이어로 이동
    fun moveToNextPlayer(): Boolean {
        val currentIndex = _uiState.value.currentPlayerIndex
        val totalPlayers = _uiState.value.players.size

        if (currentIndex >= totalPlayers - 1) {
            return false // 모든 플레이어가 플레이 완료
        }

        _uiState.update { currentState ->
            currentState.copy(
                currentPlayerIndex = currentState.currentPlayerIndex + 1
            )
        }
        return true
    }

    // 게임 결과 저장
    fun saveGameResult(result: GameResult) {
        val currentPlayerIndex = _uiState.value.currentPlayerIndex
        val updatedPlayers = _uiState.value.players.toMutableList()

        // 현재 플레이어의 결과 저장
        updatedPlayers[currentPlayerIndex].gameResults.add(result)

        // 결과 목록에 추가
        val updatedResults = _uiState.value.currentGameResults.toMutableList()
        updatedResults.add(Pair(updatedPlayers[currentPlayerIndex], result))

        _uiState.update { currentState ->
            currentState.copy(
                players = updatedPlayers,
                currentGameResults = updatedResults
            )
        }
    }

    // 게임 결과 정렬 및 순위 계산
    fun calculateRanks() {
        viewModelScope.launch {
            val currentResults = _uiState.value.currentGameResults

            // MS_DIGIT 게임 타입에 대한 정렬 (ms 끝자리 숫자가 큰 순서로 정렬)
            val sortedResults = currentResults.sortedByDescending { (_, result) ->
                result.specialValue
            }

            // 순위 부여 및 승자 설정
            val rankedResults = sortedResults.mapIndexed { index, (player, result) ->
                val updatedResult = result.copy(
                    rank = index + 1,
                    isWinner = index == 0
                )

                // 플레이어의 결과 업데이트
                val playerIndex = _uiState.value.players.indexOf(player)
                if (playerIndex >= 0) {
                    val gameResultIndex =
                        _uiState.value.players[playerIndex].gameResults.indexOf(result)
                    if (gameResultIndex >= 0) {
                        _uiState.value.players[playerIndex].gameResults[gameResultIndex] =
                            updatedResult
                    }
                }

                Pair(player, updatedResult)
            }

            // 승자에게 점수 부여
            if (rankedResults.isNotEmpty()) {
                val winner = rankedResults.first().first
                val winnerIndex = _uiState.value.players.indexOf(winner)
                if (winnerIndex >= 0) {
                    val updatedPlayers = _uiState.value.players.toMutableList()
                    updatedPlayers[winnerIndex] = updatedPlayers[winnerIndex].copy(
                        score = updatedPlayers[winnerIndex].score + 1
                    )

                    _uiState.update { currentState ->
                        currentState.copy(
                            players = updatedPlayers,
                            rankedResults = rankedResults
                        )
                    }
                }
            }
        }
    }

    // 벌칙 선택
    fun selectRandomPunishment() {
        val punishment = PunishmentRepository.getRandomPunishment()
        _uiState.update { currentState ->
            currentState.copy(
                selectedPunishment = punishment
            )
        }
    }

    // 새 게임 시작 준비
    private fun prepareNewGame() {
        _uiState.update { currentState ->
            currentState.copy(
                currentGameResults = emptyList(),
                rankedResults = emptyList(),
                selectedGame = null,
                selectedPunishment = null,
                currentPlayerIndex = 0
            )
        }
    }

    // 현재 플레이어의 마지막 게임 결과 제거
    fun removeLastPlayerResult() {
        val currentPlayerIndex = _uiState.value.currentPlayerIndex
        val updatedPlayers = _uiState.value.players.toMutableList()

        // 현재 플레이어의 마지막 결과 제거
        if (currentPlayerIndex < updatedPlayers.size) {
            val currentPlayer = updatedPlayers[currentPlayerIndex]
            if (currentPlayer.gameResults.isNotEmpty()) {
                // 마지막 결과 복사본 만들기
                val updatedResults = currentPlayer.gameResults.toMutableList()
                // 마지막 결과 제거
                updatedResults.removeAt(updatedResults.size - 1)

                // 현재 플레이어 업데이트
                updatedPlayers[currentPlayerIndex] = currentPlayer.copy(
                    gameResults = updatedResults
                )
            }
        }

        // 현재 게임 결과 목록에서도 해당 결과 제거
        val updatedGameResults = _uiState.value.currentGameResults.toMutableList()
        if (updatedGameResults.isNotEmpty()) {
            // 마지막에 추가된 현재 플레이어의 결과를 제거
            // 여러 플레이어가 있을 경우 현재 플레이어의 결과만 제거
            val playerResults = updatedGameResults.filter { (player, _) ->
                updatedPlayers.indexOf(player) == currentPlayerIndex
            }

            if (playerResults.isNotEmpty()) {
                // 현재 플레이어의 마지막 결과 제거
                updatedGameResults.remove(playerResults.last())
            }
        }

        _uiState.update { currentState ->
            currentState.copy(
                players = updatedPlayers,
                currentGameResults = updatedGameResults,
                // 랭킹 결과도 초기화
                rankedResults = emptyList()
            )
        }
    }
}

/**
 * 플레이어 추가 결과 클래스
 */
sealed class PlayerAddResult {
    data object EmptyName : PlayerAddResult()
    data object DuplicateName : PlayerAddResult()
    data class Success(val player: Player) : PlayerAddResult()
}

/**
 * 앱 UI 상태 클래스
 */
data class AppUiState(
    val players: List<Player> = emptyList(),
    val currentPlayerIndex: Int = 0,
    val selectedGame: GameType? = null,
    val currentGameResults: List<Pair<Player, GameResult>> = emptyList(),
    val rankedResults: List<Pair<Player, GameResult>> = emptyList(),
    val selectedPunishment: Punishment? = null
) {
    val currentPlayer: Player?
        get() = if (players.isNotEmpty() && currentPlayerIndex < players.size) {
            players[currentPlayerIndex]
        } else null
}
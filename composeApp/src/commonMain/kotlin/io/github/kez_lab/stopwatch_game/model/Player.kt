package io.github.kez_lab.stopwatch_game.model

/**
 * 게임 참가자 모델
 */
data class Player(
    val id: String = generateRandomId(),
    val name: String,
    var score: Int = 0,
    var gameResults: MutableList<GameResult> = mutableListOf()
) {
    companion object {
        private fun generateRandomId(): String = 
            (1..8).map { ('a'..'z').random() }.joinToString("")
    }
}

/**
 * 게임 결과 모델
 */
data class GameResult(
    val gameId: String,
    val timeTaken: Long,
    val targetTime: Long,
    val formattedTime: String,
    val specialValue: Int = -1,
    val specialValue2: Int? = null, // 콩콩팥팥 게임의 두 번째 숫자를 위해 추가
    val rank: Int = 0,
    val isWinner: Boolean = false,
) 
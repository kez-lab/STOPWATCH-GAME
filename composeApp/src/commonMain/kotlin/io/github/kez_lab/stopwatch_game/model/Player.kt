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
    val timeTaken: Long, // 밀리초 단위
    val targetTime: Long = 0, // 목표 시간 (밀리초)
    val formattedTime: String, // 표시용 시간
    val rank: Int = 0, // 게임에서의 등수
    val isWinner: Boolean = false,
    val specialValue: Int = -1 // 게임 종류에 따른 특수값 (예: ms의 끝자리)
) 
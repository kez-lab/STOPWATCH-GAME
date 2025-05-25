package io.github.kez_lab.stopwatch_game.model

/**
 * 미니게임 정보 모델
 */
data class Game(
    val id: String,
    val name: String,
    val description: String,
    val iconName: String,
    val gameType: GameType,
    val difficultyLevel: Int = 1 // 1-5, 1이 가장 쉬움
)

/**
 * 게임 종류 열거형
 */
enum class GameType {
    MS_DIGIT // ms를 높여라 (끝자리 배틀)
}

/**
 * 미리 정의된 게임 목록
 */
object GameRepository {
    val games = listOf(
        Game(
            id = "ms_digit",
            name = "ms를 높여라",
            description = "원하는 시간에 스톱워치를 멈추고, 밀리초 끝자리 숫자만 비교합니다. 가장 큰 숫자가 승리!",
            iconName = "ic_casino",
            gameType = GameType.MS_DIGIT,
            difficultyLevel = 1
        )
    )

    fun getGameById(id: String): Game = games.find { it.id == id }
        ?: throw IllegalArgumentException("Unknown game id: $id")
} 
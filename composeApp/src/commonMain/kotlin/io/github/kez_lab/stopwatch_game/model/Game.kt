package io.github.kez_lab.stopwatch_game.model

/**
 * 미니게임 정보 모델
 */

/**
 * 게임 종류 열거형
 */
enum class GameType(
    val id: String,
    val label: String,
    val description: String,
    val iconName: String,
    val difficultyLevel: Int = 1
) {
    RandomMS(
        id = "random_ms",
        label = "ms를 높여라",
        description = "원하는 시간에 스톱워치를 멈추고, 밀리초 끝자리 숫자만 비교합니다. 가장 큰 숫자가 승리!",
        iconName = "ic_casino"
    ),
    CONG_PAT(
        id = "cong_pat",
        label = "콩콩팥팥",
        description = "스톱워치를 두 번 작동시켜 나온 소수점 값을 곱해 가장 낮은 수를 낸 사람이 벌칙을 받는 타이머 게임",
        iconName = "ic_kong_pat"
    )
}

/**
 * 미리 정의된 게임 목록
 */
object GameRepository {
    fun getGameById(id: String): GameType = GameType.entries.find { it.id == id }
        ?: throw IllegalArgumentException("Unknown game id: $id")
} 
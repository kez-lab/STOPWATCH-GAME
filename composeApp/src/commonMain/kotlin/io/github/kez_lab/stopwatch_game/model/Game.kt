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
    EXACT_STOP, // 정확히 멈춰! (목표 시간에 가깝게)
    SLOWEST_STOP, // 가장 느리게 멈춰라 (제한 시간 내에서)
    RANDOM_MATCH, // 랜덤 타임 맞히기
    LAST_PERSON, // 눈치 싸움 (가장 늦게 멈추기)
    MS_DIGIT // ms의 신 (끝자리 배틀)
}

/**
 * 미리 정의된 게임 목록
 */
object GameRepository {
    val games = listOf(
        Game(
            id = "exact_stop",
            name = "정확히 멈춰!",
            description = "5.00초에 정확히 멈추세요! 가장 가까운 사람이 승리합니다.",
            iconName = "ic_timer",
            gameType = GameType.EXACT_STOP
        ),
        Game(
            id = "slowest_stop",
            name = "가장 느리게 멈춰라",
            description = "10초 이내에서 가장 늦게 멈추는 사람이 승리합니다. 시간 초과 시 탈락!",
            iconName = "ic_slow",
            gameType = GameType.SLOWEST_STOP
        ),
        Game(
            id = "random_match",
            name = "랜덤 타임 맞히기",
            description = "게임 시작 전에 잠깐 표시되는 시간을 기억하고 정확히 맞춰보세요!",
            iconName = "ic_random",
            gameType = GameType.RANDOM_MATCH,
            difficultyLevel = 3
        ),
        Game(
            id = "last_person",
            name = "눈치 싸움",
            description = "모두가 동시에 시작한 뒤, 가장 오래 버티는 사람이 승리합니다. 시간 초과 시 탈락!",
            iconName = "ic_group",
            gameType = GameType.LAST_PERSON,
            difficultyLevel = 2
        ),
        Game(
            id = "ms_digit",
            name = "ms의 신",
            description = "원하는 시간에 스톱워치를 멈추고, 밀리초 끝자리 숫자만 비교합니다. 가장 큰 숫자가 승리!",
            iconName = "ic_casino",
            gameType = GameType.MS_DIGIT,
            difficultyLevel = 1
        )
    )
    
    fun getGameById(id: String): Game? = games.find { it.id == id }
} 
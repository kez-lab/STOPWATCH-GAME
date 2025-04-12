package io.github.kez_lab.stopwatch_game.model

/**
 * 벌칙 모델
 */
data class Punishment(
    val id: String,
    val name: String,
    val description: String,
    val level: PunishmentLevel,
    val isCustom: Boolean = false
)

/**
 * 벌칙 레벨 (강도)
 */
enum class PunishmentLevel {
    LIGHT, // 가벼운 장난
    MEDIUM, // 적당한 난이도
    HARD // 어려운 벌칙
}

/**
 * 기본 벌칙 목록
 */
object PunishmentRepository {
    private val punishments = mutableListOf(
        Punishment(
            id = "push_up",
            name = "푸시업 10회",
            description = "푸시업을 10회 해야 합니다.",
            level = PunishmentLevel.LIGHT
        ),
        Punishment(
            id = "aegyo",
            name = "애교 3단계",
            description = "귀여운 애교를 3가지 다른 방식으로 보여줘야 합니다.",
            level = PunishmentLevel.MEDIUM
        ),
        Punishment(
            id = "truth",
            name = "진실 게임",
            description = "승자가 물어보는 질문에 진실만을 대답해야 합니다.",
            level = PunishmentLevel.MEDIUM
        ),
        Punishment(
            id = "dare",
            name = "도전 과제",
            description = "승자가 제시하는 도전 과제를 수행해야 합니다.",
            level = PunishmentLevel.HARD
        ),
        Punishment(
            id = "sing",
            name = "노래 한 소절",
            description = "아무 노래나 한 소절을 불러야 합니다.",
            level = PunishmentLevel.LIGHT
        ),
        Punishment(
            id = "imitation",
            name = "유명인 모사",
            description = "유명인을 지정해서 목소리와 행동을 따라해야 합니다.",
            level = PunishmentLevel.MEDIUM
        ),
        Punishment(
            id = "drink",
            name = "원샷",
            description = "음료 한 잔을 단숨에 마셔야 합니다.",
            level = PunishmentLevel.LIGHT
        ),
        Punishment(
            id = "compliment",
            name = "칭찬 릴레이",
            description = "모든 참가자에게 진심 어린 칭찬을 해야 합니다.",
            level = PunishmentLevel.MEDIUM
        )
    )
    
    // 모든 벌칙 목록 조회
    fun getAllPunishments(): List<Punishment> = punishments.toList()
    
    // 레벨별 벌칙 필터링
    fun getPunishmentsByLevel(level: PunishmentLevel): List<Punishment> = 
        punishments.filter { it.level == level }
    
    // 커스텀 벌칙 추가
    fun addCustomPunishment(punishment: Punishment) {
        punishments.add(punishment)
    }
    
    // 랜덤 벌칙 선택
    fun getRandomPunishment(): Punishment = punishments.random()
} 
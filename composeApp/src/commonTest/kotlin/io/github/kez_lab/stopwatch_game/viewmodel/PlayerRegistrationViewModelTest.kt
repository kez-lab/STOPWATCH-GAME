package io.github.kez_lab.stopwatch_game.viewmodel

import io.github.kez_lab.stopwatch_game.model.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerRegistrationViewModelTest {
    
    private lateinit var viewModel: PlayerRegistrationViewModel
    private val testDispatcher = StandardTestDispatcher()
    
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PlayerRegistrationViewModel()
    }
    
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun initialState_hasCorrectDefaults() = runTest {
        // When
        val initialState = viewModel.uiState.value
        
        // Then
        assertTrue(initialState.players.isEmpty())
        assertEquals("", initialState.currentPlayerName)
        assertNull(initialState.inputError)
        assertFalse(initialState.canProceed)
        assertFalse(initialState.hasError)
    }
    
    @Test
    fun updatePlayerName_updatesNameAndClearsError() = runTest {
        // Given
        val playerName = "테스트 플레이어"
        
        // When
        viewModel.updatePlayerName(playerName)
        
        // Then
        assertEquals(playerName, viewModel.uiState.value.currentPlayerName)
        assertNull(viewModel.uiState.value.inputError)
    }
    
    @Test
    fun addPlayer_withValidName_addsPlayerAndClearsInput() = runTest {
        // Given
        val playerName = "플레이어1"
        viewModel.updatePlayerName(playerName)
        
        // When
        val result = viewModel.addPlayer()
        
        // Then
        assertTrue(result)
        assertEquals(1, viewModel.uiState.value.players.size)
        assertEquals(playerName, viewModel.uiState.value.players.first().name)
        assertEquals("", viewModel.uiState.value.currentPlayerName)
        assertNull(viewModel.uiState.value.inputError)
    }
    
    @Test
    fun addPlayer_withEmptyName_showsError() = runTest {
        // Given
        viewModel.updatePlayerName("")
        
        // When
        val result = viewModel.addPlayer()
        
        // Then
        assertFalse(result)
        assertTrue(viewModel.uiState.value.players.isEmpty())
        assertEquals("이름을 입력해주세요", viewModel.uiState.value.inputError)
        assertTrue(viewModel.uiState.value.hasError)
    }
    
    @Test
    fun addPlayer_withWhitespaceOnlyName_showsError() = runTest {
        // Given
        viewModel.updatePlayerName("   ")
        
        // When
        val result = viewModel.addPlayer()
        
        // Then
        assertFalse(result)
        assertTrue(viewModel.uiState.value.players.isEmpty())
        assertEquals("이름을 입력해주세요", viewModel.uiState.value.inputError)
    }
    
    @Test
    fun addPlayer_withDuplicateName_showsError() = runTest {
        // Given
        val playerName = "플레이어1"
        viewModel.updatePlayerName(playerName)
        viewModel.addPlayer()
        
        // When
        viewModel.updatePlayerName(playerName)
        val result = viewModel.addPlayer()
        
        // Then
        assertFalse(result)
        assertEquals(1, viewModel.uiState.value.players.size)
        assertEquals("이미 등록된 이름입니다", viewModel.uiState.value.inputError)
        assertTrue(viewModel.uiState.value.hasError)
    }
    
    @Test
    fun addPlayer_withDuplicateNameIgnoreCase_showsError() = runTest {
        // Given
        viewModel.updatePlayerName("플레이어1")
        viewModel.addPlayer()
        
        // When
        viewModel.updatePlayerName("플레이어1")
        val result = viewModel.addPlayer()
        
        // Then
        assertFalse(result)
        assertEquals(1, viewModel.uiState.value.players.size)
        assertEquals("이미 등록된 이름입니다", viewModel.uiState.value.inputError)
    }
    
    @Test
    fun removePlayer_removesPlayerFromList() = runTest {
        // Given
        val player1 = Player(name = "플레이어1")
        val player2 = Player(name = "플레이어2")
        viewModel.updatePlayerName(player1.name)
        viewModel.addPlayer()
        viewModel.updatePlayerName(player2.name)
        viewModel.addPlayer()
        val player1InViewMode = viewModel.uiState.value.players.find { it.name == player1.name }!!
        // When
        viewModel.removePlayer(player1InViewMode)
        
        // Then
        assertEquals(1, viewModel.uiState.value.players.size)
        assertEquals(player2.name, viewModel.uiState.value.players.first().name)
    }
    
    @Test
    fun canProceed_withLessThanTwoPlayers_returnsFalse() = runTest {
        // Given
        viewModel.updatePlayerName("플레이어1")
        viewModel.addPlayer()
        
        // When & Then
        assertFalse(viewModel.uiState.value.canProceed)
    }
    
    @Test
    fun canProceed_withTwoOrMorePlayers_returnsTrue() = runTest {
        // Given
        viewModel.updatePlayerName("플레이어1")
        viewModel.addPlayer()
        viewModel.updatePlayerName("플레이어2")
        viewModel.addPlayer()
        
        // When & Then
        assertTrue(viewModel.uiState.value.canProceed)
    }
    
    @Test
    fun canProceed_withThreePlayers_returnsTrue() = runTest {
        // Given
        viewModel.updatePlayerName("플레이어1")
        viewModel.addPlayer()
        viewModel.updatePlayerName("플레이어2")
        viewModel.addPlayer()
        viewModel.updatePlayerName("플레이어3")
        viewModel.addPlayer()
        
        // When & Then
        assertTrue(viewModel.uiState.value.canProceed)
        assertEquals(3, viewModel.uiState.value.players.size)
    }
    
    @Test
    fun addMultiplePlayers_maintainsCorrectOrder() = runTest {
        // Given
        val names = listOf("플레이어1", "플레이어2", "플레이어3")
        
        // When
        names.forEach { name ->
            viewModel.updatePlayerName(name)
            viewModel.addPlayer()
        }
        
        // Then
        val playerNames = viewModel.uiState.value.players.map { it.name }
        assertEquals(names, playerNames)
    }
}
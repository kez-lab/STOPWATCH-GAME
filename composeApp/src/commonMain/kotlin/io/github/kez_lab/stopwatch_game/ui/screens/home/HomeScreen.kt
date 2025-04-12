package io.github.kez_lab.stopwatch_game.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FeatherIcons
import compose.icons.feathericons.PlayCircle
import io.github.kez_lab.stopwatch_game.ui.navigation.LocalNavigationController
import io.github.kez_lab.stopwatch_game.ui.navigation.Screen
import kotlinx.coroutines.delay

/**
 * 앱 홈 화면
 */
@Composable
fun HomeScreen() {
    val navigationController = LocalNavigationController.current
    
    // 애니메이션을 위한 상태
    var showTitle by remember { mutableStateOf(false) }
    var showSubtitle by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }
    
    // 순차적 애니메이션 실행
    LaunchedEffect(Unit) {
        delay(200)
        showTitle = true
        delay(500)
        showSubtitle = true
        delay(500)
        showButton = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 타이틀 애니메이션
            AnimatedVisibility(
                visible = showTitle,
                enter = fadeIn(animationSpec = tween(800)) + 
                        slideInVertically(animationSpec = tween(800)) { it / 2 }
            ) {
                Text(
                    text = "TimeBattle",
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 서브타이틀 애니메이션
            AnimatedVisibility(
                visible = showSubtitle,
                enter = fadeIn(animationSpec = tween(800))
            ) {
                Text(
                    text = "친구들과 함께하는 스톱워치 미니게임",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(80.dp))
            
            // 게임 시작 버튼 애니메이션
            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn(animationSpec = tween(1000)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                Button(
                    onClick = { navigationController.navigateTo(Screen.PlayerRegistration) },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = FeatherIcons.PlayCircle,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    
                    Spacer(modifier = Modifier.size(8.dp))
                    
                    Text(
                        text = "게임 시작하기",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
} 
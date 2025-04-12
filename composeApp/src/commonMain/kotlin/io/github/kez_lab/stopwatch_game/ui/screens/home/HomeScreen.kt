package io.github.kez_lab.stopwatch_game.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import compose.icons.FeatherIcons
import compose.icons.feathericons.PlayCircle
import io.github.kez_lab.stopwatch_game.ui.navigation.Routes
import kotlinx.coroutines.delay
import io.github.kez_lab.stopwatch_game.viewmodel.AppViewModel

enum class HomeAnimationStage { NONE, TITLE, SUBTITLE, BUTTON }

@Composable
fun HomeScreen(
    navController: NavHostController,
    appViewModel: AppViewModel
) {
    val uiState by appViewModel.uiState.collectAsState()
    var stage by remember { mutableStateOf(HomeAnimationStage.NONE) }

    LaunchedEffect(Unit) {
        stage = HomeAnimationStage.TITLE
        delay(400)
        stage = HomeAnimationStage.SUBTITLE
        delay(400)
        stage = HomeAnimationStage.BUTTON
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
            Title(visible = stage >= HomeAnimationStage.TITLE)
            Spacer(modifier = Modifier.height(16.dp))
            Subtitle(visible = stage >= HomeAnimationStage.SUBTITLE)
            Spacer(modifier = Modifier.height(80.dp))
            StartButton(
                visible = stage >= HomeAnimationStage.BUTTON,
                onClick = { navController.navigate(Routes.PlayerRegistration) }
            )
        }
    }
}

@Composable
private fun Title(visible: Boolean) {
    val transition = updateTransition(targetState = visible, label = "TitleTransition")
    val alpha by transition.animateFloat({ tween(400) }, label = "alpha") { if (it) 1f else 0f }
    val offsetY by transition.animateDp({ tween(400) }, label = "offsetY") { if (it) 0.dp else 30.dp }

    Text(
        text = "TimeBattle",
        fontSize = 60.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .alpha(alpha)
            .offset(y = offsetY)
    )
}

@Composable
private fun Subtitle(visible: Boolean) {
    val transition = updateTransition(targetState = visible, label = "SubtitleTransition")
    val alpha by transition.animateFloat({ tween(400) }, label = "alpha") { if (it) 1f else 0f }

    Text(
        text = "친구들과 함께하는 스톱워치 미니게임",
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .alpha(alpha)
    )
}

@Composable
private fun StartButton(visible: Boolean, onClick: () -> Unit) {
    val transition = updateTransition(targetState = visible, label = "ButtonTransition")
    val alpha by transition.animateFloat({ tween(1000) }, label = "alpha") { if (it) 1f else 0f }

    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(16.dp)
            .alpha(alpha),
        enabled = visible
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

package com.andb.apps.aspen

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Box
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andb.apps.aspen.android.BuildConfig
import com.andb.apps.aspen.models.Screen
import com.andb.apps.aspen.state.UserAction
import com.andb.apps.aspen.ui.assignment.AssignmentScreen
import com.andb.apps.aspen.ui.common.BetterCrossfade
import com.andb.apps.aspen.ui.home.HomeScreen
import com.andb.apps.aspen.ui.login.LoginScreen
import com.andb.apps.aspen.ui.subject.SubjectScreen
import com.andb.apps.aspen.ui.test.TestScreen
import com.andb.apps.aspen.util.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    private val viewModel: MainActivityViewModel by viewModel()
    private val settings: AndroidSettings by inject()
    private val handler = ActionHandler {
        viewModel.screens.handleAction(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingsProvider(settings = settings) {
                AppTheme {
                    val currentScreen = viewModel.screens.stack.collectAsState(initial = listOf())
                    StatusBar(currentScreen = currentScreen.value.lastOrNull())
                    NavigationBar(currentScreen = currentScreen.value.lastOrNull())
                    AppContent(currentScreen.value, handler)
                }
            }
        }
    }

    override fun onBackPressed() {
        val goBack = handler.handle(UserAction.Back)
        Log.d("onBackPressed", "goBack = $goBack")
        if (!goBack) {
            super.onBackPressed()
        }
    }
}

@Composable
fun AppContent(stack: List<Screen>, actionHandler: ActionHandler) {

    Surface(color = MaterialTheme.colors.background) {
        Stack(Modifier.fillMaxSize()) {
            if (stack.isNotEmpty()){
                //BetterCrossfade(stack.last(), animateIf = { old, new -> old != new }) { screen ->
                    when (val screen = stack.last()) {
                        is Screen.Login -> LoginScreen(actionHandler)
                        is Screen.Home -> {
                            val expanded = remember { mutableStateOf(false) }
                            HomeScreen(screen.subjects, screen.recentState, screen.terms, screen.selectedTerm, screen.tab, expanded.value, { expanded.value = !expanded.value }, actionHandler)
                        }
                        is Screen.Subject -> SubjectScreen(screen.subject, screen.term, actionHandler)
                        is Screen.Assignment -> AssignmentScreen(screen.assignment, actionHandler)
                        is Screen.Test -> TestScreen()
                    }
                //}
            }

            if (BuildConfig.DEBUG) {
                VersionRibbon(Modifier.gravity(Alignment.TopEnd))
            }
        }
    }

}

@Composable
private fun SettingsProvider(settings: AndroidSettings, content: @Composable() () -> Unit){
    Providers(SettingsAmbient provides settings, children = content)
}



@Composable
fun AppTheme(content: @Composable() () -> Unit) {
    val darkMode = SettingsAmbient.current.darkModeFlow.collectAsState()
    val fontSize = SettingsAmbient.current.fontSizeFlow.collectAsState()

    val colors = when (darkMode.value.isDark()) {
        false -> lightColors(
            primary = Color(0xFF388E3C),
            primaryVariant = Color(0xFF1B5E20),
            onSecondary = Color.Black.copy(alpha = .6f)
        )
        true -> darkColors(
            primary = Color(0xFF388E3C),
            primaryVariant = Color(0xFF1B5E20),
            onPrimary = Color.White,
            onSecondary = Color.White.copy(alpha = .7f)
        )
    }

    val typography = MaterialTheme.typography.copy(
        body1 = MaterialTheme.typography.body1.copy(color = colors.onSecondary, fontSize = fontSize.value.sp),
        subtitle1 = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Medium, fontSize = fontSize.value.sp),
        h3 = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Bold),
        h4 = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Medium, fontSize = 32.sp),
        h5 = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Medium)
    )

    MaterialTheme(
        colors = colors,
        typography = typography,
        content = content
    )
}

@Composable
fun VersionRibbon(modifier: Modifier = Modifier) {
    Stack(
        modifier = modifier.size(108.dp)
            .drawLayer(rotationZ = 45f, translationX = 75f, translationY = -75f)
    ) {
        Box(
            modifier = Modifier.height(24.dp).fillMaxWidth().gravity(Alignment.Center),
            backgroundColor = MaterialTheme.colors.primary
        )
        Text(
            text = BuildConfig.VERSION_NAME,
            modifier = Modifier.gravity(Alignment.Center),
            style = TextStyle(fontSize = 10.sp),
            color = MaterialTheme.colors.onPrimary
        )
    }
}

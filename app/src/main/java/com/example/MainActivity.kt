package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.DigitalBibScreen
import com.example.ui.screens.LiveTrackingScreen
import com.example.ui.screens.RaceHubScreen
import com.example.ui.screens.RegistrationScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MarathonViewModel

enum class MarathonNavTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    REGISTRATION("Register", Icons.Filled.Badge, Icons.Outlined.Badge),
    DIGITAL_BIB("Race Bib", Icons.Filled.QrCode, Icons.Outlined.QrCode),
    LIVE_TRACKING("Live Tracking", Icons.Filled.DirectionsRun, Icons.Outlined.DirectionsRun),
    RACE_HUB("Course Hub", Icons.Filled.Map, Icons.Outlined.Map)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                BengaluruMarathonApp()
            }
        }
    }
}

@Composable
fun BengaluruMarathonApp(
    viewModel: MarathonViewModel = viewModel()
) {
    var currentTab by remember { mutableStateOf(MarathonNavTab.REGISTRATION) }
    val isPresentationMode by viewModel.isPresentationMode.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (!isPresentationMode) {
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = Color(0xFF0F172A),
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("marathon_bottom_nav")
                ) {
                    MarathonNavTab.values().forEach { tab ->
                        val isSelected = currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentTab = tab },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = tab.title
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                                    fontSize = 11.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF0F172A),
                                selectedTextColor = Color(0xFF0F172A),
                                indicatorColor = Color(0xFFE2E8F0),
                                unselectedIconColor = Color(0xFF64748B),
                                unselectedTextColor = Color(0xFF64748B)
                            ),
                            modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC))
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { targetTab ->
                when (targetTab) {
                    MarathonNavTab.REGISTRATION -> {
                        RegistrationScreen(
                            viewModel = viewModel,
                            onNavigateToBib = { currentTab = MarathonNavTab.DIGITAL_BIB },
                            onNavigateToTracking = { currentTab = MarathonNavTab.LIVE_TRACKING }
                        )
                    }
                    MarathonNavTab.DIGITAL_BIB -> {
                        DigitalBibScreen(
                            viewModel = viewModel,
                            onNavigateToRegistration = { currentTab = MarathonNavTab.REGISTRATION },
                            onNavigateToTracking = { currentTab = MarathonNavTab.LIVE_TRACKING }
                        )
                    }
                    MarathonNavTab.LIVE_TRACKING -> {
                        LiveTrackingScreen(
                            viewModel = viewModel,
                            onNavigateToBib = { currentTab = MarathonNavTab.DIGITAL_BIB }
                        )
                    }
                    MarathonNavTab.RACE_HUB -> {
                        RaceHubScreen()
                    }
                }
            }
        }
    }
}

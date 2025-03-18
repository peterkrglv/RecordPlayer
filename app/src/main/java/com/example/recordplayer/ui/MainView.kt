package com.example.recordplayer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.recordplayer.ui.api_songs.ApiSongsView
import com.example.recordplayer.ui.icons.ThemeIcon
import com.example.recordplayer.ui.local_songs.LocalSongsView
import com.example.recordplayer.ui.player.PlayerView
import com.example.recordplayer.ui.theme.RecordPlayerTheme

@Composable
fun MainView() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isThemeDark = remember {
        mutableStateOf(false)
    }
    RecordPlayerTheme(
        darkTheme = isThemeDark.value
    ) {


        Scaffold(bottomBar = {
            BottomNavigationBar(navController)
        },
            floatingActionButton = {
                ThemeFab(
                    navController = navController,
                    onClick = {
                        isThemeDark.value = !isThemeDark.value
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = NavigationItem.Player.route
                    ) {
                        composable(NavigationItem.ApiSongs.route) {
                            ApiSongsView()
                        }
                        composable(NavigationItem.Player.route) {
                            PlayerView()
                        }
                        composable(NavigationItem.LocalSongs.route) {
                            LocalSongsView()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeFab(
    navController: NavHostController,
    onClick: () -> Unit
) {
    val currentRoute = navController.currentDestination?.route
    if (currentRoute != NavigationItem.Player.route) {
        return
    }

    Row(
        modifier = Modifier
    ) {
        FloatingActionButton(
            onClick = onClick,
        ) {
            Icon(
                imageVector = ThemeIcon,
                contentDescription = "Theme Toggle"
            )
        }
    }


}





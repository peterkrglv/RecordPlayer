package com.example.recordplayer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.recordplayer.ui.api_songs.ApiSongsView
import com.example.recordplayer.ui.local_songs.LocalSongsView
import com.example.recordplayer.ui.player.PlayerView

@Composable
fun MainView() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(bottomBar = {
        BottomNavigationBar(navController)
    }) { padding ->
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





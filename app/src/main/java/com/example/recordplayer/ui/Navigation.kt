package com.example.recordplayer.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.recordplayer.ui.icons.Internet
import com.example.recordplayer.ui.icons.Library
import com.example.recordplayer.ui.icons.RecordPlayer

sealed class NavigationItem(val route: String, val icon: ImageVector) {
    data object ApiSongs : NavigationItem("api_songs", Internet)
    data object Player : NavigationItem("player", RecordPlayer)
    data object LocalSongs : NavigationItem("local_songs", Library)
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val navItems = listOf(
        NavigationItem.ApiSongs,
        NavigationItem.Player,
        NavigationItem.LocalSongs
    )

    NavigationBar {
        navItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                icon = {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = item.icon,
                        contentDescription = item.route
                    )
                },
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
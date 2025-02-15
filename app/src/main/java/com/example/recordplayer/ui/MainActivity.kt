package com.example.recordplayer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.recordplayer.ui.player.PlayerView
import com.example.recordplayer.ui.theme.RecordPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecordPlayerTheme {
                PlayerView()
            }
        }
    }
}

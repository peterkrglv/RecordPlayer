package com.example.recordplayer.domain

import androidx.media3.exoplayer.ExoPlayer
import com.example.recordplayer.ui.player.PlayerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<ExoPlayer> { ExoPlayer.Builder(get()).build() }
    viewModel<PlayerViewModel> { PlayerViewModel(get()) }
}
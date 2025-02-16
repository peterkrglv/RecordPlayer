package com.example.recordplayer.domain

import androidx.media3.exoplayer.ExoPlayer
import com.example.recordplayer.data.LocalSongRepositoryImpl
import com.example.recordplayer.ui.local_songs.LocalSongsViewModel
import com.example.recordplayer.ui.player.PlayerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single<LocalSongRepository> { LocalSongRepositoryImpl(
        context = androidContext()
    ) }
}

val domainModule = module {
    factory<GetLocalSongsUseCase> { GetLocalSongsUseCase(get()) }
}

val appModule = module {
    single<ExoPlayer> { ExoPlayer.Builder(get()).build() }
    single<PlayerViewModel> { PlayerViewModel(get()) }
    viewModel<LocalSongsViewModel> {LocalSongsViewModel(get())}
}
package com.example.recordplayer.domain

import androidx.media3.exoplayer.ExoPlayer
import com.example.recordplayer.data.ApiSongRepositoryImpl
import com.example.recordplayer.data.LocalSongRepositoryImpl
import com.example.recordplayer.ui.NotificationService
import com.example.recordplayer.ui.api_songs.ApiSongsViewModel
import com.example.recordplayer.ui.local_songs.LocalSongsViewModel
import com.example.recordplayer.ui.player.PlayerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single<LocalSongRepository> {
        LocalSongRepositoryImpl(
            context = androidContext()
        )
    }
    single<ApiSongRepository> {
        ApiSongRepositoryImpl()
    }
}

val domainModule = module {
    factory<GetLocalSongsUseCase> { GetLocalSongsUseCase(get()) }
    factory<GetApiSongsUseCase> { GetApiSongsUseCase(get()) }
    factory<SearchApiSongsUseCase> { SearchApiSongsUseCase(get()) }
}

val appModule = module {
    single<ExoPlayer> { ExoPlayer.Builder(get()).build() }
    viewModel<LocalSongsViewModel> { LocalSongsViewModel(get()) }
    viewModel<ApiSongsViewModel> { ApiSongsViewModel(get(), get()) }
    single<PlayerViewModel> { PlayerViewModel(get(), get(), get()) }
    single { NotificationService() }
}
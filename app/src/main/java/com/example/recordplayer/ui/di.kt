package com.example.recordplayer.ui

import androidx.media3.exoplayer.ExoPlayer
import com.example.recordplayer.data.ApiSongRepositoryImpl
import com.example.recordplayer.data.LocalSongRepositoryImpl
import com.example.recordplayer.data.SharedPreferencesRepositoryImpl
import com.example.recordplayer.domain.ApiSongs.ApiSongRepository
import com.example.recordplayer.domain.ApiSongs.GetApiSongsUseCase
import com.example.recordplayer.domain.ApiSongs.SearchApiSongsUseCase
import com.example.recordplayer.domain.LocalSongs.GetLocalSongsUseCase
import com.example.recordplayer.domain.LocalSongs.LocalSongRepository
import com.example.recordplayer.domain.sharedPrefs.ClearSearchHistoryUseCase
import com.example.recordplayer.domain.sharedPrefs.GetSearchHistoryUseCase
import com.example.recordplayer.domain.sharedPrefs.SaveSearchQueryUseCase
import com.example.recordplayer.domain.sharedPrefs.SharedPreferencesRepository
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
    single<ApiSongRepository> { ApiSongRepositoryImpl() }
    single<SharedPreferencesRepository> { SharedPreferencesRepositoryImpl(get()) }
}

val domainModule = module {
    factory<GetLocalSongsUseCase> { GetLocalSongsUseCase(get()) }
    factory<GetApiSongsUseCase> { GetApiSongsUseCase(get()) }
    factory<SearchApiSongsUseCase> { SearchApiSongsUseCase(get()) }
    factory<ClearSearchHistoryUseCase> { ClearSearchHistoryUseCase(get()) }
    factory<SaveSearchQueryUseCase> { SaveSearchQueryUseCase(get()) }
    factory<SaveSearchQueryUseCase> { SaveSearchQueryUseCase(get()) }
    factory<GetSearchHistoryUseCase> { GetSearchHistoryUseCase(get()) }
}

val appModule = module {
    single<ExoPlayer> { ExoPlayer.Builder(get()).build() }
    viewModel<LocalSongsViewModel> { LocalSongsViewModel(get()) }
    viewModel<ApiSongsViewModel> { ApiSongsViewModel(get(), get(), get(), get(), get()) }
    single<PlayerViewModel> { PlayerViewModel(get(), get(), get()) }
    single { NotificationService() }
}
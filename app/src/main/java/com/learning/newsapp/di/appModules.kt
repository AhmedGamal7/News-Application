package com.learning.newsapp.di

import android.app.Application
import androidx.room.Room
import com.learning.newsapp.api.NewsApi
import com.learning.newsapp.repositories.BreakingNewsRepository
import com.learning.newsapp.repositories.SavedNewsRepository
import com.learning.newsapp.repositories.SearchingNewsRepository
import com.learning.newsapp.room.ArticleDataBase
import com.learning.newsapp.ui.fragments.breakingnews.BreakingNewsViewModel
import com.learning.newsapp.ui.fragments.savednews.SavedNewsViewModel
import com.learning.newsapp.ui.fragments.searchingnews.SearchingNewsViewModel
import com.learning.newsapp.utils.Constant.Companion.BASE_URL
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { provideRetrofit() }

    single { provideRestaurantApi(get()) }

    single { provideDataBase(get()) }

    single { BreakingNewsRepository(get()) }

    single { SearchingNewsRepository(get()) }

    single { SavedNewsRepository(get()) }

    viewModel { BreakingNewsViewModel(get()) }

    viewModel { SearchingNewsViewModel(get()) }

    viewModel { SavedNewsViewModel(get()) }
}

fun provideDataBase(app: Application): ArticleDataBase =
    Room.databaseBuilder(app, ArticleDataBase::class.java, "ArticleDataBse")
        .build()

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideRestaurantApi(retrofit: Retrofit): NewsApi =
    retrofit.create(NewsApi::class.java)
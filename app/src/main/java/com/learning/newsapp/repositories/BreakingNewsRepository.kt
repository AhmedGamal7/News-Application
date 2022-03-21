package com.learning.newsapp.repositories

import com.learning.newsapp.api.NewsApi
import com.learning.newsapp.room.ArticleDataBase

class BreakingNewsRepository(
    private val api: NewsApi,
) {


    suspend fun getBreakingNews(page: Int) = api.getBreakingNews(page = page)

}
package com.learning.newsapp.repositories

import com.learning.newsapp.api.NewsApi

class SearchingNewsRepository(
    private val api: NewsApi,

    ) {

    suspend fun getSearchingNews(searchQuery: String, page: Int) =
        api.getSearchNews(searchQuery = searchQuery, page = page)

}
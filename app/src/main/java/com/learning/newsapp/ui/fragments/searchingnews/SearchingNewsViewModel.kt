package com.learning.newsapp.ui.fragments.searchingnews

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.newsapp.models.NewsResponse
import com.learning.newsapp.repositories.SearchingNewsRepository
import com.learning.newsapp.utils.CheckConnection
import com.learning.newsapp.utils.Resource
import kotlinx.coroutines.launch
import java.io.IOException

class SearchingNewsViewModel(
    private val repository: SearchingNewsRepository
) : ViewModel() {

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    private val page: Int = 1

    fun getSearchingNews(searchQuery: String) = viewModelScope.launch {
        safeSearchingNewsCall(searchQuery)
    }

    private suspend fun safeSearchingNewsCall(searchQuery: String) {
        searchNews.postValue(Resource.Loading())
        val con = CheckConnection()
        if (con.hasInternetConnection()) {
            try {
                val response = repository.getSearchingNews(searchQuery, page)
                if (response.isSuccessful) {
                    response.body()?.let { newsResponse ->
                        searchNews.postValue(Resource.Success(newsResponse))
                    }
                } else {
                    searchNews.postValue(Resource.Error("Sorry,connection with server was drop"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                    else -> searchNews.postValue(Resource.Error(t.localizedMessage!!))
                }
            }
        } else {
            searchNews.postValue(Resource.Error("No internet connection"))
        }
    }
}
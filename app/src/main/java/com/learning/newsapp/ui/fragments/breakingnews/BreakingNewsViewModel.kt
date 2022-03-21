package com.learning.newsapp.ui.fragments.breakingnews

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.newsapp.models.NewsResponse
import com.learning.newsapp.repositories.BreakingNewsRepository
import com.learning.newsapp.utils.CheckConnection
import com.learning.newsapp.utils.Constant.Companion.MAX_PAGE
import com.learning.newsapp.utils.Resource
import kotlinx.coroutines.launch
import java.io.IOException

class BreakingNewsViewModel(
    private val repository: BreakingNewsRepository
) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    private var page: Int = 1

    private var isload = false

    init {
        getBreakingNews()
    }

    fun getBreakingNews() = viewModelScope.launch {
        safeBreakingNewsCall()
    }

    fun loadMore() {
        if (page < MAX_PAGE) {
            isload = true
            page++
            getBreakingNews()
        }
    }

    private suspend fun safeBreakingNewsCall() {
        breakingNews.postValue(Resource.Loading())
        val con = CheckConnection()
        if (con.hasInternetConnection()) {
            try {
                val response = repository.getBreakingNews(page)
                if (response.isSuccessful) {
                    response.body()?.let { newsResponse ->
                        if (isload) {
                            val oldArticles = breakingNews.value?.data?.articles
                            if (oldArticles != null) {
                                newsResponse.articles.addAll(0, oldArticles)
                            }
                        }
                        breakingNews.postValue(Resource.Success(newsResponse))
                    }
                } else {
                    breakingNews.postValue(Resource.Error("Sorry,connection with server was drop"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                    else -> breakingNews.postValue(Resource.Error(t.localizedMessage!!))
                }
            }
        } else {
            breakingNews.postValue(Resource.Error("No internet connection"))
        }
    }
}
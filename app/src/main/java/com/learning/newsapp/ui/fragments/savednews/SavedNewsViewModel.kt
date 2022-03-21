package com.learning.newsapp.ui.fragments.savednews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.learning.newsapp.models.Article
import com.learning.newsapp.repositories.SavedNewsRepository
import kotlinx.coroutines.launch

class SavedNewsViewModel(
    private val repository: SavedNewsRepository
) : ViewModel() {

    val articles = repository.getAllArticles().asLiveData()

    fun insertArticle(article: Article) = viewModelScope.launch {
        repository.insert(article)
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            repository.delete(article)
        }
    }
}
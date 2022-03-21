package com.learning.newsapp.repositories

import com.learning.newsapp.models.Article
import com.learning.newsapp.room.ArticleDataBase

class SavedNewsRepository(
    db: ArticleDataBase
) {

    private val dao = db.getArticleDao()

    suspend fun insert(article: Article) = dao.insertArticle(article)

    suspend fun delete(article: Article) = dao.deleteArticle(article)

    fun getAllArticles() = dao.getAllArticles()
}
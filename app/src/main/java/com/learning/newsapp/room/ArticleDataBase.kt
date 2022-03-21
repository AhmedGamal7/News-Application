package com.learning.newsapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.learning.newsapp.models.Article

@Database(entities = [Article::class], version = 1, exportSchema = false)
@TypeConverters(Convertors::class)
abstract class ArticleDataBase : RoomDatabase() {
    abstract fun getArticleDao(): ArticleDao
}
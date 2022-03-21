package com.learning.newsapp.room

import androidx.room.TypeConverter
import com.learning.newsapp.models.Source

class Convertors {
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }

//    @TypeConverter
//    fun fromSourceToString(source: Source): String {
//        return Gson().toJson(source)
//    }
//    @TypeConverter
//    fun fromStringToSource(string: String): Source {
//        val type = object : TypeToken<Source>() {}.type
//        return Gson().fromJson(string, type)
//    }
}
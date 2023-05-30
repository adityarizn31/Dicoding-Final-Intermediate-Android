package com.dicoding.sub1_appsstory.Retrofit

import android.content.Context
import com.dicoding.sub1_appsstory.Database.StoryDatabase
import com.dicoding.sub1_appsstory.Repository.StoryAppRepository
import com.dicoding.sub1_appsstory.Retrofit.ApiConfig

object ApiRetro {
    fun provideRepository(context: Context): StoryAppRepository {
        val database = StoryDatabase.getDatabasee(context)
        val apiService = ApiConfig.getApiService()
        return StoryAppRepository(database, apiService)
    }
}
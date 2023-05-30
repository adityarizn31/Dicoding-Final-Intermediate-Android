package com.dicoding.sub1_appsstory.Database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.sub1_appsstory.Data.ListStoryItem

@Dao
interface  StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun InsertStory(quote: List<ListStoryItem>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM story")
    suspend fun deleteAlls()
}
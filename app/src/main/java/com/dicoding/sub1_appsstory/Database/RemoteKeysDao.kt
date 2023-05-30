package com.dicoding.sub1_appsstory.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    // Digunakan untuk Insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun InsertAlls(remoteKey: List<RemoteKeys>)

    // Digunakan untuk menampilkan data
    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): RemoteKeys?

    // Digunakan untuk menghapus data
    @Query("DELETE FROM remote_keys")
    suspend fun deleteRemoteKeysId()
}
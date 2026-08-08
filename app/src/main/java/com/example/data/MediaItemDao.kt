package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaItemDao {
    @Query("SELECT * FROM media_portfolio_items ORDER BY isFeatured DESC, id DESC")
    fun getAllItems(): Flow<List<MediaItem>>

    @Query("SELECT * FROM media_portfolio_items WHERE category = :category ORDER BY id DESC")
    fun getItemsByCategory(category: String): Flow<List<MediaItem>>

    @Query("SELECT * FROM media_portfolio_items WHERE id = :id")
    suspend fun getItemById(id: Int): MediaItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: MediaItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MediaItem>)

    @Update
    suspend fun updateItem(item: MediaItem)

    @Delete
    suspend fun deleteItem(item: MediaItem)

    @Query("DELETE FROM media_portfolio_items WHERE id = :id")
    suspend fun deleteItemById(id: Int)

    @Query("DELETE FROM media_portfolio_items")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM media_portfolio_items")
    suspend fun getItemCount(): Int
}

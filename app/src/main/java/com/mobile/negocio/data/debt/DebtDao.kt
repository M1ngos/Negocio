package com.mobile.negocio.data.debt

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DebtDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Debt)

    @Update
    suspend fun update(item: Debt)

    @Delete
    suspend fun delete(item: Debt)

    @Query("SELECT * from debt WHERE id = :id")
    fun getItem(id: Int): Flow<Debt>

    @Query("SELECT * from debt ORDER BY name ASC")
    fun getAllItems(): Flow<List<Debt>>
}
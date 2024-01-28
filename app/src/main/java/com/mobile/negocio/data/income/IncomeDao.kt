package com.mobile.negocio.data.income

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Income)

    @Update
    suspend fun update(item: Income)

    @Delete
    suspend fun delete(item: Income)

    @Query("SELECT * from income WHERE id = :id")
    fun getItem(id: Int): Flow<Income>

    @Query("SELECT * from income ORDER BY name ASC")
    fun getAllItems(): Flow<List<Income>>
}
package com.mobile.negocio.data.income

import kotlinx.coroutines.flow.Flow

interface IncomeRepository {
    fun getAllItemsStream(): Flow<List<Income>>
    fun getItemStream(id: Int): Flow<Income?>
    suspend fun insertItem(item: Income)
    suspend fun deleteItem(item: Income)
    suspend fun updateItem(item: Income)

}
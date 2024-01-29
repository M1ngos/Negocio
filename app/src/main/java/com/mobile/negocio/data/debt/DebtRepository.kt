package com.mobile.negocio.data.debt

import kotlinx.coroutines.flow.Flow

interface DebtRepository {
    fun getAllItemsStream(): Flow<List<Debt>>
    fun getItemStream(id: Int): Flow<Debt?>
    suspend fun insertItem(item: Debt)
    suspend fun deleteItem(item: Debt)
    suspend fun updateItem(item: Debt)

}
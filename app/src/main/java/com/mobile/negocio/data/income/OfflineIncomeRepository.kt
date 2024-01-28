package com.mobile.negocio.data.income

import kotlinx.coroutines.flow.Flow

class OfflineIncomeRepository(private val incomeDao: IncomeDao) : IncomeRepository {
    override fun getAllItemsStream(): Flow<List<Income>> = incomeDao.getAllItems()

    override fun getItemStream(id: Int): Flow<Income?> = incomeDao.getItem(id)

    override suspend fun insertItem(item: Income) = incomeDao.insert(item)

    override suspend fun deleteItem(item: Income) = incomeDao.delete(item)

    override suspend fun updateItem(item: Income) = incomeDao.update(item)
}
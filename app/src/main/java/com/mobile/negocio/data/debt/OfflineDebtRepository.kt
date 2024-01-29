package com.mobile.negocio.data.debt

import kotlinx.coroutines.flow.Flow

class OfflineDebtRepository(private val debtDao: DebtDao) : DebtRepository {
    override fun getAllItemsStream(): Flow<List<Debt>> = debtDao.getAllItems()

    override fun getItemStream(id: Int): Flow<Debt?> = debtDao.getItem(id)

    override suspend fun insertItem(item: Debt) = debtDao.insert(item)

    override suspend fun deleteItem(item: Debt) = debtDao.delete(item)

    override suspend fun updateItem(item: Debt) = debtDao.update(item)
}
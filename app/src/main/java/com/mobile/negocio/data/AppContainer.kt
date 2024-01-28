package com.mobile.negocio.data

import android.content.Context
import com.mobile.negocio.data.income.IncomeRepository
import com.mobile.negocio.data.income.OfflineIncomeRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val itemsRepository: IncomeRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {

    override val itemsRepository: IncomeRepository by lazy {
        OfflineIncomeRepository(AppDatabase.getDatabase(context).incomeDao())
    }
}
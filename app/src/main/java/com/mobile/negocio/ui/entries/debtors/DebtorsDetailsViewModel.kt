package com.mobile.negocio.ui.entries.debtors

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mobile.negocio.ui.entries.income.IncomeDetails

class DebtorsDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val itemId: Int = checkNotNull(savedStateHandle[DebtorDetailsDestination.registryIdArg])

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class ItemDetailsUiState(
    val registryDetails: IncomeDetails = IncomeDetails()
)

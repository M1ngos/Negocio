package com.mobile.negocio.ui.entries.income

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

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

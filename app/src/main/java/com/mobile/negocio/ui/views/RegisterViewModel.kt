package com.mobile.negocio.ui.views


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.negocio.data.income.Income
import com.mobile.negocio.data.income.IncomeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all items in the Room database.
 */
class RegisterViewModel(incomeRepository: IncomeRepository) : ViewModel() {

    val registerUiState: StateFlow<RegisterUiState> =
        incomeRepository.getAllItemsStream().map { RegisterUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = RegisterUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}


data class RegisterUiState(val itemList: List<Income> = listOf())

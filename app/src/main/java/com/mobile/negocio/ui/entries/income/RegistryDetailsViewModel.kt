package com.mobile.negocio.ui.entries.income

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.negocio.data.income.IncomeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class RegistryDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val incomeRepository: IncomeRepository,
): ViewModel() {
    private val registryId: Int = checkNotNull(savedStateHandle[RegistryDetailsDestination.registryIdArg])

    val uiState: StateFlow<RegistryDetailsUiState> =
        incomeRepository.getItemStream(registryId)
            .filterNotNull()
            .map {
                RegistryDetailsUiState(itemDetails = it.toIncomeDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = RegistryDetailsUiState()
            )

    fun changeState() {
        viewModelScope.launch {
            val currentState = uiState.value.itemDetails.toIncomeItem()
            if(currentState.status == false) {
                incomeRepository.updateItem(currentState.copy(status = true))
            }
        }
    }

    suspend fun deleteItem() {
        incomeRepository.deleteItem(uiState.value.itemDetails.toIncomeItem())
    }
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}



data class RegistryDetailsUiState(
    val itemDetails: IncomeDetails = IncomeDetails()
)

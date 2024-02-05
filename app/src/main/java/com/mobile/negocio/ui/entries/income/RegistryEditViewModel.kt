package com.mobile.negocio.ui.entries.income

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.negocio.data.income.IncomeRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RegistryEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val incomeRepository: IncomeRepository
    ) : ViewModel () {
        var registryUiState by mutableStateOf(RegistryUiState())
            private set

        private val registryId: Int = checkNotNull(savedStateHandle[RegistryEditDestination.registryIdArg])

    init {
        viewModelScope.launch {
            registryUiState = incomeRepository.getItemStream(registryId)
                .filterNotNull()
                .first()
                .toIncomeUiState()
        }
    }

    suspend fun updateItem() {
        if (validateInput(registryUiState.incomeDetails)) {
            incomeRepository.updateItem(registryUiState.incomeDetails.toIncomeItem())
        }
    }

    fun updateUiState(incomeDetails: IncomeDetails) {
        registryUiState =
            RegistryUiState(incomeDetails = incomeDetails, isEntryValid = validateInput(incomeDetails))
    }

    private fun validateInput(uiState: IncomeDetails = registryUiState.incomeDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && value.isNotBlank() && quantity.isNotBlank()
        }
    }

}
package com.mobile.negocio.ui.entries.debt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.negocio.data.debt.DebtRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RegistryEditViewModelAlt(
    savedStateHandle: SavedStateHandle,
    private val debtRepository: DebtRepository
    ) : ViewModel () {
    var registryUiState by mutableStateOf(RegistryUiStateAlt())
        private set

    private val registryId: Int = checkNotNull(savedStateHandle[RegistryEditDestinationAlt.registryIdArg])

    init {
        viewModelScope.launch {
            registryUiState = debtRepository.getItemStream(registryId)
                .filterNotNull()
                .first()
                .toDebtUiState()
        }
    }

    suspend fun updateItem() {
        if (validateInput(registryUiState.debtDetails)) {
            debtRepository.updateItem(registryUiState.debtDetails.toDebtItem())
        }
    }

    fun updateUiState(debtDetails: DebtDetails) {
        registryUiState =
            RegistryUiStateAlt(debtDetails = debtDetails, isEntryValid = validateInput(debtDetails))
    }

    private fun validateInput(uiState: DebtDetails = registryUiState.debtDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && value.isNotBlank()
        }
    }


}
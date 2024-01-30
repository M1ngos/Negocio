package com.mobile.negocio.ui.entries.debt

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.negocio.data.debt.DebtRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class RegistryDetailsViewModelAlt(
    savedStateHandle: SavedStateHandle,
    private val debtRepository: DebtRepository,
): ViewModel() {
    private val registryId: Int = checkNotNull(savedStateHandle[RegistryDetailsDestinationAlt.registryIdArg])

    val uiState: StateFlow<com.mobile.negocio.ui.entries.debt.RegistryDetailsUiState> =
        debtRepository.getItemStream(registryId)
            .filterNotNull()
            .map {
                RegistryDetailsUiState(itemDetails = it.toDebtDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = com.mobile.negocio.ui.entries.debt.RegistryDetailsUiState()
            )

    suspend fun deleteItem() {
        debtRepository.deleteItem(uiState.value.itemDetails.toDebtItem())
    }
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
data class RegistryDetailsUiState(
    val itemDetails: DebtDetails = DebtDetails()
)

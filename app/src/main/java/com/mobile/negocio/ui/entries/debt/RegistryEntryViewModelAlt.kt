package com.mobile.negocio.ui.entries.debt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mobile.negocio.data.debt.Debt
import com.mobile.negocio.data.debt.DebtRepository
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Locale

class RegistyEntryViewModelAlt(
    private val debtRepository: DebtRepository
): ViewModel() {
    var registryUistateAlt by mutableStateOf(RegistryUiStateAlt())
        private set


    fun updateUiStateAlt(debtDetails: DebtDetails) {
        registryUistateAlt =
            RegistryUiStateAlt(debtDetails = debtDetails, isEntryValid = validateInput(debtDetails))
    }


    suspend fun saveItem() {
        if (validateInput()) {
            debtRepository.insertItem(registryUistateAlt.debtDetails.toDebtItem())
        }
    }

    private fun validateInput(uiState: DebtDetails = registryUistateAlt.debtDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && value.isNotBlank()
        }
    }
}

data class RegistryUiStateAlt(
    val debtDetails: DebtDetails = DebtDetails(),
    val isEntryValid: Boolean = false
)

data class DebtDetails(
    val id: Int = 0,
    val name: String = "",
    val value: String = "",
    val details: String = "",
    val date: String = LocalDate.now().toString()
)
fun Debt.formatedValue(): String {
    return NumberFormat.getCurrencyInstance(Locale("pt","MZ")).format(value)
}


fun DebtDetails.toDebtItem(): Debt = Debt(
    id = id,
    name = name,
    value = value.toDoubleOrNull() ?: 0.0,
    details = details,
    date = date
)

fun Debt.toDebtUiState(isEntryValid: Boolean = false): RegistryUiStateAlt = RegistryUiStateAlt(
    debtDetails = this.toDebtDetails(),
    isEntryValid = isEntryValid
)
fun Debt.toDebtDetails(): DebtDetails = DebtDetails(
    id = id,
    name = name,
    value = value.toString(),
    details = details,
    date = date
)




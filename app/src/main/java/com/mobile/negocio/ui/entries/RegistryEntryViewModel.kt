package com.mobile.negocio.ui.entries

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mobile.negocio.data.income.Income
import com.mobile.negocio.data.income.IncomeRepository
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class RegistryEntryViewModel(
    private val incomeRepository: IncomeRepository
) : ViewModel() {

    var registryUistate by mutableStateOf(RegistryUiState())
        private set


    fun updateStatus(isChecked: Boolean) {
        registryUistate = registryUistate.copy(
            incomeDetails = registryUistate.incomeDetails.copy(status = isChecked)
        )
    }
    fun updateUiState(incomeDetails: IncomeDetails) {
        registryUistate =
            RegistryUiState(incomeDetails = incomeDetails, isEntryValid = validateInput(incomeDetails))
    }

    suspend fun saveItem() {
        if (validateInput()) {
            incomeRepository.insertItem(registryUistate.incomeDetails.toIncomeItem())
        }
    }

    private fun validateInput(uiState: IncomeDetails = registryUistate.incomeDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && value.isNotBlank() && quantity.isNotBlank()
        }
    }

}

data class RegistryUiState(
    val incomeDetails: IncomeDetails = IncomeDetails(),
    val isEntryValid: Boolean = false
)

data class IncomeDetails(
    val id: Int = 0,
    val name: String = "",
    val contact: String = "",
    val value: String = "",
    val quantity: String = "",
    val status: Boolean = false,
    val date: String = LocalDate.now().toString()
)

fun Income.formatedValue(): String {
    return NumberFormat.getCurrencyInstance(Locale("pt","MZ")).format(value)
}

fun IncomeDetails.toIncomeItem(): Income = Income(
    id = id,
    name = name,
    contact = contact,
    value = value.toDoubleOrNull() ?: 0.0,
    quantity = quantity.toIntOrNull() ?: 0,
    status = status,
    date = date
)

fun Income.toIncomeUiState(isEntryValid: Boolean = false): RegistryUiState = RegistryUiState(
    incomeDetails = this.toIncomeDetails(),
    isEntryValid = isEntryValid
)
fun Income.toIncomeDetails(): IncomeDetails = IncomeDetails(
    id = id,
    name = name,
    contact = contact,
    value = value.toString(),
    quantity = quantity.toString(),
    status = status,
    date = date
)
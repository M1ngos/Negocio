package com.mobile.negocio.ui.entries.income

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.mobile.negocio.R
import com.mobile.negocio.ui.AppViewModelProvider
import com.mobile.negocio.ui.navigation.AlternativeTopBar
import com.mobile.negocio.ui.navigation.NavigationDestination
import com.mobile.negocio.ui.theme.AppTheme
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale

object RegistryEntryDestination : NavigationDestination {
    override val route = "registry_entry"
    override val titleRes = R.string.add
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistryEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: RegistryEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val  context = LocalContext.current
    Scaffold(
        topBar = {
            AlternativeTopBar(
                title = stringResource(RegistryEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { it ->
        RegistryEntryBody(
            registryUistate = viewModel.registryUistate,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the item may not be saved in the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.saveItem()
                    navigateBack()
                    Toast.makeText(context,
                        "Registado com sucesso!",
                        Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }

}

@Composable
fun RegistryEntryBody(
    registryUistate: RegistryUiState,
    onItemValueChange: (IncomeDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
    ) {
        IncomeInputForm(
            itemDetails = registryUistate.incomeDetails,
            onValueChange = onItemValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = registryUistate.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeInputForm(
    itemDetails: IncomeDetails,
    modifier: Modifier,
    onValueChange: (IncomeDetails) -> Unit,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {

        val calendarState = rememberSheetState()

        CalendarDialog(
            state = calendarState,
            config = CalendarConfig(
                monthSelection = true,
                yearSelection = true,
            ),
            selection = CalendarSelection.Date {date ->
                onValueChange(itemDetails.copy(date = date.toString() ))
            }
        )

        OutlinedTextField(
            value = itemDetails.name,
            onValueChange = { onValueChange(itemDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.registry_name_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = itemDetails.contact,
            onValueChange = { onValueChange(itemDetails.copy(contact = it)) },
            label = { Text(stringResource(R.string.registry_contact_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = itemDetails.value,
            onValueChange = { onValueChange(itemDetails.copy(value = it)) },
            keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),

            label = { Text(stringResource(R.string.registry_value_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            leadingIcon = { Text(Currency.getInstance(Locale("pt","MZ")).symbol) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = itemDetails.quantity,
            onValueChange = { onValueChange(itemDetails.copy(quantity = it)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
            label = { Text(stringResource(R.string.registry_quantity_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        
        Button(
            onClick = { calendarState.show() })
        {
            Text(text = "Alterar data do registo (Opcional)")    
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.registry_now))

            Checkbox(
                checked = itemDetails.status,
                onCheckedChange = { onValueChange(itemDetails.copy(status = it)) },
                enabled = true,
            )
        }

        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_large))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemEntryScreenPreview() {
    AppTheme {
        RegistryEntryBody(registryUistate = RegistryUiState(
            IncomeDetails(
                name = "Ana", value = "250", quantity = "1"
            )
        ), onItemValueChange = {}, onSaveClick = {})
    }
}

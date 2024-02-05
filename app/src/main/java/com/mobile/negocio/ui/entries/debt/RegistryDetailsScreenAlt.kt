package com.mobile.negocio.ui.entries.debt

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobile.negocio.R
import com.mobile.negocio.data.debt.Debt
import com.mobile.negocio.ui.AppViewModelProvider
import com.mobile.negocio.ui.navigation.AlternativeTopBar
import com.mobile.negocio.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object RegistryDetailsDestinationAlt : NavigationDestination {
    override val route = "registry_details_alt"
    override val titleRes = R.string.registry_details_debt
    const val registryIdArg = "registryId"
    val routeWithArgs = "$route/{$registryIdArg}"
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistryDetailsScreenAlt(
    navigateToEditItem: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegistryDetailsViewModelAlt = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            AlternativeTopBar(
                title = stringResource(RegistryDetailsDestinationAlt.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },

    floatingActionButton = {
        FloatingActionButton(
            onClick = { navigateToEditItem(uiState.value.itemDetails.id) },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.edit_registry_details),
            )
        }
    }, modifier = modifier
    ) { innerPadding ->
        RegistryDetailsBody(
            registryDetailsUiState = uiState.value,
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteItem()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
fun RegistryDetailsBody(
    registryDetailsUiState: RegistryDetailsUiState,
    onDelete: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        RegistryDetails(
            item = registryDetailsUiState.itemDetails.toDebtItem(), modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium))

        ) {
            Text(stringResource(R.string.delete))
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}



@Composable
fun RegistryDetails(
    item: Debt,
    modifier: Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        ItemDetailsRow(
            labelResID = R.string.registry_supplier_name,
            itemDetail = item.name,
            modifier = Modifier.padding(
                horizontal = dimensionResource(
                    id = R.dimen
                        .padding_medium
                )
            )
        )

        ItemDetailsRow(
            labelResID = R.string.registry_value,
            itemDetail = item.value.toString(),
            modifier = Modifier.padding(
                horizontal = dimensionResource(
                    id = R.dimen
                        .padding_medium
                )
            )
        )

        ItemDetailsRow(
            labelResID = R.string.registry_details,
            itemDetail = item.details,
            modifier = Modifier.padding(
                horizontal = dimensionResource(
                    id = R.dimen
                        .padding_medium
                )
            )
        )

        ItemDetailsRow(
            labelResID = R.string.registry_date,
            itemDetail = item.date,
            modifier = Modifier.padding(
                horizontal = dimensionResource(
                    id = R.dimen
                        .padding_medium
                )
            )
        )
    }
}

@Composable
private fun ItemDetailsRow(
    @StringRes labelResID: Int, itemDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = stringResource(labelResID))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = itemDetail, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        })
}



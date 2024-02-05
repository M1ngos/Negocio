package com.mobile.negocio.ui.entries.income

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobile.negocio.R
import com.mobile.negocio.ui.AppViewModelProvider
import com.mobile.negocio.ui.navigation.AlternativeTopBar
import com.mobile.negocio.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object RegistryEditDestination : NavigationDestination {
    override val route = "item_edit"
    override val titleRes = R.string.edit_registry_details
    const val registryIdArg = "registryId"
    val routeWithArgs = "$route/{$registryIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun RegistryEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegistryEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            AlternativeTopBar(
                title = stringResource(RegistryEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
//                actions = {
//                    IconButton(onClick = { /* Handle action 1 click */ }) {
//                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
//                    }
//                }
            )
        }
    ) { innerPadding ->
        RegistryEntryBody(
            registryUistate = viewModel.registryUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                viewModel.updateItem()
                navigateBack()
            }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }

}
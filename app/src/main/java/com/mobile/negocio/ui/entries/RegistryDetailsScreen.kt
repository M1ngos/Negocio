package com.mobile.negocio.ui.entries

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.mobile.negocio.R
import com.mobile.negocio.ui.navigation.AlternativeTopBar
import com.mobile.negocio.ui.navigation.NavigationDestination

object RegistryDetailsDestination : NavigationDestination {
    override val route = "registry_details"
    override val titleRes = R.string.registry_details
    const val registryIdArg = "registryId"
    val routeWithArgs = "$route/{$registryIdArg}"
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistryDetailsScreen(
    navigateToEditRegistry: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
    topBar = {
        AlternativeTopBar(
            title = stringResource(RegistryDetailsDestination.titleRes),
            canNavigateBack = true,
            navigateUp = navigateBack
        )
    },
    floatingActionButton = {
        FloatingActionButton(
            onClick = { navigateToEditRegistry(0) },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.edit_registry_details),
            )
        }
    }, modifier = modifier
    ){
        Text(text = "")
//        ItemDetailsBody(
//            itemDetailsUiState = ItemDetailsUiState(),
//            onSellItem = { },
//            onDelete = { },
//            modifier = Modifier
//                .padding(innerPadding)
//                .verticalScroll(rememberScrollState())
//        )

    }
}
package com.mobile.negocio.ui.components.search

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobile.negocio.R
import com.mobile.negocio.data.debt.Debt
import com.mobile.negocio.data.income.Income
import com.mobile.negocio.ui.AppViewModelProvider
import com.mobile.negocio.ui.navigation.NavigationDestination
import com.mobile.negocio.ui.views.DebtItem
import com.mobile.negocio.ui.views.IncomeItem
import com.mobile.negocio.ui.views.RegisterViewModel
import com.mobile.negocio.ui.views.RegisterViewModelAlt

object SearchDestination : NavigationDestination {
    override val route = "search_destination"
    override val titleRes = androidx.compose.material3.R.string.search_bar_search
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onIncomeClick: (Int) -> Unit,
    onDebtClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory),
    viewModelALt: RegisterViewModelAlt = viewModel(factory = AppViewModelProvider.Factory),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    val registerUiState by viewModel.registerUiState.collectAsState()
    val registerUiStateAlt by viewModelALt.registerUiStateAlt.collectAsState()

    val state = remember { mutableStateOf(TextFieldValue("")) }

    Column {
       Spacer(modifier = modifier.statusBarsPadding())
        SearchToolBar(
            onBackClick = onBackClick,
            state = state,
        )
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Resultados :",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
            )
        }
        SearchList(
            state = state,
            searchList = registerUiState.itemList + registerUiStateAlt.itemList,
            onIncomeClick = { onIncomeClick(it.id) },
            onDebtClick = { onDebtClick(it.id) },
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
        )
    }
}

@Composable
fun SearchList(
    state: MutableState<TextFieldValue>,
    searchList: List<Any>,
    onIncomeClick: (Income) -> Unit,
    onDebtClick: (Debt) -> Unit,
    modifier: Modifier
) {

    val filteredList = searchList.filter {
        when (it) {
            is Income -> it.name.contains(state.value.text, ignoreCase = true)
            is Debt -> it.name.contains(state.value.text, ignoreCase = true)
            else -> false
        }
    }

    if(searchList.isEmpty()) {
        Text(
            text = stringResource(R.string.no_item_description),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_large))
                .fillMaxWidth()
        )
    } else {
        LazyColumn(modifier = modifier) {
            items(
                items = filteredList,
                key = { item ->
                    when (item) {
                        is Income -> "Income_${item.id}"
                        is Debt -> "Debt_${item.id}"
                        else -> throw IllegalArgumentException("Unsupported item type")
                    }
                }
            ) { item ->
                when (item) {
                    is Income -> {
                        IncomeItem(item = item,
                            modifier = Modifier
                                .padding(dimensionResource(id = R.dimen.padding_small))
                                .clickable { onIncomeClick(item) })
                    }
                    is Debt -> {
                        DebtItem(item = item,
                            modifier = Modifier
                                .padding(dimensionResource(id = R.dimen.padding_small))
                                .clickable { onDebtClick(item) })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchToolBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    state: MutableState<TextFieldValue>,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchExplicitlyTriggered = {
        keyboardController?.hide()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
            )
        }
     TextField(
         colors = TextFieldDefaults.colors(
             focusedIndicatorColor = Color.Transparent,
             unfocusedIndicatorColor = Color.Transparent,
             disabledIndicatorColor = Color.Transparent,
         ),
         leadingIcon = {
             Icon(
                 imageVector = Icons.Default.Search,
                 contentDescription = "",
                 tint = MaterialTheme.colorScheme.onSurface,
             )
         },
         trailingIcon = {
             if (state.value.text.isNotEmpty()) {
                 IconButton(
                     onClick = {
                         state.value = TextFieldValue("")
                     },
                 ) {
                     Icon(
                         imageVector = Icons.Default.Close,
                         contentDescription = "",
                         tint = MaterialTheme.colorScheme.onSurface,
                     )
                 }
             }
         },
         modifier = Modifier
             .fillMaxWidth()
             .padding(16.dp)
             .focusRequester(focusRequester)
             .onKeyEvent {
                 if (it.key == Key.Enter) {
                     onSearchExplicitlyTriggered()
                     true
                 } else {
                     false
                 }
             }
             .testTag("searchTextField"),
         value = state.value,
         onValueChange = { value ->
             state.value = value
         },
         shape = RoundedCornerShape(32.dp),
         keyboardOptions = KeyboardOptions(
             imeAction = ImeAction.Search,
         ),
         keyboardActions = KeyboardActions(
             onSearch = {
                 onSearchExplicitlyTriggered()
             },
         ),
         maxLines = 1,
         singleLine = true,
         )
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@Composable
@Preview
fun SearchScreenPreview() {
    SearchScreen(onIncomeClick = {}, onDebtClick = {}, onBackClick = { /*TODO*/ })
}

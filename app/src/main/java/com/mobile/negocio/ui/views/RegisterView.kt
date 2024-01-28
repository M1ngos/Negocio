package com.mobile.negocio.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobile.negocio.R
import com.mobile.negocio.data.income.Income
import com.mobile.negocio.ui.AppViewModelProvider
import com.mobile.negocio.ui.entries.formatedValue
import com.mobile.negocio.ui.navigation.navRegistryItems
import com.mobile.negocio.ui.theme.AppTheme
import java.time.LocalDate


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegisterScreen (
    navigateToRegistryEntry: () -> Unit,
    navigateToRegistryEntryAlt: () -> Unit,
    navigateToUpdateRegistry: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory)
    ) {

    val registerUiState by viewModel.registerUiState.collectAsState()

    var selectedTabIndex by remember {
        mutableStateOf(0)
    }

    val pagerState = rememberPagerState {
        navRegistryItems.size
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { if(selectedTabIndex == 0 ) navigateToRegistryEntry() else navigateToRegistryEntryAlt() },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Filled.Add ,
                    contentDescription = stringResource(R.string.add)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LaunchedEffect(selectedTabIndex) {
                pagerState.animateScrollToPage(selectedTabIndex)
            }

            LaunchedEffect(pagerState.currentPage) {
                selectedTabIndex = pagerState.currentPage
            }

            TabRow(selectedTabIndex = selectedTabIndex ) {
                navRegistryItems.forEachIndexed { index, navRegistryItem ->
                    Tab(
                        selected = index == selectedTabIndex,
                        onClick = {
                            selectedTabIndex = index
                        },
                        text = {
                            Text(text = navRegistryItem.title, fontSize = 18.sp)
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                index ->
                when (index) {
                    0 -> IncomeList(
                        incomeList = registerUiState.itemList,
                        onItemClick = { navigateToUpdateRegistry(it.id) },
                        modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
                    )
                }
            }
        }
    }
}

@Composable
fun IncomeList(
    incomeList: List<Income>, onItemClick: (Income) -> Unit, modifier: Modifier = Modifier
) {
    var selectedFilterIndex by remember { mutableStateOf(0) }

    if (incomeList.isEmpty()){
        Text(
            text = stringResource(R.string.no_item_description),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
    } else {
        LazyColumn(modifier = modifier) {
            items(items = incomeList, key = { it.id }) { item ->
                if (item.status == true) {
                    IncomeItem(item = item,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_small))
                            .clickable { onItemClick(item) })
                }
            }
        }
    }
}

@Composable
fun DateFilterButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.extra_padding_small))
            .border(
                shape = CircleShape,
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
            ),
        enabled = !isSelected
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
        )
    }
}
@Composable
private fun IncomeItem(
    item: Income, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = item.formatedValue(),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${item.quantity} ${if (item.quantity > 1) "favos" else "favo"}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = item.date,
                    style = MaterialTheme.typography.titleMedium
                )
            }

        }
    }
}



@Preview
@Composable
fun IncomeCardPreview() {
    AppTheme {
        IncomeItem(
            Income(0,"Ana","250.0",250.0, quantity = 1,status = false, date = LocalDate.now().toString())
        )
    }
}


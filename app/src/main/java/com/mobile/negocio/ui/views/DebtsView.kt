package com.mobile.negocio.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobile.negocio.R
import com.mobile.negocio.data.dash.calculateDays
import com.mobile.negocio.data.income.Income
import com.mobile.negocio.ui.AppViewModelProvider
import com.mobile.negocio.ui.entries.income.formatedValue


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtsScreen (
    navigateToUpdateRegistry: (Int) -> Unit,
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val registerUiState by viewModel.registerUiState.collectAsState()
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(context,"Ainda em desenvolvimento",Toast.LENGTH_SHORT).show()
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = ""
                        )
                    }
                },
                title = {
                    Text("Número de devedores: ${registerUiState.count}", fontSize = 18.sp)
                }
            )
        },
    ) {it ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                DebtorsList(
                    debtorsList = registerUiState.itemList,
                    onItemClick = { navigateToUpdateRegistry(it.id) },
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
                )
            }
    }
}

@Composable
fun DebtorsList(
    debtorsList: List<Income>,
    onItemClick: (Income) -> Unit,
    modifier: Modifier
) {
    val validItems = mutableListOf<Income>()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()){
        if (debtorsList.isEmpty()){
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
                items(items = debtorsList, key = { it.id }) { item ->
                    if (!item.status) {
                        validItems.add(item)
                        IncomeItem(
                            item = item,
                            expires = calculateDays(item),
                            modifier = Modifier
                                .padding(dimensionResource(id = R.dimen.padding_small))
                                .clickable { onItemClick(item) }
                        )
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = {
                     validItems.forEach {
                         if(calculateDays(it).toInt() == 0){
                             sendWhatsAppMessage(it.contact,context,it)
                         }
                     }
            },
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_extra_large))
                .align(alignment = Alignment.BottomEnd)
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = null)
        }
    }
}


@Preview
@Composable
fun PreviewItem() {
    val it = Income(0,"John","84",500.0,5,false,"2024-01-02")
    IncomeItem(item = it, expires = calculateDays(it) )
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun IncomeItem(
    item: Income,
    expires: Long,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
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
                Spacer(Modifier.weight(1f))
                Text(
                    text = "${item.quantity} ${if (item.quantity > 1) "favos" else "favo"}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = if(expires.toInt() == 0) "Venceu" else "Vencimento:$expires ${if (expires > 1) "dias" else "dia"}",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

fun sendWhatsAppMessage(phoneNumber: String, context: Context, income: Income) {
//    for (phoneNumber in phoneNumbers) {
        val formattedPhoneNumber = if (phoneNumber.startsWith("+258")) {
            phoneNumber
        } else {
            "+258$phoneNumber"
        }
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://wa.me/$formattedPhoneNumber?text=${Uri.encode(" " +
                "\nOlá ${income.name},\n" +
                "Espero que esta mensagem lhe encontre bem! \uD83D\uDE0A \n"+
                "Este é um lembrete da dívida do pagamento de ovos \n\n" +
                "*Detalhes da dívida:*\n" +
                "- Valor: ${income.value}\n" +
                "- Quandidade de favos: ${income.quantity}\n" +
                "- Data da venda: ${income.date}" +
                "\n\nPor favor, garanta o pagamento dentro do prazo de 30 dias" +
                "\n\n*Mensagem gerada por um computador*")}")
        startActivity(context, intent, null)
//    }
}


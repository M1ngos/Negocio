package com.mobile.negocio.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobile.negocio.data.debt.Debt
import com.mobile.negocio.data.income.Income
import com.mobile.negocio.ui.AppViewModelProvider
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.random.Random

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashScreen (
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory),
    viewModelALt: RegisterViewModelAlt = viewModel(factory = AppViewModelProvider.Factory)
) {

    val registerUiState by viewModel.registerUiState.collectAsState()
    val registerUiStateAlt by viewModelALt.registerUiStateAlt.collectAsState()

    val chartEntryModelProducer = ChartEntryModelProducer(getRandomEntries())
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
       snackbarHost = {
           SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    scope.launch {
                        snackbarHostState.showSnackbar("Ainda em desevolvimento")
                    }
                }
                .fillMaxWidth(),
        ) {
            Chart(
                chart = columnChart(),
                chartModelProducer = chartEntryModelProducer,
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(),
            )
            ProfitCard(
                registerUiState.itemList,
                registerUiStateAlt.itemList
            )
        }
    }

}


@Composable
fun ProfitCard(
    incomeList: List<Income>,
    debtList: List<Debt>,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(25.dp)
            )
            Spacer(modifier = Modifier.width(25.dp))
            Text(
                text = "Lucro: "+getProfits(incomeList,debtList),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun getProfits(incomeList: List<Income>,debtList: List<Debt>):String {
    return  NumberFormat.getCurrencyInstance(Locale("pt","MZ")).format(getTotalIncome(incomeList) - getTotalDebt(debtList))

}

fun getTotalDebt(debtList: List<Debt>): Double {
    var acumulator = 0.0
    debtList.forEach { item ->
        acumulator += item.value
    }
    return acumulator
}

fun getTotalIncome(incomeList: List<Income>): Double {
    var acumulator = 0.0
    incomeList.forEach { item ->
        acumulator += item.value
    }
    return acumulator
}

fun getRandomEntries() = List(4) { entryOf(it, Random.nextFloat() * 16f) }
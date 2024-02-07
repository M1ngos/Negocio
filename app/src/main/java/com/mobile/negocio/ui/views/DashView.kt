package com.mobile.negocio.ui.views

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jaikeerthick.composable_graphs.composables.line.LineGraph
import com.jaikeerthick.composable_graphs.composables.line.model.LineData
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphColors
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphFillType
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphStyle
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphVisibility
import com.mobile.negocio.R
import com.mobile.negocio.data.dash.createMonthlyData
import com.mobile.negocio.data.dash.getLiquidProfit
import com.mobile.negocio.data.dash.getTotalDebt
import com.mobile.negocio.data.dash.getTotalIncome
import com.mobile.negocio.ui.AppViewModelProvider
import com.mobile.negocio.ui.theme.AppTheme
import java.text.NumberFormat
import java.util.Locale


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashScreen (
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory),
    viewModelALt: RegisterViewModelAlt = viewModel(factory = AppViewModelProvider.Factory)
) {

    val registerUiState by viewModel.registerUiState.collectAsState()
    val registerUiStateAlt by viewModelALt.registerUiStateAlt.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
       snackbarHost = {
           SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
//            Log.d("data:", months.toString())
            Log.d("data:", createMonthlyData(registerUiState.itemList).toString())

            LineGraph(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                data = createMonthlyData(registerUiState.itemList),
                onPointClick = { value: LineData ->
                    Toast.makeText(context,"Cartões vendidos em ${value.x} ",Toast.LENGTH_SHORT).show()
                },
                style = LineGraphStyle(
                    visibility = LineGraphVisibility(
                        isCrossHairVisible = false,
                        isYAxisLabelVisible = true
                    ),
                    colors = LineGraphColors(
                        lineColor = MaterialTheme.colorScheme.primary,
                        pointColor = MaterialTheme.colorScheme.primary,
                        clickHighlightColor = MaterialTheme.colorScheme.inversePrimary,
                        fillType = LineGraphFillType.Gradient(
                            brush = Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.inversePrimary))
                        )
                    )
                ),
            )

            StackedTextCard(
                painter1 = painterResource(id = R.drawable.profit),
                painter2 = painterResource(id = R.drawable.loss),
                gains = "+${getTotalIncome(registerUiState.itemList)}",
                loss = "-${getTotalDebt(registerUiStateAlt.itemList)}",
                profit = getLiquidProfit(registerUiState.itemList,registerUiStateAlt.itemList)
            )
        }
    }
}

@Composable
fun StackedTextCard(
    painter1: Painter,
    painter2: Painter,
    gains: String,
    loss: String,
    profit: Double,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(250.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painter1,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .size(80.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Receitas", style = MaterialTheme.typography.titleLarge)
                    Text(text = gains, style = MaterialTheme.typography.titleLarge)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painter2,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .size(80.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Despesas", style = MaterialTheme.typography.titleLarge)
                    Text(text = loss, style = MaterialTheme.typography.titleLarge)
                }
            }
            Row {
                Text(text = "Lucro líquido : ", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    text = NumberFormat.getCurrencyInstance(Locale("pt","MZ")).format(profit),
                    style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold ,
                    color = if(profit > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
            }
        }
    }
}


@Preview
@Composable
fun StackedTextCardWithPreview() {
    AppTheme {
        StackedTextCard(
            painter1 = painterResource(id = R.drawable.profit),
            painter2 = painterResource(id = R.drawable.loss),
            gains = "+1500",
            loss = "-500",
            profit = 10000.0
        )
    }
}

package com.mobile.negocio.data.dash

import com.jaikeerthick.composable_graphs.composables.line.model.LineData
import com.mobile.negocio.data.debt.Debt
import com.mobile.negocio.data.income.Income
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Locale



fun createMonthlyData(itemList: List<Income>): List<LineData> {
    val months = listOf(
        "Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
        "Jul", "Ago", "Set", "Out", "Nov", "Dez"
    )
    return months.mapIndexed { index, month ->
        LineData(x = month, y = getTotalInMonth(itemList,month))
    }
}

fun getTotalInMonth(itemList: List<Income>, month: String): Int {
    val currentYear = LocalDate.now().year
    val toCompare = when (month) {
        "Jan" -> "$currentYear-01"
        "Fev" -> "$currentYear-02"
        "Mar" -> "$currentYear-03"
        "Abr" -> "$currentYear-04"
        "Mai" -> "$currentYear-05"
        "Jun" -> "$currentYear-06"
        "Jul" -> "$currentYear-07"
        "Ago" -> "$currentYear-08"
        "Set" -> "$currentYear-09"
        "Out" -> "$currentYear-10"
        "Nov" -> "$currentYear-11"
        "Dez" -> "$currentYear-12"
        else -> throw IllegalArgumentException("Invalid month: $month")
    }

    return itemList.filter { it.date.startsWith(toCompare) }
        .count()
}

fun getTotalIncome(itemList: List<Income>): String {
    var accumulator = 0.0
    itemList.forEach{item ->
        if(item.status) {
            accumulator += item.value
        }
    }
    return NumberFormat.getCurrencyInstance(Locale("pt","MZ")).format(accumulator)
}

fun getTotalDebt(itemList: List<Debt>): String {
    var accumulator = 0.0
    itemList.forEach{item ->
            accumulator += item.value
    }
    return NumberFormat.getCurrencyInstance(Locale("pt","MZ")).format(accumulator)
}

fun getLiquidProfit(incomeList: List<Income>,debtList: List<Debt>): Double {
    val totalProfit = incomeList
        .filter { it.status }
        .sumOf { it.value }

    val totalDebt = debtList.sumOf { it.value }

    val difference = totalProfit - totalDebt

    return difference
}
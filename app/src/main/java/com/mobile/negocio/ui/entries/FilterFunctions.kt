package com.mobile.negocio.ui.entries


import com.mobile.negocio.data.income.Income
import java.time.LocalDate

fun filterByDate(filterIndex: Int, incomeList: List<Income>): List<Income> {
    val currentDate = LocalDate.now().toString()

    return when (filterIndex) {
        0 -> incomeList.filter { it.date == currentDate }
        // Add cases for other filters
//        1 -> incomeList.filter { /* logic for this week */ }
//        2 -> incomeList.filter { /* logic for this month */ }
        else -> incomeList // "All" case
    }
}


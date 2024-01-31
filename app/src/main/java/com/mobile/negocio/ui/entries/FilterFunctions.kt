package com.mobile.negocio.ui.entries


import com.mobile.negocio.data.debt.Debt
import com.mobile.negocio.data.income.Income
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.Locale


private val now: String = LocalDate.now().toString()

fun filterItems(items: List<Income>, selectedFilterIndex: Int): List<Income> {
    return when (selectedFilterIndex) {
        1 -> filterByToday(items)
        2 -> filterByWeek(items)
        3 -> filterByMonth(items)
        else -> items
    }
}

fun filterItemsAlt(items: List<Debt>, selectedFilterIndex: Int): List<Debt> {
    return when (selectedFilterIndex) {
        1 -> filterByTodayAlt(items)
        2 -> filterByWeekAlt(items)
        3 -> filterByMonthAlt(items)
        else -> items
    }
}


fun isWithinOneWeek(currentDate: String, dateToCheck: String): Boolean {
    val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
    val currentLocalDate = LocalDate.parse(currentDate, formatter)
    val dateToCheckLocalDate = LocalDate.parse(dateToCheck, formatter)

    val daysDifference = ChronoUnit.DAYS.between(currentLocalDate, dateToCheckLocalDate)

    return daysDifference in 0..6
}

fun isWithinThisWeek(currentDate: String, dateToCheck: String): Boolean {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
    val currentLocalDate = LocalDate.parse(currentDate, formatter)
    val dateToCheckLocalDate = LocalDate.parse(dateToCheck, formatter)

    val currentWeek = currentLocalDate.get(WeekFields.of(Locale.ENGLISH).weekOfYear())
    val weekToCheck = dateToCheckLocalDate.get(WeekFields.of(Locale.ENGLISH).weekOfYear())

    return currentWeek == weekToCheck
}

fun isWithinOneMonth(currentDate: String, dateToCheck: String): Boolean {
    val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
    val currentLocalDate = LocalDate.parse(currentDate, formatter)
    val dateToCheckLocalDate = LocalDate.parse(dateToCheck, formatter)

    val monthsDifference = ChronoUnit.MONTHS.between(currentLocalDate, dateToCheckLocalDate)

    return monthsDifference in 0..1
}

fun isSameMonth(currentDate: String, dateToCheck: String): Boolean {
    val currentMonth = currentDate.substring(0, 7)
    val dateToCheckMonth = dateToCheck.substring(0, 7)
    return currentMonth == dateToCheckMonth
}


fun filterByToday(items: List<Income>): List<Income> {
    return items.filter { income ->
        income.date == now
    }
}

fun filterByTodayAlt(items: List<Debt>): List<Debt> {
    return items.filter { debt ->
        debt.date == now
    }
}

fun filterByWeek(items: List<Income>): List<Income> {
    return items.filter { income ->
        isWithinThisWeek(now,income.date)
    }
}

fun filterByWeekAlt(items: List<Debt>): List<Debt> {
    return items.filter { debt ->
        isWithinThisWeek(now,debt.date)
    }
}
fun filterByMonth(items: List<Income>): List<Income> {
    //TODO: Check Requirements
    return items.filter { income ->
        isSameMonth(now,income.date)
    }
}

fun filterByMonthAlt(items: List<Debt>): List<Debt> {
    //TODO: Check Requirements
    return items.filter { debt ->
        isSameMonth(now,debt.date)
    }
}
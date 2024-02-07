package com.mobile.negocio.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Insights
import androidx.compose.ui.graphics.vector.ImageVector

enum class Views {
    RegisterView,
    DashView,
    DebtsView
}
enum class NavigationBarItems(
    val icon: ImageVector,
    val label: String,
    val route: String
) {
    Registry(label = "Registos", icon = Icons.Default.Create, route = Views.RegisterView.name),
    Dash(label = "Resumo", icon = Icons.Default.Insights, route = Views.DashView.name),
    Debt(label = "Devedores", icon = Icons.Default.Checklist, route = Views.DebtsView.name)
}
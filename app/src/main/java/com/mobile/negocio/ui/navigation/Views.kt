package com.mobile.negocio.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
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
    Dash(label = "Resumo", icon = Icons.Default.Info, route = Views.DashView.name),
    Debt(label = "Devedores", icon = Icons.Default.Delete, route = Views.DebtsView.name)
}
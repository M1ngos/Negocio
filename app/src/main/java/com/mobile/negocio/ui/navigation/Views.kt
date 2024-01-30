package com.mobile.negocio.ui.navigation

import com.mobile.negocio.R

enum class Views {
    RegisterView,
    DashView,
    DebtsView
}

enum class NavigationBarItems(
    val icon: Int,
    val route: String
) {
    Register(icon = R.drawable.plus, route = Views.RegisterView.name),
    Dash(icon =  R.drawable.home, route = Views.DashView.name),
    Debt(icon =  R.drawable.person,  Views.DebtsView.name)
}

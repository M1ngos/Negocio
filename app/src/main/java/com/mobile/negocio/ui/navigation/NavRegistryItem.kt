package com.mobile.negocio.ui.navigation

data class NavRegistryItem(
    val title: String,
)


val navRegistryItems = listOf(
    NavRegistryItem(
        title = "Receitas",
    ),
    NavRegistryItem(
        title = "Despesas",
    ),
)

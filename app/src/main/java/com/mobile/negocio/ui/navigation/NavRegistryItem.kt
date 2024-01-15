package com.mobile.negocio.ui.navigation

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector

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

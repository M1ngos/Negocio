package com.mobile.negocio.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mobile.negocio.ui.views.DashScreen
import com.mobile.negocio.ui.views.DebtsScreen
import com.mobile.negocio.ui.views.RegisterScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                navItems.forEach { navItem ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == navItem.route} == true,
                        onClick = { navController.navigate(navItem.route) {
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                                  },
                        icon = { 
                               Icon(imageVector = navItem.selectedIcon , contentDescription = null)
                        },
                        label = {
                            Text(text = navItem.label)
                        })
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Views.RegisterView.name,
            modifier = Modifier
                .padding(paddingValues)
            ) {
            composable(route = Views.RegisterView.name) {
                RegisterScreen()
            }
            composable(route = Views.DashView.name) {
                DashScreen()
            }
            composable(route = Views.DebtsView.name) {
                DebtsScreen()
            }
        }
    }
}
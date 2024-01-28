package com.mobile.negocio.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import com.mobile.negocio.ui.entries.RegistryEntryDestination
import com.mobile.negocio.ui.entries.RegistryEntryDestinationAlt
import com.mobile.negocio.ui.entries.RegistryEntryScreen
import com.mobile.negocio.ui.entries.RegistryEntryScreenAlt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier
) {
    val navController: NavHostController = rememberNavController()
    var selectedNavItem by remember { mutableStateOf(navItems.first()) }

    Scaffold(
        topBar = {
                 CenterAlignedTopAppBar(
                     title = { Text(text = selectedNavItem.label) },
                     navigationIcon = {
                         IconButton(onClick = { /*TODO*/ }) {
                             Icon(imageVector = Icons.Filled.Search, contentDescription = "")
                         }
                     },
                     actions = {
                         IconButton(onClick = { /*TODO*/ }) {
                             Icon(imageVector = Icons.Filled.Settings, contentDescription = "")

                         }
                     }
                 )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                navItems.forEach { navItem ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == navItem.route} == true,
                        onClick = {
                            selectedNavItem = navItem
                            navController.navigate(navItem.route)
                            {
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
                RegisterScreen(
                    navigateToRegistryEntry = { navController.navigate(RegistryEntryDestination.route) },
                    navigateToRegistryEntryAlt = {  navController.navigate(RegistryEntryDestinationAlt.route) },
                    navigateToUpdateRegistry = {
//                        navController.navigate("${ItemDetailsDestination.route}/${it}")
                    }
                )
            }
            composable(route = RegistryEntryDestination.route) {
                RegistryEntryScreen(
                    navigateBack = { navController.popBackStack()},
                    onNavigateUp = { navController.navigateUp()}
                )
            }

            composable(route = RegistryEntryDestinationAlt.route) {
                RegistryEntryScreenAlt(
                    navigateBack = { navController.popBackStack()},
                    onNavigateUp = { navController.navigateUp()}
                )
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlternativeTopBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Voltar"
                    )
                }
            }
        }
    )
}





package com.mobile.negocio.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
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


/*
  topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Register Screen",
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = {  }) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                        }

                        IconButton(onClick = {  }) {
                            Icon(imageVector = Icons.Filled.Settings, contentDescription = null)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
        },
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController: NavHostController = rememberNavController()
    
    Scaffold(
        topBar = {
                 CenterAlignedTopAppBar(
                     title = { Text(text = "*State*") },
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




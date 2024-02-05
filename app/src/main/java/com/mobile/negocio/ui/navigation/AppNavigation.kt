package com.mobile.negocio.ui.navigation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mobile.negocio.ui.entries.debt.RegistryDetailsDestinationAlt
import com.mobile.negocio.ui.entries.debt.RegistryDetailsScreenAlt
import com.mobile.negocio.ui.entries.debt.RegistryEditDestinationAlt
import com.mobile.negocio.ui.entries.debt.RegistryEditScreenAlt
import com.mobile.negocio.ui.entries.debt.RegistryEntryDestinationAlt
import com.mobile.negocio.ui.entries.debt.RegistryEntryScreenAlt
import com.mobile.negocio.ui.entries.debtors.DebtorDetailsDestination
import com.mobile.negocio.ui.entries.debtors.DebtorDetailsScreen
import com.mobile.negocio.ui.entries.income.RegistryDetailsDestination
import com.mobile.negocio.ui.entries.income.RegistryDetailsScreen
import com.mobile.negocio.ui.entries.income.RegistryEditDestination
import com.mobile.negocio.ui.entries.income.RegistryEditScreen
import com.mobile.negocio.ui.entries.income.RegistryEntryDestination
import com.mobile.negocio.ui.entries.income.RegistryEntryScreen
import com.mobile.negocio.ui.views.DashScreen
import com.mobile.negocio.ui.views.DebtsScreen
import com.mobile.negocio.ui.views.RegisterScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier
) {
    val navController: NavHostController = rememberNavController()
    var selectedNavItem by remember { mutableStateOf(navItems.first()) }
    val coroutineScope = rememberCoroutineScope()
    val animatableScale = remember { Animatable(1f) }


    LaunchedEffect(selectedNavItem) {
        // Trigger animation when the selectedNavItem changes
        coroutineScope.launch {
            animatableScale.animateTo(1.2f, animationSpec = tween(durationMillis = 300))
            animatableScale.animateTo(1f, animationSpec = tween(durationMillis = 300))
        }
    }

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
                        selected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true,
                        onClick = {
                            selectedNavItem = navItem
                            navController.navigate(navItem.route)
                            {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            AnimatedIcon(
                                imageVector = navItem.selectedIcon,
                                contentDescription = null,
                                scale = animatableScale.value
                            )
//                            Icon(imageVector = navItem.selectedIcon, contentDescription = null)
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
                        navController.navigate("${RegistryDetailsDestination.route}/${it}")
                    },
                    navigateToUpdateRegistryAlt = {
                        navController.navigate("${RegistryDetailsDestinationAlt.route}/${it}")
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

            composable(
                route = RegistryDetailsDestination.routeWithArgs,
                arguments = listOf(navArgument(RegistryDetailsDestination.registryIdArg) {
                    type = NavType.IntType
                })
            ) {
                RegistryDetailsScreen(
                    navigateToEditItem = { navController.navigate("${RegistryEditDestination.route}/$it") },
                    navigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = RegistryDetailsDestinationAlt.routeWithArgs,
                arguments = listOf(navArgument(RegistryDetailsDestinationAlt.registryIdArg) {
                    type = NavType.IntType
                })
            ) {
                RegistryDetailsScreenAlt(
                    navigateToEditItem = { navController.navigate("${RegistryEditDestinationAlt.route}/$it") },
                    navigateBack = { navController.popBackStack() }
                )
            }

            composable(route = Views.DashView.name) {
                DashScreen(
                )
            }
            composable(route = Views.DebtsView.name) {
                DebtsScreen(
                    navigateToUpdateRegistry = {
                        navController.navigate("${DebtorDetailsDestination.route}/${it}")
                    },
                )
            }

            composable(
                route = DebtorDetailsDestination.routeWithArgs,
                arguments = listOf(navArgument(DebtorDetailsDestination.registryIdArg) {
                    type = NavType.IntType
                })
            ) {
                DebtorDetailsScreen(
                    navigateToEditItem = { navController.navigate("${RegistryEditDestination.route}/$it") },
                    navigateBack = { navController.navigateUp() })
            }

            composable(
                route = RegistryEditDestination.routeWithArgs,
                arguments = listOf(navArgument(RegistryEditDestination.registryIdArg) {
                    type = NavType.IntType
                })
            ) {
                RegistryEditScreen(
                    navigateBack = { navController.popBackStack() },
                    onNavigateUp = { navController.navigateUp() }
                )
            }

            composable(
                route = RegistryEditDestinationAlt.routeWithArgs,
                arguments = listOf(navArgument(RegistryEditDestinationAlt.registryIdArg) {
                    type = NavType.IntType
                })
            ) {
                RegistryEditScreenAlt(
                    navigateBack = { navController.popBackStack() },
                    onNavigateUp = { navController.navigateUp() }
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlternativeTopBarWithAction(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit
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
        },
        actions = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                actions()
            }
        },
    )
}

@Composable
fun AnimatedIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    scale: Float
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = Modifier.scale(scale)
    )
}





package com.mobile.negocio.ui.navigation

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
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
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Straight
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val navController: NavHostController = rememberNavController()
    var selectedIndex by remember { mutableIntStateOf(0) }
    val navigationBarItems = remember { NavigationBarItems.entries.toTypedArray() }
    val selectedLabel = when (selectedIndex) {
        0 -> "Registos"
        1 -> "Resumo"
        2 -> "Devedores"
        else -> {""}
    }
    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.padding(all = 12.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = selectedLabel) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            Toast.makeText(context, "Ainda em desenvolvimento", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Ainda em desenvolvimento", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(imageVector = Icons.Filled.Settings, contentDescription = "")
                    }
                }
            )
        },
        bottomBar = {
            AnimatedNavigationBar(
                modifier = Modifier.height(64.dp),
                selectedIndex = selectedIndex,
                cornerRadius = shapeCornerRadius(cornerRadius = 34.dp),
                ballAnimation = Straight(tween(300)),
                indentAnimation = Height(tween(300)),
                barColor = MaterialTheme.colorScheme.primary,
                ballColor = MaterialTheme.colorScheme.primary,
                ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                navigationBarItems.forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .noRippleClickable {
                                selectedIndex = item.ordinal
                                navController.navigate(item.route)
                                {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            modifier = Modifier.size(26.dp),
                            imageVector = item.icon,
                            contentDescription = "",
                            tint = if (currentDestination?.hierarchy?.any { it.route == item.route } == true) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.inversePrimary
                        )
                    }
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
                DashScreen()
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
    modifier: Modifier = Modifier
        .height(50.dp),
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

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.noRippleClickable(onClick: () -> Unit):Modifier = composed {
  clickable(
      indication = null,
      interactionSource = remember { MutableInteractionSource()}) {
      onClick()
  }
}





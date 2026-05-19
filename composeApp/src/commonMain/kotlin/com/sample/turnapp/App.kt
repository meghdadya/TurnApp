package com.sample.turnapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sample.turnapp.core.ui.theme.ClyTheme
import com.sample.turnapp.core.ui.theme.TurnAppTheme
import com.sample.turnapp.feature.home.presentation.HomeScreen
import kotlinx.serialization.Serializable
import org.koin.compose.KoinContext



// ---------------------- APP ROOT ----------------------

@Composable
fun App() {
    TurnAppTheme {
        KoinContext {
            AppScaffold()
        }
    }
}

// ---------------------- NAV GRAPH ----------------------

@Composable
fun AppScaffold() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Home,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable<Home> {
                HomeScreen()
            }

            composable<People> {
                Text("People")
            }

            composable<Appointment> {
                Text("Appointment")
            }
        }
    }
}

// ---------------------- ROUTES (TYPED) ----------------------

@Serializable
object Home

@Serializable
object People

@Serializable
object Appointment

// ---------------------- BOTTOM BAR ----------------------

data class BottomItem<T>(
    val route: T,
    val title: String,
    val icon: ImageVector
)

@Composable
fun BottomBar(
    navController: NavController
) {

    val colors = ClyTheme.colors

    val items = listOf(
        BottomItem(Home, "خانه", Icons.Default.Home),
        BottomItem(People, "افراد", Icons.Default.Person),
        BottomItem(Appointment, "نوبت‌ها", Icons.Default.CheckCircle)
    )

    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = backStackEntry?.destination

    NavigationBar(
        containerColor = colors.backgroundNavbar
    ) {

        items.forEach { item ->

            val selected = currentDestination
                ?.hasRoute(item.route::class) == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(Home) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(item.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colors.primaryAction,
                    selectedTextColor = colors.primaryAction,
                    unselectedIconColor = colors.textSecondary,
                    unselectedTextColor = colors.textSecondary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

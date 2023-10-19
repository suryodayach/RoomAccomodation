package com.suryodayach.eventplanner.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.suryodayach.authentication.navigation.loginScreen
import com.suryodayach.authentication.navigation.navigateToLoginScreen
import com.suryodayach.authentication.navigation.navigateToSignUpScreen
import com.suryodayach.authentication.navigation.otpVerificationPattern
import com.suryodayach.authentication.navigation.signUpScreen
import com.suryodayach.eventplanner.navigation.TopLevelDestination
import com.suryodayach.feature.splash.navigation.splashScreen
import com.suryodayach.feature.notes.navigation.groupDetailsPattern
import com.suryodayach.feature.notes.navigation.groupsRoute
import com.suryodayach.feature.notes.navigation.navigateToGroupsGraph
import com.suryodayach.feature.notes.navigation.navigateToNewGroup
import com.suryodayach.feature.notes.navigation.newGroupPattern
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberEventPlannerAppState(
    windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): EventPlannerAppState {
    return remember(
        navController,
        coroutineScope,
        windowSizeClass,
    ) {
        EventPlannerAppState(
            navController,
            coroutineScope,
            windowSizeClass
        )
    }
}


@Stable
class EventPlannerAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            //TODO: add destination route
            groupsRoute -> TopLevelDestination.GROUPS
            else -> null
        }

//    val shouldShowBottomBar: Boolean
//        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val appCurrentRoute: String?
         @Composable
         get() = currentDestination?.route

    val shouldShowBottomBar: Boolean
        @Composable
        get() = when (appCurrentRoute) {
            splashScreen -> false
            loginScreen -> false
            signUpScreen -> false
            otpVerificationPattern -> false
            else -> false
        }

    val shouldShowFloatingActionButton: Boolean
        @Composable
        get() = when (currentDestination?.route) {
            groupsRoute -> true
            else -> false
        }

    val appBarTitle: String
        @Composable get() = when (currentDestination?.route) {
            newGroupPattern -> ""
            groupDetailsPattern -> ""
            else -> ""
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }

            when (topLevelDestination) {
                TopLevelDestination.GROUPS -> navController.navigateToGroupsGraph(topLevelNavOptions)
                else -> {}
            }
        }
    }

    fun navigateToNewGroupScreen() {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
//            launchSingleTop = true
            restoreState = true
        }
        navController.navigateToNewGroup(navOptions)
    }

    fun navigateToRoomsScreen() {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            restoreState = true
        }
        navController.navigateToGroupsGraph(navOptions)
    }

    fun navigateToLoginScreen() {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        navController.navigateToLoginScreen(navOptions)
    }

    fun navigateToSignUpScreen() {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        navController.navigateToSignUpScreen(navOptions)
    }
}
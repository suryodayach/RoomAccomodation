package com.suryodayach.eventplanner.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.suryodayach.authentication.navigation.loginGraph
import com.suryodayach.authentication.navigation.loginGraphRoute
import com.suryodayach.authentication.navigation.navigateToLoginScreen
import com.suryodayach.authentication.navigation.navigateToOtpVerificationScreen
import com.suryodayach.authentication.navigation.navigateToSignUpScreen
import com.suryodayach.eventplanner.ui.BRUAppState
import com.suryodayach.feature.splash.SplashScreen
import com.suryodayach.feature.splash.navigation.splashScreen
import com.suryodayach.feature.notes.navigation.groupDetailsScreen
import com.suryodayach.feature.notes.navigation.navigateToGroupDetails
import com.suryodayach.feature.notes.navigation.navigateToGroupsGraph
import com.suryodayach.feature.notes.navigation.navigateToNewGroup
import com.suryodayach.feature.notes.navigation.newGroupScreen
import com.suryodayach.feature.notes.navigation.splitGroupsGraph

@Composable
fun BRUniversityNavHost(
    appState: BRUAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = loginGraphRoute,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = splashScreen) {
            SplashScreen()
        }
        splitGroupsGraph(
            onItemClick = {
                navController.navigateToGroupDetails(it)
            },
            nestedGraphs = {
                newGroupScreen { }
                groupDetailsScreen { }
            },
            onRoomBooked = {
                navController.navigateToNewGroup()
            }
        )
        loginGraph(
            onSignUpClicked = {
                navController.navigateToSignUpScreen()
            },
            onLoginClicked = {
                navController.navigateToLoginScreen()
            },
            onOtpSentSignUp = { email, password ->
                navController.navigateToOtpVerificationScreen(email, password)
            },
            onLoggedIn = {
                         navController.navigateToGroupsGraph()
            },
            onShowSnackbar = onShowSnackbar,
        ) { }
    }
}
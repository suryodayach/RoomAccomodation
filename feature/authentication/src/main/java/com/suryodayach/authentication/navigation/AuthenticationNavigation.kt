package com.suryodayach.authentication.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.suryodayach.authentication.ui.login.LoginRoute
import com.suryodayach.authentication.ui.otpverification.OtpVerificationRoute
import com.suryodayach.authentication.ui.signup.SignUpRoute

const val loginGraphRoute = "login_graph_route"
const val loginScreen = "login_screen"
const val signUpScreen = "signup_screen"
const val EMAIL = "email"
const val PASSWORD = "password"
const val otpVerificationScreen = "otp_verification_screen"
const val otpVerificationPattern = "$otpVerificationScreen/{$EMAIL}/{$PASSWORD}"

fun NavController.navigateToLoginScreen(navOptions: NavOptions? = null) {
    this.navigate(loginScreen, navOptions)
}

fun NavGraphBuilder.loginGraph(
    onSignUpClicked: () -> Unit,
    onLoginClicked: () -> Unit,
    onLoggedIn: () -> Unit,
    onOtpSentSignUp: (String, String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation(
        route = loginGraphRoute,
        startDestination = loginScreen,
    ) {
        composable(route = loginScreen) {
            LoginRoute(
                onSignUpClicked = onSignUpClicked,
                onLoggedIn = onLoggedIn,
                onOtpSent = { email, password ->
                    onOtpSentSignUp(email, password)
                },
                onShowSnackbar = onShowSnackbar
            )
        }
        composable(route = signUpScreen) {
            SignUpRoute(
                onLoginClicked = {
                    onLoginClicked.invoke()
                },
                onOtpSent = { email, password ->
                    onOtpSentSignUp(email, password)
                },
                onShowSnackbar = onShowSnackbar
            )
        }
        composable(
            route = otpVerificationPattern,
            arguments = listOf(
                navArgument(EMAIL) { type = NavType.StringType },
                navArgument(PASSWORD) { type = NavType.StringType }
            )
        ) {
            OtpVerificationRoute(
                onLoggedIn = onLoggedIn,
                onShowSnackbar = onShowSnackbar,
            )
        }
        nestedGraphs()
    }
}

fun NavController.navigateToSignUpScreen(navOptions: NavOptions? = null) {
    this.navigate(signUpScreen, navOptions)
}

fun NavController.navigateToOtpVerificationScreen(
    email: String,
    password: String,
    navOptions: NavOptions? = null
) {
    this.navigate("$otpVerificationScreen/$email/$password", navOptions)
}
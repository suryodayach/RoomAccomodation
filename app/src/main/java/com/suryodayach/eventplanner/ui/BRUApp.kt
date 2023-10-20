package com.suryodayach.eventplanner.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.suryodayach.authentication.navigation.loginGraphRoute
import com.suryodayach.core.common.R.*
import com.suryodayach.core.designsystem.EventPlannerNavigationBar
import com.suryodayach.core.designsystem.EventPlannerNavigationBarItem
import com.suryodayach.core.designsystem.EventPlannerTopAppBar
import com.suryodayach.eventplanner.navigation.BRUniversityNavHost
import com.suryodayach.eventplanner.navigation.TopLevelDestination
import com.suryodayach.eventplanner.MainActivityUiState
import com.suryodayach.feature.splash.navigation.splashScreen
import com.suryodayach.feature.notes.navigation.groupsGraphRoutePattern

@OptIn(
    ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)
@Composable
fun BRUApp(
    windowSizeClass: WindowSizeClass,
    appState: BRUAppState = rememberEventPlannerAppState(windowSizeClass = windowSizeClass),
    uiState: MainActivityUiState
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        },
//        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                EventPlannerBottomBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination,
                    modifier = Modifier.testTag("EventPlannerBottomBar")
                )
            }
        },
        floatingActionButton = {
//            if (appState.appCurrentRoute == groupsRoute) {
//                EventPlannerFloatingActionButton(onClick = appState::navigateToNewGroupScreen)
//            }
        }
    ) { padding ->
        Row(
            Modifier
                .fillMaxSize()
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal
                    ),
                ),
        ) {
            Column(Modifier.fillMaxSize()) {
                val destination = appState.currentTopLevelDestination
                if (destination != null) {
                    EventPlannerTopAppBar(
                        titleRes = destination.titleText,
                        actionIcon = drawable.ic_person_24,
                        actionIconContentDescription = "Notifications",
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.Transparent,
                        ),
                        onActionClick = appState::navigateToLogoutScreen,
                    )
                } else {
                    EventPlannerTopAppBar(
                        titleRes = appState.appBarTitle,
                        navigationIcon = Icons.Default.ArrowBack,
                        navigationIconContentDescription = "Back Button",
                        onNavigationClick = appState::navigateToPreviousScreen
                    )
//                    if (appState.appCurrentRoute == newGroupPattern) {
//                        EventPlannerTopAppBar(
//                            titleRes = appState.appBarTitle,
//                            navigationIcon = Icons.Default.ArrowBack,
//                            navigationIconContentDescription = "Back Button",
//                        )
//                    }
                }

                val startDestination = when (uiState) {
                    MainActivityUiState.Loading -> splashScreen
                    MainActivityUiState.LoggedIn -> groupsGraphRoutePattern
                    MainActivityUiState.LoggedOut -> loginGraphRoute
                    is MainActivityUiState.LoginError -> loginGraphRoute
                }

                BRUniversityNavHost(
                    appState = appState, onShowSnackbar = { message, action ->
                        snackbarHostState.showSnackbar(
                            message = message,
                            actionLabel = action,
                            duration = SnackbarDuration.Short,
                        ) == SnackbarResult.ActionPerformed
                    },
                    startDestination = startDestination
                )
            }
        }
    }
}


@Composable
private fun EventPlannerBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    EventPlannerNavigationBar(modifier = modifier) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            EventPlannerNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(imageVector = destination.unselectedIcon, contentDescription = null)
                },
                selectedIcon = {
                    Icon(
                        imageVector = destination.selectedIcon, contentDescription = null,
                    )
                },
                label = { Text(text = destination.iconText) },
                modifier = Modifier
            )
        }
    }
}


private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false
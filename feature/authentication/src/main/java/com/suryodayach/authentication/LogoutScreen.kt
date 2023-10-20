package com.suryodayach.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.suryodayach.authentication.ui.LogoutUiState
import com.suryodayach.authentication.ui.LogoutViewModel
import com.suryodayach.core.designsystem.EventPlannerPrimaryButton

@Composable
fun LogoutScreen(
    onLoggedOut: () -> Unit,
    viewModel: LogoutViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LogoutScreen(
        uiState = uiState,
        onLogoutClicked = viewModel::logoutUser,
        onLoggedOut = onLoggedOut
    )
}

@Composable
internal fun LogoutScreen(
    uiState: LogoutUiState,
    onLoggedOut: () -> Unit,
    onLogoutClicked: () -> Unit,
) {

    if (uiState is LogoutUiState.LoggedOut) {
        LaunchedEffect(true) {
            onLoggedOut.invoke()
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
    ) {
        EventPlannerPrimaryButton(
            onClick = onLogoutClicked,
            text = "Logout"
        )
    }
}

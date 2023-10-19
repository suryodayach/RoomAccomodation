package com.suryodayach.authentication.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.suryodayach.core.common.R
import com.suryodayach.core.designsystem.EventPlannerLinearProgressIndicator
import com.suryodayach.core.designsystem.EventPlannerPrimaryButton

@Composable
fun LoginRoute(
    onSignUpClicked: () -> Unit,
    onLoggedIn: () -> Unit,
    onOtpSent: (String, String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LoginScreen(
        uiState = uiState,
        onShowSnackbar = onShowSnackbar,
        onSignUpClicked = onSignUpClicked,
        onLoginButtonClicked = viewModel::loginUser,
        onLoggedIn = onLoggedIn,
        onVerifyClicked = viewModel::sendOtp,
        onOtpSent = onOtpSent,
        email = viewModel.email,
        password = viewModel.password,
        isEmailValid = viewModel.isEmailValid,
        isPasswordValid = viewModel.isPasswordValid,
        emailErrorMsg = viewModel.emailErrorMsg,
        passwordErrorMsg = viewModel.passwordErrorMsg,
        showPassword = viewModel.showPassword,
        onEmailChanged = { email ->
            viewModel.updateEmail(email)
        },
        onPasswordChanged = { password ->
            viewModel.updatePassword(password)
        },
        onShowPasswordClicked = viewModel::toggleShowPassword,
        modifier = modifier,
    )
}

@Composable
internal fun LoginScreen(
    uiState: LoginUiState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onSignUpClicked: () -> Unit,
    onLoginButtonClicked: () -> Unit,
    onLoggedIn: () -> Unit,
    onVerifyClicked: () -> Unit,
    onOtpSent: (String, String) -> Unit,
    email: String,
    password: String,
    isEmailValid: Boolean,
    isPasswordValid: Boolean,
    emailErrorMsg: String,
    passwordErrorMsg: String,
    showPassword: Boolean,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onShowPasswordClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState is LoginUiState.LoginError) {
        LaunchedEffect(uiState) {
            onShowSnackbar(uiState.error ?: "Error Logging In!", "Okay")
        }
    }

    if (uiState is LoginUiState.UserNotConfirmed) {
        LaunchedEffect(true) {
            val snackbarResult = onShowSnackbar(
                "User is not confirmed. Please verify your email!",
                "Verify"
            )
            if (snackbarResult) {
                onVerifyClicked.invoke()
            }
        }
    }

    if (uiState is LoginUiState.LoginSuccess) {
        LaunchedEffect(true) {
            onLoggedIn.invoke()
        }
    }

    if (uiState is LoginUiState.OtpSent) {
        LaunchedEffect(true) {
            onOtpSent(email, password)
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 12.dp)
    ) {
        val focusManager = LocalFocusManager.current
        Column {
            Text(
                "Log in",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(56.dp))
            Text("Email", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { onEmailChanged(it) },
                placeholder = { Text("example@gmail.com") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                    focusedContainerColor = MaterialTheme.colorScheme.onPrimary
                ),
                isError = !isEmailValid,
                supportingText = {
                    if (!isEmailValid) {
                        Text(
                            text = emailErrorMsg,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(22.dp))
            Text("Password", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { onPasswordChanged(it) },
                placeholder = {
                    Text("password")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                    focusedContainerColor = MaterialTheme.colorScheme.onPrimary
                ),
                isError = !isPasswordValid,
                supportingText = {
                    if (!isPasswordValid) {
                        Text(
                            text = passwordErrorMsg,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                trailingIcon = {
                    val icon = if (showPassword) {
                        R.drawable.fairshare_ui_see_icon
                    } else {
                        R.drawable.fairshare_ui_hidden_icon
                    }

                    IconButton(onClick = onShowPasswordClicked) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = "Password Visibility"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onLoginButtonClicked.invoke()
                    }),
                singleLine = true,
                visualTransformation =
                if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Forgot password?",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(38.dp))
            EventPlannerPrimaryButton(
                onClick = { onLoginButtonClicked.invoke() },
                text = "Log in"
            )

            if (uiState is LoginUiState.LoggingIn) {
                EventPlannerLinearProgressIndicator()
            }

            Spacer(modifier = Modifier.height(38.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.fairshare_ui_line),
                    contentDescription = "Line",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(13.dp))
                Text(
                    text = "Or Login with",
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.width(13.dp))
                Icon(
                    painter = painterResource(id = R.drawable.fairshare_ui_line),
                    contentDescription = "Line",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(22.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    ),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fairshare_ui_facebook_icon),
                        contentDescription = "Login with Facebook",
                        modifier = Modifier.padding(6.dp)
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                OutlinedButton(
                    onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    ),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fairshare_ui_google_icon),
                        contentDescription = "Login with Facebook",
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(54.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Don't have an account?",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "Sign up", color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                        .copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.clickable {
                        onSignUpClicked.invoke()
                    }
                )
            }
        }
    }
}
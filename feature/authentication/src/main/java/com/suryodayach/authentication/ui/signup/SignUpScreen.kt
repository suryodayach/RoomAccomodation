package com.suryodayach.authentication.ui.signup

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.suryodayach.core.common.R
import com.suryodayach.core.designsystem.EventPlannerLinearProgressIndicator
import com.suryodayach.core.designsystem.EventPlannerPrimaryButton

@Composable
fun SignUpRoute(
    onLoginClicked: () -> Unit,
    onOtpSent: (String, String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SignUpScreen(
        uiState = uiState,
        onShowSnackbar = onShowSnackbar,
        onLoginClicked = onLoginClicked,
        onSignUpButtonClicked = viewModel::registerUser,
        onOtpSent = onOtpSent,
        email = viewModel.email,
        password = viewModel.password,
        confirmPassword = viewModel.confirmPassword,
        isEmailValid = viewModel.isEmailValid,
        isPasswordValid = viewModel.isPasswordValid,
        isConfirmPasswordValid = viewModel.isConfirmPasswordValid,
        emailErrorMsg = viewModel.emailErrorMsg,
        passwordErrorMsg = viewModel.passwordErrorMsg,
        confirmPasswordErrorMsg = viewModel.confirmPasswordErrorMsg,
        showPassword = viewModel.showPassword,
        showConfirmPassword = viewModel.showConfirmPassword,
        onEmailChanged = { email ->
            viewModel.updateEmail(email)
        },
        onPasswordChanged = { password ->
            viewModel.updatePassword(password)

        },
        onConfirmPasswordChanged = { confirmPassword ->
            viewModel.updateConfirmPassword(confirmPassword)
        },
        onShowPasswordClicked = viewModel::toggleShowPassword,
        onShowConfirmPasswordClicked = viewModel::toggleShowConfirmPassword,
        modifier = modifier,
    )
}

@Composable
internal fun SignUpScreen(
    uiState: SignUpUiState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onLoginClicked: () -> Unit,
    onSignUpButtonClicked: () -> Unit,
    onOtpSent: (String, String) -> Unit,
    email: String,
    password: String,
    confirmPassword: String,
    isEmailValid: Boolean,
    isPasswordValid: Boolean,
    isConfirmPasswordValid: Boolean,
    emailErrorMsg: String,
    passwordErrorMsg: String,
    confirmPasswordErrorMsg: String,
    showPassword: Boolean,
    showConfirmPassword: Boolean,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onShowPasswordClicked: () -> Unit,
    onShowConfirmPasswordClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {

    if (uiState is SignUpUiState.SignUpError) {
        LaunchedEffect(uiState) {
            onShowSnackbar(uiState.error ?: "Error Logging In!", "Okay")
        }
    }

    if (uiState is SignUpUiState.OtpSent) {
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
        Column {
            val focusManager = LocalFocusManager.current
            Text(
                "Sign up",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(56.dp))
            Text("Email", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = {
                    onEmailChanged(it)
                },
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
            Text("Create a password", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { onPasswordChanged(it) },
                placeholder = { Text("must be 8 characters") },
                modifier = Modifier.fillMaxWidth(),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                singleLine = true,
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
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.height(22.dp))
            Text("Confirm password", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { onConfirmPasswordChanged(it) },
                placeholder = { Text("repeat password") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                    focusedContainerColor = MaterialTheme.colorScheme.onPrimary
                ),
                isError = !isConfirmPasswordValid,
                supportingText = {
                    if (!isConfirmPasswordValid) {
                        Text(
                            text = confirmPasswordErrorMsg,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onSignUpButtonClicked.invoke()
                    }),
                singleLine = true,
                trailingIcon = {
                    val icon = if (showConfirmPassword) {
                        R.drawable.fairshare_ui_see_icon
                    } else {
                        R.drawable.fairshare_ui_hidden_icon
                    }

                    IconButton(onClick = onShowConfirmPasswordClicked) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = "Password Visibility"
                        )
                    }
                },
                visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.height(38.dp))
            EventPlannerPrimaryButton(
                onClick = { onSignUpButtonClicked.invoke() },
                text = "Create and Log in"
            )

            if (uiState is SignUpUiState.SigningUp) {
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
                Text(text = "Or Register with", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.width(13.dp))
                Icon(
                    painter = painterResource(id = R.drawable.fairshare_ui_line),
                    contentDescription = "Line",
                    tint = MaterialTheme.colorScheme.outline
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
                    )
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
                    )
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
                Text(text = "Already have an account?", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "Log in", color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                        .copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.clickable {
                        onLoginClicked.invoke()
                    }
                )
            }
        }
    }
}
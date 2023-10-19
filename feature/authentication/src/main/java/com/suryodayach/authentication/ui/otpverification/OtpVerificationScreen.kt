package com.suryodayach.authentication.ui.otpverification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.suryodayach.core.designsystem.EventPlannerLinearProgressIndicator
import com.suryodayach.core.designsystem.EventPlannerPrimaryButton

@Composable
fun OtpVerificationRoute(
    onLoggedIn: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: OtpVerificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OtpVerificationScreen(
        uiState = uiState,
        verifyOtpValue = viewModel.verifyOtpValue.value,
        onShowSnackbar = onShowSnackbar,
        onVerifyButtonClicked = viewModel::onVerifyOtpClicked,
        onLoggedIn = onLoggedIn,
        otp = viewModel.otp,
        onOtpChanged = { otp ->
            viewModel.updateOtp(otp)
        },
        onResendClicked = viewModel::onResendOtpClicked,
        modifier = modifier,
    )
}

@Composable
internal fun OtpVerificationScreen(
    uiState: OtpVerificationUiState,
    verifyOtpValue: VerifyOtpValue,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onVerifyButtonClicked: () -> Unit,
    onLoggedIn: () -> Unit,
    onResendClicked: () -> Unit,
    otp: String,
    onOtpChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState is OtpVerificationUiState.OtpVerificationError) {
        LaunchedEffect(uiState) {
            onShowSnackbar(uiState.error ?: "Otp Verification Error!", null)
        }
    }

    if (uiState is OtpVerificationUiState.OtpVerified) {
        LaunchedEffect(uiState) {
            onLoggedIn.invoke()
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 12.dp)
    ) {
        Column {
            Text(
                text = "Please check your email",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(56.dp))
            Text(
                text = "We've sent a code to ${verifyOtpValue.email}",
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(38.dp))
            PinView(pinText = otp, onPinTextChange = { onOtpChanged(it) }, containerSize = 48.dp)
            Spacer(modifier = Modifier.height(38.dp))
            EventPlannerPrimaryButton(onClick = onVerifyButtonClicked, text = "Verify")
            if (uiState is OtpVerificationUiState.OtpVerifying) {
                EventPlannerLinearProgressIndicator()
            }
            Spacer(modifier = Modifier.height(67.dp))
            ResendOtpView(verifyOtpValue, onResendClicked)
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun ResendOtpView(
    verifyOtpValue: VerifyOtpValue,
    onResendClicked: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (verifyOtpValue.countdownTime.isNullOrEmpty()) {
            Text(text = "I didn't receive a code", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(25.dp))
            Text(
                text = "Resend",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                ),
                modifier = Modifier.clickable {
                    onResendClicked.invoke()
                }
            )
        } else {
            Text(text = "Send code again", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(25.dp))
            Text(
                text = verifyOtpValue.countdownTime,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                )
            )
        }
    }
}

const val PIN_VIEW_TYPE_UNDERLINE = 0
const val PIN_VIEW_TYPE_BORDER = 1

@Composable
fun PinView(
    pinText: String,
    onPinTextChange: (String) -> Unit,
    digitColor: Color = MaterialTheme.colorScheme.onBackground,
    digitSize: TextUnit = 16.sp,
    containerSize: Dp = digitSize.value.dp * 2,
    digitCount: Int = 6,
    type: Int = PIN_VIEW_TYPE_BORDER,
) {
    BasicTextField(value = pinText,
        onValueChange = onPinTextChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(digitCount) { index ->
                    DigitView(
                        index = index,
                        pinText = pinText,
                        digitColor = digitColor,
                        digitSize = digitSize,
                        containerSize = containerSize,
                        type = type
                    )
                }
            }
        }
    )
}

@Composable
private fun DigitView(
    index: Int,
    pinText: String,
    digitColor: Color,
    digitSize: TextUnit,
    containerSize: Dp,
    type: Int = PIN_VIEW_TYPE_BORDER,
) {
    val modifier = if (type == PIN_VIEW_TYPE_BORDER) {
        Modifier
            .width(containerSize)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(14.dp)
    } else Modifier.width(containerSize)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = if (index >= pinText.length) "" else pinText[index].toString(),
            color = digitColor,
            modifier = modifier,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = digitSize,
            textAlign = TextAlign.Center
        )
        if (type == PIN_VIEW_TYPE_UNDERLINE) {
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .background(digitColor)
                    .height(1.dp)
                    .width(containerSize)
            )
        }
    }
}
package com.suryodayach.feature.notes

import android.widget.RatingBar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.suryodayach.core.designsystem.EventPlannerPrimaryButton
import kotlin.math.ceil
import kotlin.math.floor

@Composable
internal fun RoomDetailsRoute(
    modifier: Modifier = Modifier,
    onRoomBooked: () -> Unit,
    viewModel: RoomDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RoomDetailsScreen(
        modifier = modifier,
        uiState = uiState,
        noOfRoom = viewModel.noOfRoom,
        onValueChange = { value ->
            viewModel.updateRooms(value)
        },
        onBookClicked = { roomId, noOfRoom ->
            viewModel.bookRoom(roomId, noOfRoom)
        },
        onRoomBooked = onRoomBooked
    )
}

@Composable
internal fun RoomDetailsScreen(
    modifier: Modifier,
    uiState: RoomDetailsUiState,
    noOfRoom: Int,
    onValueChange: (Int) -> Unit,
    onBookClicked: (String, Int) -> Unit,
    onRoomBooked: () -> Unit
) {

    if (uiState is RoomDetailsUiState.Success) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = uiState.room.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uiState.room.photos.first())
                    .build(),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))
            RatingBar(
                rating = uiState.room.rating
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Address", style = MaterialTheme.typography.titleMedium)
            Text(text = uiState.room.location, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Room Type", style = MaterialTheme.typography.titleMedium)
            Text(text = uiState.room.roomType, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Room Type", style = MaterialTheme.typography.titleMedium)
            Text(text = uiState.room.roomType, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Amenities", style = MaterialTheme.typography.titleMedium)
            uiState.room.facilities.forEach {facility ->
                Text(text = "- $facility", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Available Rooms: ${uiState.room.availability}", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(12.dp))
            PlusMinusRow(
                value = noOfRoom,
                onValueChange = onValueChange,
                uiState.room.availability
            )

            Spacer(modifier = Modifier.height(12.dp))
            EventPlannerPrimaryButton(
                onClick = {
                    onBookClicked(uiState.room.roomId, noOfRoom)
                },
                text = "Book Room",
                modifier = Modifier.padding(bottom = 92.dp)
            )
        }
    } else if (uiState is RoomDetailsUiState.Loading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                Modifier.semantics {
                    this.contentDescription = "Loading"
                }
            )
        }
    }

    if (uiState is RoomDetailsUiState.Booked) {
        LaunchedEffect(true) {
            onRoomBooked.invoke()
        }
    }
}


@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    starsColor: Color = Color.Yellow,
) {
    val filledStars = floor(rating).toInt()
    val unfilledStars = (stars - ceil(rating)).toInt()
    val halfStar = !(rating.rem(1).equals(0.0))
    Row(modifier = modifier) {
        repeat(filledStars) {
            Icon(imageVector = Icons.Outlined.Star, contentDescription = null, tint = starsColor)
        }
        if (halfStar) {
            Icon(
                imageVector = Icons.Outlined.StarHalf,
                contentDescription = null,
                tint = starsColor
            )
        }
        repeat(unfilledStars) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = starsColor
            )
        }
    }
}

@Composable
fun PlusMinusRow(
    value: Int,
    onValueChange: (Int) -> Unit,
    availableRoom: Int = 0
) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Minus Icon
        Icon(
            imageVector = Icons.Default.Remove,
            contentDescription = null,
            modifier = Modifier.clickable {
                if (value > 0)
                    onValueChange(value - 1)
            }
        )

        // Editable Text
        TextField(
            enabled = false,
            value = value.toString(),
            onValueChange = {
                // Handle the value change and update the state
                val newValue = it.toIntOrNull() ?: 0
                onValueChange(newValue)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                capitalization = KeyboardCapitalization.None
            ),
            textStyle = androidx.compose.ui.text.TextStyle.Default.copy(color = Color.Black),
            modifier = Modifier.weight(0.3f).padding(start = 8.dp, end = 8.dp)
        )

        // Plus Icon
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.clickable {
                if (value < availableRoom)
                    onValueChange(value + 1)
            }
        )

    }
}


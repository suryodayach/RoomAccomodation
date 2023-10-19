package com.suryodayach.feature.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RoomsListRoute(
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit,
    viewModel: RoomsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RoomsListScreen(
        modifier = modifier,
        uiState = uiState,
        onItemClick = onItemClick
    )
}


@Composable
internal fun RoomsListScreen(
    modifier: Modifier = Modifier,
    uiState: RoomsUiState,
    onItemClick: (String) -> Unit
) {

    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()) {
        if (uiState is RoomsUiState.Success) {
            LazyColumn(
                contentPadding = PaddingValues(
                    vertical = 8.dp,
                    horizontal = 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.rooms) { room ->
                    RoomItem(
                        roomId = room.roomId,
                        photo = room.photos.first(),
                        name = room.name,
                        location = room.location,
                        rating = room.rating,
                        onClick = { roomId -> onItemClick(roomId) }
                    )
                }
            }
        }

        if (uiState is RoomsUiState.Loading)
            CircularProgressIndicator(
                Modifier.semantics {
                    this.contentDescription = "Loading"
                }
            )

        if (uiState is RoomsUiState.Error)
            Text(text = uiState.error.orEmpty())
    }
}
package com.suryodayach.feature.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun RoomItem(
    roomId: String,
    photo: String,
    name: String,
    location: String,
    rating: Double,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .clickable {
                onClick.invoke(roomId)
            }
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photo)
                    .build(),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(
                modifier = Modifier.width(12.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = name, style = MaterialTheme.typography.titleMedium)
                Text(text = "Address: $location", style = MaterialTheme.typography.bodySmall)
                Text(text = "Rating: $rating/5", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
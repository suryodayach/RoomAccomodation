package com.suryodayach.feature.notes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.suryodayach.core.ui.EventPlannerTheme

@Composable
fun SplitGroupItem(
    title: String,
    description: String,
    category: String,
    expenseMessage: String,
    members: List<String>,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .clickable {
                onClick.invoke(title)
            }
            .padding(12.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fnovacatclinic.com%2Fwp-content%2Fuploads%2F2019%2F03%2FIMG_8668.jpg&f=1&nofb=1&ipt=5048d49d4b005112c3c82b54912e85d2b0bc9ddd2e3070c59e871fd5bae82e38&ipo=images")
                    .build(),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = title, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    AssistChip(
                        onClick = { /*TODO*/ },
                        label = {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        enabled = false,
                        shape = CircleShape,
                        border = null,
                        colors = AssistChipDefaults
                            .assistChipColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = MaterialTheme.colorScheme.primary,
                                disabledLabelColor = MaterialTheme.colorScheme.onPrimary,
                                disabledLeadingIconContentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Home,
                                modifier = Modifier.size(9.dp),
                                contentDescription = "Home Icon"
                            )
                        },
                        modifier = Modifier.height(16.dp)
                    )

                }
                Text(text = description, style = MaterialTheme.typography.bodySmall)
                Text(text = expenseMessage, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More Options")
            }
        }
    }
}

@Preview
@Composable
private fun SplitGroupCardPreview() {
    EventPlannerTheme {
        Surface {
            SplitGroupItem(
                title = "Trip to Goa",
                description = "Tickets, food and stay expenses",
                category = "Travel",
                expenseMessage = "Rs 5,920 paid by Ally",
                members = listOf("Suryodaya", "Kartik"),
                {}
            )
        }
    }
}
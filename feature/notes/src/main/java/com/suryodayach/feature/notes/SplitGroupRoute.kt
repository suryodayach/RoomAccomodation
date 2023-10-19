package com.suryodayach.feature.notes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SplitGroupRoute(
    modifier: Modifier = Modifier,
    onGroupItemClick: (String) -> Unit,
) {
    SplitGroupScreen(modifier = modifier, onGroupItemClick)
}


@Composable
internal fun SplitGroupScreen(
    modifier: Modifier = Modifier,
    onGroupItemClick: (String) -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = 12.dp)
    ) {

    }
}
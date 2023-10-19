package com.suryodayach.eventplanner.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String,
    val titleText: String,
) {
    GROUPS(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Default.Home,
        iconText = "Home",
        titleText = "Rooms",
    ),
    FRIENDS(
        selectedIcon = Icons.Filled.List,
        unselectedIcon = Icons.Default.List,
        iconText = "Friends",
        titleText = "Friends",
    ),
    PROFILE(
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Default.Person,
        iconText = "Account",
        titleText = "Account",
    )
}
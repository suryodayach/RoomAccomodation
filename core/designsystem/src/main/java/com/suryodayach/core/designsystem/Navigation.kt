package com.suryodayach.core.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.EventPlannerNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: @Composable () -> Unit = icon,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
) {
    NavigationBarItem(
        selected = selected, onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = EventPlannerNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = EventPlannerNavigationDefaults.navigationContentColor(),
            selectedTextColor = EventPlannerNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = EventPlannerNavigationDefaults.navigationContentColor(),
            indicatorColor = EventPlannerNavigationDefaults.navigationIndicatorColor(),
        ),
    )
}


@Composable
fun EventPlannerNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier.background(color = MaterialTheme.colorScheme.primary),
        containerColor = EventPlannerNavigationDefaults.navigationContainerColor(),
        contentColor = EventPlannerNavigationDefaults.navigationContentColor(),
        tonalElevation = 4.dp,
        content = content
    )
}


object EventPlannerNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer

    @Composable
    fun navigationContainerColor() = MaterialTheme.colorScheme.surface
}
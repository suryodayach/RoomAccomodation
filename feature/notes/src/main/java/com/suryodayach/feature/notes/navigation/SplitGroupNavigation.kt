package com.suryodayach.feature.notes.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.suryodayach.feature.notes.RoomDetailsRoute
import com.suryodayach.feature.notes.NewGroupScreen
import com.suryodayach.feature.notes.RoomsListRoute

const val groupsGraphRoutePattern = "split_groups_graph"
const val newGroupPattern = "new_group_screen"
const val groupsRoute = "split_groups_route"
const val groupDetailsPattern = "group_details_screen"
const val ROOM_ID = "room_id"
const val roomDetailsPattern = "$groupDetailsPattern/{$ROOM_ID}"

fun NavController.navigateToGroupsGraph(navOptions: NavOptions? = null){
    this.navigate(groupsGraphRoutePattern, navOptions)
}

fun NavGraphBuilder.splitGroupsGraph(
    onItemClick: (String) -> Unit,
    onRoomBooked: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation(
        route = groupsGraphRoutePattern,
        startDestination = groupsRoute,
    ) {
        composable(route = groupsRoute) {
//            SplitGroupRoute(
//                onGroupItemClick = { onGroupClick(it) }
//            )
            RoomsListRoute(
                onItemClick = { onItemClick(it) }
            )
        }
        composable(
            route = roomDetailsPattern,
            arguments = listOf(
                navArgument(ROOM_ID) { type = NavType.StringType }
            )
        ) {
            RoomDetailsRoute(
                onRoomBooked = onRoomBooked
            )
        }
        nestedGraphs()
    }
}


fun NavController.navigateToNewGroup(navOptions: NavOptions? = null) {
    this.navigate(newGroupPattern, navOptions)
}

fun NavController.navigateToGroupDetails(
    roomId: String,
    navOptions: NavOptions? = null
) {
    this.navigate("$groupDetailsPattern/$roomId", navOptions)
}

fun NavGraphBuilder.newGroupScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = newGroupPattern,
    ) {
        NewGroupScreen()
    }
}


fun NavGraphBuilder.groupDetailsScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = groupDetailsPattern,
    ) {
        RoomDetailsRoute(onRoomBooked = onBackClick)
    }
}
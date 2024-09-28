package com.example.mymusicplayer.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

data class SwipeAction<T>(
    val direction: DismissDirection,
    val action: (T) -> Unit,
    val icon: @Composable () -> Unit
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> SwipeContainer(
    item: T,
    action: SwipeAction<T>,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {
    var isSwiped by remember { mutableStateOf(false) }
    val state = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToEnd) {
                isSwiped = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(isSwiped) {
        if (isSwiped) {
            delay(animationDuration.toLong())
            action.action(item)
            state.reset()
            isSwiped = false
        }
    }

    SwipeToDismiss(
        state = state,
        background = {
            SwipeBackground(
                swipeDismissState = state,
                direction = action.direction,
                icon = action.icon
            )
        },
        dismissContent = { content(item) },
        directions = setOf(action.direction)
    )
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeBackground(
    swipeDismissState: DismissState,
    direction: DismissDirection = DismissDirection.StartToEnd,
    icon: @Composable () -> Unit = {},
    backgroundColor: Color = MaterialTheme.colorScheme.primary
) {
    val color = if (swipeDismissState.dismissDirection == direction)
        backgroundColor
    else
        Color.Transparent

    Box(
        contentAlignment = when (direction) {
            DismissDirection.StartToEnd -> Alignment.CenterStart
            DismissDirection.EndToStart -> Alignment.CenterEnd
        },
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp)
    ) {
        icon()
    }
}
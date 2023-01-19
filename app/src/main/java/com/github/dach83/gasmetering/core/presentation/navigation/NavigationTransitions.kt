package com.github.dach83.gasmetering.core.presentation.navigation

import androidx.compose.animation.*
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle

@OptIn(ExperimentalAnimationApi::class)
object NavigationTransitions : DestinationStyle.Animated {

    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition() =
        slideInHorizontally { it }

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition() =
        slideOutHorizontally { -it }

    override fun AnimatedContentScope<NavBackStackEntry>.popEnterTransition() =
        slideInHorizontally { -it }

    override fun AnimatedContentScope<NavBackStackEntry>.popExitTransition() =
        slideOutHorizontally { it }
}

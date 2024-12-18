package ir.mytodo.presentation.navGraphs

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT ,
        startDestination = Graph.HOME
    ) {
        homeNavGraph(navController = navController)

    }
}

fun NavGraphBuilder.CustomComposable(
    route : String ,
    content : @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route ,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left , tween(500)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left , tween(500)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right , tween(500)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right , tween(500)) }
    ) {
        content(it)
    }
}



object Graph{
    const val ROOT = "root_graph"
    const val HOME = "home_graph"
}

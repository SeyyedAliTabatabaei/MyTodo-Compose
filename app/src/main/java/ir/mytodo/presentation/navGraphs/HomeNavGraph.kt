package ir.mytodo.presentation.navGraphs

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.gson.Gson
import ir.mytodo.domain.model.Todo
import ir.mytodo.presentation.home.HomeScreen
import ir.mytodo.presentation.home.HomeViewModel
import ir.mytodo.presentation.taskDetails.TaskDetailsScreen
import ir.mytodo.presentation.taskDetails.TaskDetailsViewModel

fun NavGraphBuilder.homeNavGraph(navController: NavController) {
    navigation(
        route = Graph.HOME,
        startDestination = HomeGraph.Home.route ,
    ) {

        CustomComposable(route = HomeGraph.Home.route){
            var todo : Todo ?= null
            val saveStateHandle = navController.currentBackStackEntry?.savedStateHandle
            saveStateHandle?.getLiveData<String>("todo_item")?.observe(LocalLifecycleOwner.current){ value ->
                todo = Gson().fromJson(value , Todo::class.java)
            }
            val homeViewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(navController , todo , homeViewModel)
            navController.currentBackStackEntry?.savedStateHandle?.set("todo_item" , null)
        }
        CustomComposable(route = HomeGraph.TaskDetails.route){ backStackEntry ->
            val todoItem = backStackEntry.arguments?.getString("todoItem")

            val taskDetailsViewModel = hiltViewModel<TaskDetailsViewModel>()
            TaskDetailsScreen(
                navController = navController ,
                todo = if (todoItem == null) null else Gson().fromJson(todoItem , Todo::class.java),
                viewModel = taskDetailsViewModel
            )
        }

    }
}



sealed class HomeGraph(val route : String) {
    data object Home : HomeGraph("Home")
    data object TaskDetails : HomeGraph("TaskDetails/{todoItem}") {
        fun navigateWithTaskItem(todo: Todo) : String {
            val todoGson = Gson().toJson(todo)
            return "TaskDetails/${todoGson}"
        }

        fun navigateWithOutTaskItem() : String {
            return "TaskDetails/"
        }

    }
}

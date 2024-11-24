package ir.mytodo.presentation.home

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ir.mytodo.R
import ir.mytodo.domain.enums.TodoPriority
import ir.mytodo.domain.model.Todo
import ir.mytodo.domain.model.base.UiState
import ir.mytodo.presentation.common.CustomButtonPrimary
import ir.mytodo.presentation.common.CustomCircleProgressBar
import ir.mytodo.presentation.iconPicker.IconUtil
import ir.mytodo.presentation.navGraphs.HomeGraph
import ir.mytodo.presentation.ui.theme.CustomColorPalette
import ir.mytodo.presentation.ui.theme.MyTodoTheme
import ir.mytodo.presentation.utilities.CustomComposePreview
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox


@Composable
fun HomeScreen(
    navController: NavController ,
    todo: Todo ?= null ,
    viewModel: HomeViewModel ?= null
) {
    val uiState by viewModel?.uiStateTodoList?.collectAsState(UiState.Idle) ?: remember { mutableStateOf(UiState.Idle) }
    viewModel?.upsertTodoInList(todo)


    Scaffold(
        topBar = { HomeTopBar() } ,
        floatingActionButton = {
            if (uiState is UiState.Success && getTaskListFromUiState(uiState).isNotEmpty()){
                FloatingActionButton(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        navController.navigate(HomeGraph.TaskDetails.navigateWithOutTaskItem())
                    } ,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Image(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task" ,
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                }
            }
        }
    ) { innerPadding ->

        when(uiState) {
            is UiState.Loading -> {
                HomeLoading(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
            is UiState.Success -> {
                getTaskListFromUiState(uiState = uiState).let { list ->
                    if (list.isEmpty()) {
                        EmptyList(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            navController = navController
                        )
                    } else {
                        HomeContent(
                            innerPadding = innerPadding,
                            list = (uiState as UiState.Success<List<Todo>>).resultData,
                            navController = navController ,
                            deleteItem =  { todoItem ->
                                viewModel?.deleteTodo(todoItem)
                            }
                        )
                    }
                }
            }
            is UiState.Error -> {}
            is UiState.Idle -> {}
        }


    }
}

@Composable
private fun HomeContent(
    innerPadding: PaddingValues,
    list: List<Todo>,
    navController: NavController,
    deleteItem: (item: Todo) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
    ) {
        LazyColumn {
            items(
                items = list,
                key = { item -> item.id },
                contentType = { todo -> todo.id }
            ) { todoItem ->
                ItemTask(
                    item = todoItem,
                    clickItem = {
                        navController.navigate(HomeGraph.TaskDetails.navigateWithTaskItem(todoItem))
                    },
                    deleteItem = deleteItem
                )

            }
        }
    }
}

private fun getTaskListFromUiState(uiState: UiState<List<Todo>>?) =
    (uiState as UiState.Success<List<Todo>>).resultData

@Composable
fun EmptyList(modifier: Modifier , navController: NavController) {
    Box(
        modifier = modifier ,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth() ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                modifier = Modifier.size(100.dp),
                imageVector = IconUtil.createImageVector("TaskAlt") ?: Icons.Default.Check,
                contentDescription = "Empty Icon" ,
                colorFilter = ColorFilter.tint(CustomColorPalette.textColor)
            )

            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = stringResource(id = R.string.task_list_empty) ,
                style = MaterialTheme.typography.bodyLarge ,
                color = CustomColorPalette.textColor
            )

            CustomButtonPrimary(
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .padding(top = 50.dp) ,
                title = stringResource(id = R.string.add_task)
            ) {
                navController.navigate(HomeGraph.TaskDetails.navigateWithOutTaskItem())
            }
        }
    }
}

@Composable
fun HomeLoading(modifier: Modifier) {
    Box(modifier = modifier , contentAlignment = Alignment.Center) {
        CustomCircleProgressBar(isShow = true)
    }
}


@Composable
fun LazyItemScope.ItemTask(item : Todo , clickItem : (item : Todo) -> Unit , deleteItem : (item : Todo) -> Unit) {
    SwipeableActionsBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { clickItem(item) }
            .background(CustomColorPalette.backgroundCardColor)
            .animateItem(tween(600)),
        startActions = listOf(
            SwipeAction(
                onSwipe = { deleteItem(item) } ,
                background = Color.Red ,
                icon = {
                    Image(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .size(40.dp),
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = "Icon delete" ,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(8.dp),
                imageVector = IconUtil.findIconItemByName(item.iconName).image ?: Icons.Default.Check,
                contentDescription = "Icon Item",
                colorFilter = ColorFilter.tint(getColorPriority(item))
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = item.title,
                    color = CustomColorPalette.textColor,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    modifier = Modifier
                        .padding(top = 5.dp),
                    text = item.description,
                    color = CustomColorPalette.textColorSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

}

@Composable
private fun getColorPriority(item: Todo) = when (item.priority) {
    TodoPriority.High -> Color.Red
    TodoPriority.Medium -> MaterialTheme.colorScheme.primary
    TodoPriority.Low -> Color.Green
}

@Composable
fun HomeTopBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 20.dp) ,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.hello) ,
            style = MaterialTheme.typography.titleLarge ,
            color = CustomColorPalette.textColor
        )

        Text(
            text = stringResource(id = R.string.have_good_day) ,
            style = MaterialTheme.typography.bodyMedium ,
            color = CustomColorPalette.textColorSecondary
        )
    }
}

@Composable
@CustomComposePreview
private fun HomeScreenPreview() {
    MyTodoTheme {
//        Scaffold { ip ->
//            EmptyList(modifier = Modifier
//                .fillMaxSize()
//                .padding(ip) ,
//                rememberNavController())
//        }
        HomeScreen(rememberNavController() , null)
    }
}

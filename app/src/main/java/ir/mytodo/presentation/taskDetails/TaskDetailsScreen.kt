package ir.mytodo.presentation.taskDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import ir.mytodo.R
import ir.mytodo.domain.enums.TodoPriority
import ir.mytodo.domain.model.Todo
import ir.mytodo.domain.model.base.UiState
import ir.mytodo.presentation.common.CustomButtonPrimary
import ir.mytodo.presentation.common.CustomEditText
import ir.mytodo.presentation.common.CustomToolbar
import ir.mytodo.presentation.iconPicker.IconPickerBottomSheet
import ir.mytodo.presentation.iconPicker.IconUtil
import ir.mytodo.presentation.ui.theme.CustomColorPalette
import ir.mytodo.presentation.ui.theme.MyTodoTheme
import ir.mytodo.presentation.utilities.CustomComposePreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    navController: NavController ,
    todo: Todo ?= null ,
    viewModel: TaskDetailsViewModel ?= null
) {
    viewModel?.setTodoItemForUpdate(todo)
    val uiState by viewModel?.upsertTodoUiState?.collectAsState(UiState.Idle) ?: remember { mutableStateOf(UiState.Idle) }

    var isTitleError by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(viewModel?.titleValue?.value ?: "") }
    var description by remember { mutableStateOf(viewModel?.descriptionValue?.value ?: "") }
    var iconItemSelected by remember { mutableStateOf(viewModel?.iconItem?.value!!) }
    var priority by remember { mutableStateOf(prioritiesList(viewModel?.priority?.value ?: TodoPriority.Medium)) }
    val iconPickerBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showIconPickerBottomSheet by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        when(uiState) {
            is UiState.Loading -> showLoading = true
            is UiState.Success -> {
                navController.previousBackStackEntry?.savedStateHandle
                    ?.set("todo_item" , Gson().toJson(
                        (uiState as UiState.Success).resultData
                    ))
                navController.popBackStack()
            }
            is UiState.Error -> showLoading = false
            UiState.Idle -> {}
        }
    }

    Scaffold(
        topBar = { TaskDetailsTopBar(navController) } ,
        bottomBar = {
            SaveButton(
                isEnabled = isEnableButton(viewModel, title),
                isLoading = showLoading ,
                onClick = {
                    viewModel?.apply {
                        setTitle(title)
                        setDescription(description)
                        setPriority(priority.find { it.third }?.first ?: TodoPriority.Medium)
                        setIconItem(iconItemSelected)
                        upsertTodo()
                    }
                }
            )
        }
    ) { innerPAdding ->

        if (showIconPickerBottomSheet){
            IconPickerBottomSheet(
                state = iconPickerBottomSheetState ,
                dismissDialog = { iconItem ->
                    iconItemSelected = iconItem
                    showIconPickerBottomSheet = false
                }
            )
        }

        TaskDetailsContent(
            title = title,
            description = description,
            isTitleError = isTitleError,
            priority = priority,
            iconItemSelected = iconItemSelected,
            onTitleChange = {
                isTitleError = it.length !in 3..40
                title = it
            },
            onDescriptionChange = { description = it },
            onPriorityChange = { selectedTitle ->
                priority = priority.map { it.copy(third = it.first.name == selectedTitle) }
            },
            onIconClick = { showIconPickerBottomSheet = true },
            modifier = Modifier.padding(innerPAdding)
        )

    }

}

@Composable
private fun isEnableButton(
    viewModel: TaskDetailsViewModel?,
    title: String
) = viewModel?.isDataValid(title) ?: false

@Composable
fun TaskDetailsContent(
    title: String,
    description: String,
    isTitleError: Boolean,
    priority: List<Triple<TodoPriority, Color, Boolean>>,
    iconItemSelected: IconUtil.IconItem,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriorityChange: (String) -> Unit,
    onIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconPicker(iconItemSelected, onIconClick)

        PrioritySelection(priority, onPriorityChange)

        CustomEditText(
            defaultValue = title,
            lableText = stringResource(id = R.string.title),
            singleLine = true,
            paddingTop = 30.dp,
            errorMessage = stringResource(id = R.string.title_between_3_to_40_char),
            isError = isTitleError,
            onValueChange = onTitleChange
        )

        CustomEditText(
            defaultValue = description,
            lableText = stringResource(id = R.string.description),
            minLine = 4,
            onValueChange = onDescriptionChange
        )
    }
}

@Composable
fun IconPicker(iconItemSelected: IconUtil.IconItem, onClick: () -> Unit) {
    Image(
        modifier = Modifier
            .padding(top = 20.dp)
            .size(100.dp)
            .clip(CircleShape)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(10.dp),
        imageVector = iconItemSelected.image ?: Icons.Default.Check,
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun PrioritySelection(
    priority: List<Triple<TodoPriority, Color, Boolean>>,
    onPriorityChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = 50.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        priority.forEach { item ->
            ItemPriority(
                title = item.first.name,
                isChecked = item.third,
                colorIcon = item.second,
                onClickItem = onPriorityChange
            )
        }
    }
}


@Composable
fun SaveButton(
    isEnabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    CustomButtonPrimary(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        title = stringResource(id = R.string.save),
        isEnable = isEnabled,
        isLoading = isLoading,
        onClickButton = onClick
    )
}


@Composable
private fun TaskDetailsTopBar(navController: NavController) {
    CustomToolbar(
        title = stringResource(id = R.string.add_task),
        backIconClick = {
            navController.popBackStack()
        })
}

private fun prioritiesList(defaultSelected : TodoPriority) = listOf(
    Triple(TodoPriority.High, Color.Red, TodoPriority.High == defaultSelected),
    Triple(TodoPriority.Medium, Color.Yellow, TodoPriority.Medium == defaultSelected),
    Triple(TodoPriority.Low, Color.Green, TodoPriority.Low == defaultSelected)
)

@Composable
fun ItemPriority(
    title : String ,
    isChecked : Boolean ,
    colorIcon : Color ,
    onClickItem : (title : String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically ,
        modifier = Modifier
            .clickable {
                onClickItem(title)
            }
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(colorIcon),
        ) {
            androidx.compose.animation.AnimatedVisibility(visible = isChecked) {
                Image(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Is Checked Item" ,
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            }
        }

        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = title ,
            style = MaterialTheme.typography.titleSmall,
            color = CustomColorPalette.textColor
        )
    }
}

@CustomComposePreview
@Composable
private fun TaskDetailsScreenPreview() {
    MyTodoTheme {
        TaskDetailsScreen(rememberNavController())
    }
}

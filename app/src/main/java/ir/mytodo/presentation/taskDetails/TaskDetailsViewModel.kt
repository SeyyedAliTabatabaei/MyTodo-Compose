package ir.mytodo.presentation.taskDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mytodo.domain.enums.TodoPriority
import ir.mytodo.domain.model.Todo
import ir.mytodo.domain.model.base.ResultEntity
import ir.mytodo.domain.model.base.UiState
import ir.mytodo.domain.usecase.UpsertTodoUseCase
import ir.mytodo.presentation.iconPicker.IconUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val upsertTodoUseCase: UpsertTodoUseCase
) : ViewModel() {

    private var todo : Todo ?= null
    var titleValue = MutableStateFlow("")
    var descriptionValue = MutableStateFlow("")
    var priority = MutableStateFlow(TodoPriority.Medium)
    var iconItem = MutableStateFlow(IconUtil.findIconItemByName(IconUtil.defaultIcon))

    private val _addTodoUiState = MutableStateFlow<UiState<Todo>>(UiState.Idle)
    val upsertTodoUiState : StateFlow<UiState<Todo>> = _addTodoUiState.asStateFlow()

    fun setTodoItemForUpdate(todo: Todo?) {
        todo?.let {
            this.todo = it
            setTitle(it.title)
            setDescription(it.description)
            setPriority(it.priority)
            setIconItem(IconUtil.findIconItemByName(it.iconName))
        }
    }

    fun setTitle(value : String) {
        titleValue.value = value
    }

    fun setDescription(value : String) {
        descriptionValue.value = value
    }

    fun setPriority(todoPriority: TodoPriority) {
        priority.value = todoPriority
    }

    fun setIconItem(newIcon : IconUtil.IconItem) {
        iconItem.value = newIcon
    }

    private fun updateAddTodoUiState(action : UiState<Todo>.() -> UiState<Todo>) {
        _addTodoUiState.update{
            action(it)
        }
    }

    fun upsertTodo() {
        if (!isDataValid(titleValue.value))
            return

        val newTodo = createTodoItem()
        upsertTodoUseCase.invoke(newTodo)
            .onStart {
                updateAddTodoUiState { UiState.Loading }
            }.onEach {
                updateAddTodoUiState {
                    when(it) {
                        is ResultEntity.Success -> UiState.Success(
                            newTodo.copy(id = todo?.id ?: it.data)
                        )
                        is ResultEntity.Error -> UiState.Error(it.error)
                        else -> UiState.Error()
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun isDataValid(title : String) : Boolean {
        return (title.length in 3..40)
    }

    fun createTodoItem() = Todo(
        id = todo?.id ?: 0,
        title = titleValue.value,
        description = descriptionValue.value,
        iconName = iconItem.value.name,
        priority = priority.value
    )


}

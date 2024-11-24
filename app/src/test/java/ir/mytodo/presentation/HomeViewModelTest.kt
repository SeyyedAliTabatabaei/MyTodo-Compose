package ir.mytodo.presentation

import androidx.annotation.VisibleForTesting
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkClass
import ir.mytodo.TestCoroutineRule
import ir.mytodo.domain.enums.TodoPriority
import ir.mytodo.domain.model.Todo
import ir.mytodo.domain.model.base.ResultEntity
import ir.mytodo.domain.model.base.UiState
import ir.mytodo.domain.usecase.DeleteTodoUseCase
import ir.mytodo.domain.usecase.GetTodoListUseCase
import ir.mytodo.presentation.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.jvm.functions.Function1

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: HomeViewModel

    @RelaxedMockK
    lateinit var getTodoListUseCase: GetTodoListUseCase

    @RelaxedMockK
    lateinit var deleteTodoUseCase: DeleteTodoUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = HomeViewModel(getTodoListUseCase , deleteTodoUseCase)
    }

    @Test
    fun `Give todo list, Then respond successfully`() = runTest {
        val response = listOf(mockkClass(Todo::class))

        every {
            getTodoListUseCase.invoke(Unit)
        } returns flowOf(ResultEntity.Success(response))

        viewModel.getTodoList()
        assert(viewModel.uiStateTodoList.value == UiState.Loading)

        advanceUntilIdle()
        assert(viewModel.uiStateTodoList.value == UiState.Success(response))
    }

    @Test
    fun `Give todo list, Then respond emptyList`() = runTest {
        every {
            getTodoListUseCase.invoke(Unit)
        } returns flowOf(ResultEntity.Success(emptyList()))

        viewModel.getTodoList()
        assert(viewModel.uiStateTodoList.value == UiState.Loading)

        advanceUntilIdle()
        assert(viewModel.uiStateTodoList.value == UiState.Success(emptyList<Todo>()))
    }

    @Test
    fun `Give todo list, Then respond error`() = runTest {
        val errorMessage = "Error When Give Todo List"

        every {
            getTodoListUseCase.invoke(Unit)
        } returns flowOf(ResultEntity.Error(errorMessage))

        viewModel.getTodoList()
        assert(viewModel.uiStateTodoList.value == UiState.Loading)

        advanceUntilIdle()
        assert(viewModel.uiStateTodoList.value == UiState.Error(errorMessage))
    }

    @Test
    fun `Upsert item in todo list`() = runTest {
        val updateState = viewModel.javaClass.getDeclaredMethod("updateStateTodoList" , Function1::class.java)
        updateState.isAccessible = true
        val lambda: (UiState<List<Todo>>) -> UiState<List<Todo>> = { UiState.Success(listOf()) }
        updateState.invoke(viewModel , lambda)

        val todo = Todo(
            id = 9 ,
            title = "New Title" ,
            description = "Description" ,
            iconName = "Task" ,
            priority = TodoPriority.Low
        )
        val todoList = listOf(todo)

        viewModel.upsertTodoInList(todo)

        advanceUntilIdle()
        assert(viewModel.uiStateTodoList.value == UiState.Success(todoList))
    }

    @Test
    fun `Delete item in todo list`() = runTest {
        val todo = Todo(
            id = 9 ,
            title = "New Title" ,
            description = "Description" ,
            iconName = "Task" ,
            priority = TodoPriority.Low
        )
        val todoList = listOf(todo)

        val updateState = viewModel.javaClass.getDeclaredMethod("updateStateTodoList" , Function1::class.java)
        updateState.isAccessible = true
        val lambda: (UiState<List<Todo>>) -> UiState<List<Todo>> = { UiState.Success(todoList) }
        updateState.invoke(viewModel , lambda)


        every {
            deleteTodoUseCase.invoke(any())
        } returns flowOf(ResultEntity.Success(Unit))

        viewModel.deleteTodo(todo)

        advanceUntilIdle()
        assert(viewModel.uiStateTodoList.value == UiState.Success(listOf<Todo>()))
    }


}

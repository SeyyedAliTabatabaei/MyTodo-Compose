package ir.mytodo.presentation

import android.util.Log
import app.cash.turbine.test
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkClass
import ir.mytodo.TestCoroutineRule
import ir.mytodo.domain.enums.TodoPriority
import ir.mytodo.domain.model.Todo
import ir.mytodo.domain.model.base.ResultEntity
import ir.mytodo.domain.model.base.UiState
import ir.mytodo.domain.usecase.UpsertTodoUseCase
import ir.mytodo.presentation.iconPicker.IconUtil
import ir.mytodo.presentation.taskDetails.TaskDetailsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*


@OptIn(ExperimentalCoroutinesApi::class)
class TaskDetailsViewModelTest {

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: TaskDetailsViewModel

    @RelaxedMockK
    lateinit var upsertTodoUseCase: UpsertTodoUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = TaskDetailsViewModel(upsertTodoUseCase)
    }

    @Test
    fun `Set title value`() {
        val newTitle = "New Title"
        viewModel.setTitle(newTitle)
        assertEquals(newTitle , viewModel.titleValue.value)
    }

    @Test
    fun `Set description value`() {
        val newDescription = "New Description"
        viewModel.setDescription(newDescription)
        assertEquals(newDescription , viewModel.descriptionValue.value)
    }

    @Test
    fun `Set priority value`() {
        val newPriority = TodoPriority.High
        viewModel.setPriority(newPriority)
        assertEquals(newPriority , viewModel.priority.value)
    }

    @Test
    fun `Set icon value`() {
        val newIcon = mockkClass(IconUtil.IconItem::class)
        viewModel.setIconItem(newIcon)
        assertEquals(newIcon , viewModel.iconItem.value)
    }

    @Test
    fun `Check data is valid`() {
        val title = "New Title"
        assertTrue(viewModel.isDataValid(title))
    }

    @Test
    fun `Check data is not valid`() {
        // LESS THAN 3 CHAR
        val title = "N"
        assertFalse(viewModel.isDataValid(title))

        // MORE THAN 40 CHAR
        val title2 = "N".repeat(50)
        assertFalse(viewModel.isDataValid(title2))
    }


    @Test
    fun `Create todo item`() {
        val title = "New Title"
        val description = "New Description"
        val priority = TodoPriority.Low
        val iconItem = IconUtil.findIconItemByName(IconUtil.defaultIcon)

        viewModel.apply {
            setTitle(title)
            setDescription(description)
            setIconItem(iconItem)
            setPriority(priority)
        }

        val todo = viewModel.createTodoItem()
        assertEquals(todo , Todo(
            id = 0 ,
            title = title ,
            description = description ,
            priority = TodoPriority.Low ,
            iconName = iconItem.name
        ))
    }

    @Test
    fun `Add Todo Item, Then Successfully`() = runTest {
        assert(viewModel.upsertTodoUiState.value == UiState.Idle)

        val todo = Todo(5 , "Title" , "Description" , IconUtil.defaultIcon , TodoPriority.Low)
        every {
            upsertTodoUseCase.invoke(any())
        } returns flowOf(ResultEntity.Success(todo.id))

        // Set title for set data is valid
        viewModel.setTitle(todo.title)
        viewModel.setDescription(todo.description)
        viewModel.setIconItem(IconUtil.findIconItemByName(todo.iconName))
        viewModel.setPriority(todo.priority)

        viewModel.upsertTodo()

        advanceUntilIdle()
        assert((viewModel.upsertTodoUiState.value  == UiState.Success(todo)))
    }

    @Test
    fun `Add Todo Item, Then Error`() = runTest {
        assert(viewModel.upsertTodoUiState.value == UiState.Idle)

        val errorMessage = "Error Message"
        every {
            upsertTodoUseCase.invoke(any())
        } returns flowOf(ResultEntity.Error(errorMessage))

        // Set title for set data is valid
        viewModel.setTitle("New Title")

        viewModel.upsertTodo()

        advanceUntilIdle()
        assert(viewModel.upsertTodoUiState.value == UiState.Error(errorMessage))
    }

    @Test
    fun `Update Todo Item, Then Successfully`() = runTest {
        assert(viewModel.upsertTodoUiState.value == UiState.Idle)

        val todo = Todo(5 , "Title" , "Description" , "ic" , TodoPriority.Low)
        viewModel.setTodoItemForUpdate(todo)

        val todoItemForUpdate = viewModel.createTodoItem()
        every {
            upsertTodoUseCase.invoke(any())
        } returns flowOf(ResultEntity.Success(todoItemForUpdate.id))

        viewModel.upsertTodo()
        advanceUntilIdle()
        assert(viewModel.upsertTodoUiState.value == UiState.Success(todoItemForUpdate))
    }

    @Test
    fun `Update Todo Item, Then Error`() = runTest {
        assert(viewModel.upsertTodoUiState.value == UiState.Idle)

        val todo = Todo(5 , "Title" , "Description" , "ic" , TodoPriority.Low)
        viewModel.setTodoItemForUpdate(todo)

        val errorMessage = "Error Message"
        every {
            upsertTodoUseCase.invoke(any())
        } returns flowOf(ResultEntity.Error(errorMessage))

        viewModel.upsertTodo()
        advanceUntilIdle()
        assert(viewModel.upsertTodoUiState.value == UiState.Error(errorMessage))
    }

}

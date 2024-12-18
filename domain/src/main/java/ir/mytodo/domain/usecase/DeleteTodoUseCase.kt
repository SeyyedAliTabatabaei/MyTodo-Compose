package ir.mytodo.domain.usecase

import ir.mytodo.domain.model.Todo
import ir.mytodo.domain.model.base.ResultEntity
import ir.mytodo.domain.repositories.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class DeleteTodoUseCase(
    private val todoRepository: TodoRepository ,
    coroutineDispatcher: CoroutineDispatcher
) : BaseUseCase<Todo , ResultEntity<Unit>>(coroutineDispatcher) {

    override fun execute(parameters: Todo): Flow<ResultEntity<Unit>> =
        todoRepository.delete(parameters)


}

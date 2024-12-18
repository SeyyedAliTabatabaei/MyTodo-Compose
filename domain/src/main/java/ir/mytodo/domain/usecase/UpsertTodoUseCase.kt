package ir.mytodo.domain.usecase

import ir.mytodo.domain.model.Todo
import ir.mytodo.domain.model.base.ResultEntity
import ir.mytodo.domain.repositories.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class UpsertTodoUseCase(
    private val todoRepository: TodoRepository ,
    coroutineDispatcher: CoroutineDispatcher
) : BaseUseCase<Todo , ResultEntity<Long>>(coroutineDispatcher) {

    override fun execute(parameters: Todo): Flow<ResultEntity<Long>> {
        return todoRepository.upsert(parameters)
    }

}

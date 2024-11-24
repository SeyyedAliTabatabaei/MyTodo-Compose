package ir.mytodo.domain.usecase

import ir.mytodo.domain.model.Todo
import ir.mytodo.domain.model.base.ResultEntity
import ir.mytodo.domain.repositories.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class GetTodoListUseCase(
    private val todoRepository: TodoRepository ,
    coroutineDispatcher: CoroutineDispatcher
) : BaseUseCase<Unit , ResultEntity<List<Todo>>>(coroutineDispatcher) {

    override fun execute(parameters: Unit): Flow<ResultEntity<List<Todo>>> {
        return todoRepository.getList()
    }


}

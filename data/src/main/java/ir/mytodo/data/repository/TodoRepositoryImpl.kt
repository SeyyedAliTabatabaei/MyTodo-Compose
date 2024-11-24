package ir.mytodo.data.repository

import ir.mytodo.domain.model.Todo
import ir.mytodo.domain.model.base.ResultEntity
import ir.mytodo.domain.repositories.TodoLocalDataSource
import ir.mytodo.domain.repositories.TodoRepository
import kotlinx.coroutines.flow.Flow

class TodoRepositoryImpl(
    private val todoDataSource: TodoLocalDataSource
) : TodoRepository {

    override fun upsert(todo: Todo): Flow<ResultEntity<Long>> {
        return todoDataSource.upsert(todo)
    }

    override fun getList(): Flow<ResultEntity<List<Todo>>> {
        return todoDataSource.getList()
    }

    override fun delete(todo: Todo): Flow<ResultEntity<Unit>> {
        return todoDataSource.delete(todo)
    }

}

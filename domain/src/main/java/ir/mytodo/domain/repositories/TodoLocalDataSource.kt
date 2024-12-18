package ir.mytodo.domain.repositories

import ir.mytodo.domain.model.Todo
import ir.mytodo.domain.model.base.ResultEntity
import kotlinx.coroutines.flow.Flow

interface TodoLocalDataSource {

    fun upsert(todo: Todo) : Flow<ResultEntity<Long>>

    fun getList() : Flow<ResultEntity<List<Todo>>>

    fun delete(todo: Todo) : Flow<ResultEntity<Unit>>
}

package ir.mytodo.data.datasource

import ir.mytodo.data.config.db.TodoDao
import ir.mytodo.data.entities.mappers.TodoMappers
import ir.mytodo.domain.model.Todo
import ir.mytodo.domain.model.base.ResultEntity
import ir.mytodo.domain.repositories.TodoLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TodoLocalDataSource(
    private val dao: TodoDao ,
    private val todoMappers: TodoMappers
) : TodoLocalDataSource, BaseDataSource() {

    override fun upsert(todo: Todo): Flow<ResultEntity<Long>> {
        return flow {
            emit(
                safeQueryDb {
                    dao.upsert(todoMappers.mapToEntity(todo))
                }
            )
        }
    }

    override fun getList(): Flow<ResultEntity<List<Todo>>> {
        return flow {
            emit(
                safeQueryDb {
                    dao.getList().map { todoMappers.mapFromEntity(it) }
                }
            )
        }
    }

    override fun delete(todo: Todo): Flow<ResultEntity<Unit>> {
        return flow {
            emit(
                safeQueryDb {
                    dao.delete(todoMappers.mapToEntity(todo))
                }
            )
        }
    }


}

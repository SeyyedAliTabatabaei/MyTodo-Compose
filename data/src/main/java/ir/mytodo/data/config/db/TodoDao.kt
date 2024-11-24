package ir.mytodo.data.config.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import ir.mytodo.data.entities.TodoEntity

@Dao
interface TodoDao {

    @Upsert(TodoEntity::class)
    suspend fun upsert(todo: TodoEntity) : Long

    @Delete(TodoEntity::class)
    suspend fun delete(todo: TodoEntity)

    @Query("SELECT * FROM todo ORDER BY id DESC")
    suspend fun getList() : List<TodoEntity>
}

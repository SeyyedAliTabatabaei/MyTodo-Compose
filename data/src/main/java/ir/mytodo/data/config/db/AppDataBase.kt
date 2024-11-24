package ir.mytodo.data.config.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.mytodo.data.entities.TodoEntity
import ir.mytodo.domain.model.Todo


@Database(entities = [TodoEntity::class] , version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getDao() : TodoDao

}

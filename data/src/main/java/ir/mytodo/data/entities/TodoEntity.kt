package ir.mytodo.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.mytodo.domain.enums.TodoPriority

@Entity(tableName = "todo")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id : Long ,
    @ColumnInfo(name = "title") val title : String ,
    @ColumnInfo(name = "description") val description : String ,
    @ColumnInfo(name = "icon_name") val iconName : String ,
    @ColumnInfo(name = "priority") val priority: TodoPriority = TodoPriority.Medium
)

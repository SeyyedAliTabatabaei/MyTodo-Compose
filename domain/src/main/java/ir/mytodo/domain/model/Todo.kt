package ir.mytodo.domain.model

import ir.mytodo.domain.enums.TodoPriority

data class Todo(
    val id : Long ,
    val title : String ,
    val description : String ,
    val iconName : String ,
    val priority: TodoPriority = TodoPriority.Medium
)

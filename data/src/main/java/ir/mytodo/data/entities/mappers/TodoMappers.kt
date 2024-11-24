package ir.mytodo.data.entities.mappers

import ir.mytodo.data.entities.EntityMapper
import ir.mytodo.data.entities.TodoEntity
import ir.mytodo.domain.model.Todo

class TodoMappers : EntityMapper<TodoEntity , Todo> {
    override fun mapToEntity(domain: Todo): TodoEntity {
        return TodoEntity(
            id = domain.id ,
            title = domain.title ,
            description = domain.description ,
            iconName = domain.iconName ,
            priority = domain.priority
        )
    }

    override fun mapFromEntity(entity: TodoEntity): Todo {
        return Todo(
            id = entity.id ,
            title = entity.title ,
            description = entity.description ,
            iconName = entity.iconName ,
            priority = entity.priority
        )
    }
}

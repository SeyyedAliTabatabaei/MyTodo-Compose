package ir.mytodo.data.entities

interface EntityMapper<E , D> {
    fun mapToEntity(domain : D) : E
    fun mapFromEntity(entity : E) : D
}

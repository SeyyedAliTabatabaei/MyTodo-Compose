package ir.mytodo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.mytodo.data.config.db.TodoDao
import ir.mytodo.data.entities.mappers.TodoMappers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModules {

    @Singleton
    @Provides
    fun provideTodoDataSource(todoDao: TodoDao , todoMappers: TodoMappers) : ir.mytodo.domain.repositories.TodoLocalDataSource {
        return ir.mytodo.data.datasource.TodoLocalDataSource(todoDao , todoMappers)
    }

}

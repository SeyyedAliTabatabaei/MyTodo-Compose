package ir.mytodo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.mytodo.data.repository.TodoRepositoryImpl
import ir.mytodo.domain.repositories.TodoLocalDataSource
import ir.mytodo.domain.repositories.TodoRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModules {

    @Singleton
    @Provides
    fun provideTodoRepository(todoDataSource: TodoLocalDataSource) : TodoRepository {
        return TodoRepositoryImpl(todoDataSource)
    }

}

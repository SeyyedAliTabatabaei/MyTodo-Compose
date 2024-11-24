package ir.mytodo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.mytodo.domain.repositories.TodoRepository
import ir.mytodo.domain.usecase.UpsertTodoUseCase
import ir.mytodo.domain.usecase.DeleteTodoUseCase
import ir.mytodo.domain.usecase.GetTodoListUseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserCaseModules {

    @Singleton
    @Provides
    fun provideUpsertTodoUseCase(todoRepository: TodoRepository) : UpsertTodoUseCase {
        return UpsertTodoUseCase(todoRepository , Dispatchers.IO)
    }

    @Singleton
    @Provides
    fun provideDeleteTodoUseCase(todoRepository: TodoRepository) : DeleteTodoUseCase {
        return DeleteTodoUseCase(todoRepository , Dispatchers.IO)
    }

    @Singleton
    @Provides
    fun provideGetTodoListUseCase(todoRepository: TodoRepository) : GetTodoListUseCase {
        return GetTodoListUseCase(todoRepository , Dispatchers.IO)
    }

}

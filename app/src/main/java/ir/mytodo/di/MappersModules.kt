package ir.mytodo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.mytodo.data.entities.mappers.TodoMappers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MappersModules {

    @Singleton
    @Provides
    fun provideTodoMapper() : TodoMappers {
        return TodoMappers()
    }

}

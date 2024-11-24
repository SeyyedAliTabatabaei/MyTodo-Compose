package ir.mytodo.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.mytodo.data.config.db.AppDataBase
import ir.mytodo.data.config.db.RoomHelper
import ir.mytodo.data.config.db.TodoDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context) : AppDataBase {
        return RoomHelper().invoke(context)
    }

    @Singleton
    @Provides
    fun provideDao(appDataBase: AppDataBase) : TodoDao {
        return appDataBase.getDao()
    }

}

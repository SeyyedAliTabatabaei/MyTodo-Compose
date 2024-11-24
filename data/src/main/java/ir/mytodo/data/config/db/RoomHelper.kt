package ir.mytodo.data.config.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.mytodo.data.Constants

class RoomHelper {

    operator fun invoke(context: Context) : AppDataBase {
        return Room
            .databaseBuilder(context , AppDataBase::class.java , Constants.DB_NAME)
            .build()
    }

}

package ir.mytodo.data.datasource

import androidx.core.os.BuildCompat
import ir.mytodo.domain.model.base.ResultEntity

abstract class BaseDataSource {

    suspend fun <T> safeQueryDb(query : suspend () -> T) : ResultEntity<T> {
        return try {
            query.invoke().let {
                ResultEntity.Success(it)
            }
        } catch (e : Exception) {
            ResultEntity.Error(e.message.toString())
        }
    }


}

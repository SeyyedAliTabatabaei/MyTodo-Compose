package ir.mytodo.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseUseCase<in P , R>(private val coroutineDispatcher : CoroutineDispatcher) {

    operator fun invoke(parameters : P) : Flow<R> {
        return execute(parameters)
            .flowOn(coroutineDispatcher)
    }

    abstract fun execute(parameters: P) : Flow<R>
}

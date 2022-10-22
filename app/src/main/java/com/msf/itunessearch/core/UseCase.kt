package com.msf.itunessearch.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class UseCase<out T, in Params>(
    private val contextProvider: CoroutinesContextProvider
) {

    abstract suspend fun run(params: Params): Either<T, Throwable>

    operator fun invoke(
        scope: CoroutineScope,
        params: Params,
        onError: ((Throwable) -> Unit) = {},
        onSuccess: (T) -> Unit
    ) {
        scope.launch(contextProvider.io) {
            runCatching {
                run(params).run {
                    withContext(contextProvider.main) {
                        either(
                            {
                                onSuccess(it)
                            },
                            {
                                onError(it)
                            }
                        )
                    }
                }
            }.onFailure {
                withContext(contextProvider.main) {
                    onError(it)
                }
            }
        }
    }
}

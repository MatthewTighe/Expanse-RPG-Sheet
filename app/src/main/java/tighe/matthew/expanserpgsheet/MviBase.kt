package tighe.matthew.expanserpgsheet

import androidx.annotation.IdRes
import kotlinx.coroutines.flow.Flow

interface Intention<A: Action> {
    fun mapToAction(): A
}

interface Action

interface Result

interface ViewState

sealed class ViewStateEvent {
    data class Navigate(@IdRes val fragment: Int)
    data class Toast(@IdRes val message: Int)
}

interface Processor<A: Action> {
    val result: Flow<Result>

    fun consumeAction(action: A)
}

interface Reducer<V: ViewState, R: Result>
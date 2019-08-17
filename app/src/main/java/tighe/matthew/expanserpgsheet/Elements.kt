package tighe.matthew.expanserpgsheet

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData

interface BaseViewModel<V: ViewState, A: Action> {
    fun observeViewState(): LiveData<V>

    fun observeEvent(): LiveData<Event>

    fun submitAction(action: A)
}

interface ViewState

interface Action

sealed class Event {
    data class Navigate(@IdRes val fragment: Int, val bundle: Bundle = bundleOf()) : Event()
}
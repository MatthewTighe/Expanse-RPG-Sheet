package tighe.matthew.expanserpgsheet

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

interface BaseViewModel<V: ViewState, A: Action> {
    fun observeViewState(): LiveData<V>

    fun observeEvent(): SingleLiveEvent<Event>

    fun submitAction(action: A)
}

interface ViewState

interface Action

sealed class Event {
    data class Navigate(@IdRes val fragment: Int, val bundle: Bundle = bundleOf()) : Event()
}

class SingleLiveEvent<E> : MutableLiveData<E?>() {
    private var pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in E?>) {
        super.observe(owner, Observer { it?.let { update ->
            if (pending.compareAndSet(true, false))
                observer.onChanged(update)
        }})
    }

    @MainThread
    override fun setValue(value: E?) {
        pending.set(true)
        super.setValue(value)
    }
}
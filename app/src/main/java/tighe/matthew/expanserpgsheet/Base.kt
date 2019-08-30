package tighe.matthew.expanserpgsheet

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
import java.util.concurrent.atomic.AtomicBoolean

interface BaseViewModel<V : ViewState, A : Action> {
    fun observeViewState(): LiveData<V>

    fun observeEvent(): SingleLiveEvent<Event>

    fun submitAction(action: A)
}

interface ViewState

interface TextInputFieldError {
    val errorEnabled: Boolean
    val errorMessage: Int
    fun handleDisplay(view: TextInputLayout?) {
        if (errorEnabled) {
            view?.isErrorEnabled = true
            view?.error = view?.context?.getString(errorMessage)
        } else {
            view?.isErrorEnabled = false
        }
    }
}

interface Action

data class NavigationArgument(
    val key: String = "",
    val parcelable: Parcelable
)

fun navArgsToBundle(args: List<NavigationArgument>): Bundle {
    val bundle = bundleOf()
    with(bundle) {
        for (navArg in args) {
            putParcelable(navArg.key, navArg.parcelable)
        }
    }
    return bundle
}

sealed class Event {
    data class Navigate(@IdRes val fragment: Int, val navigationArgs: List<NavigationArgument> = listOf()) : Event()
}

class SingleLiveEvent<E : Event> : MutableLiveData<E>() {
    private var pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in E>) {
        super.observe(owner, Observer { it?.let { update ->
            if (pending.compareAndSet(true, false))
                observer.onChanged(update)
        } })
    }

    @MainThread
    override fun setValue(value: E) {
        pending.set(true)
        super.setValue(value)
    }
}
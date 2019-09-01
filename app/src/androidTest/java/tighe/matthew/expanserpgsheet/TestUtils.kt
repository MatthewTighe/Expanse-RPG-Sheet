package tighe.matthew.expanserpgsheet

import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.test.rule.ActivityTestRule
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun ActivityTestRule<MainActivity>.navTo(@IdRes fragmentId: Int, navArgs: List<NavigationArgument> = listOf()) {
    val bundle = navArgsToBundle(navArgs)
    this.activity.findNavController(R.id.nav_host_fragment).navigate(fragmentId, bundle)
}

fun <T> LiveData<T>.blockingObserve(): T? {
    var value: T? = null
    val latch = CountDownLatch(1)
    val innerObserver = Observer<T> {
        value = it
        latch.countDown()
    }
    observeForever(innerObserver)
    latch.await(500, TimeUnit.MILLISECONDS)
    return value
}
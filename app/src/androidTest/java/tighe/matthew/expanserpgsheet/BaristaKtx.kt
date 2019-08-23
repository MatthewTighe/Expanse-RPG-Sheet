package tighe.matthew.expanserpgsheet

import androidx.annotation.IdRes
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.internal.failurehandler.BaristaException
import java.util.concurrent.TimeoutException


// Interactions
@IdRes fun Int.click() {
    clickOn(this)
}

fun String.click() {
    clickOn(this)
}

// Assertions
@IdRes fun Int.isDisplayed() {
    assertDisplayed(this)
}

fun String.isDisplayed() {
    assertDisplayed(this)
}

@IdRes fun Int.shortWaitForDisplay() {
    val test = {
        try {
            assertDisplayed(this)
            true
        } catch (err: BaristaException) {
            false
        }
    }
    waitForSuccess(test, 1000)
}

fun waitForSuccess(test: () -> Boolean, timeout: Long) {
    val startTime = System.currentTimeMillis()
    while (!test()) {
        val currTime = System.currentTimeMillis()
        if (currTime >= startTime + timeout) throw TimeoutException()
        Thread.sleep(10)
    }
}

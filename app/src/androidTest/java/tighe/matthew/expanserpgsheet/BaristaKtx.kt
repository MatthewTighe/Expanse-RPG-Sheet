package tighe.matthew.expanserpgsheet

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotExist
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo

// Interactions
@IdRes fun Int.click() {
    clickOn(this)
}

fun String.click() {
    clickOn(this)
}

@IdRes fun Int.writeText(text: String) {
    writeTo(this, text)
}

@IdRes fun Int.menuClick() {
    clickMenu(this)
}

// Assertions
@IdRes fun Int.isDisplayed() {
    assertDisplayed(this)
}

fun String.isDisplayed() {
    assertDisplayed(this)
}

fun String.doesNotExist() {
    assertNotExist(this)
}

@StringRes fun Int.isContained() {
    assertContains(this)
}

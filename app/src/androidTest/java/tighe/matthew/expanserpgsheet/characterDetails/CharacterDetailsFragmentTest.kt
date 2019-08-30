package tighe.matthew.expanserpgsheet.characterDetails

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tighe.matthew.expanserpgsheet.*
import tighe.matthew.expanserpgsheet.model.character.Character

@RunWith(AndroidJUnit4::class)
class CharacterDetailsFragmentTest {
    @get:Rule val activityRule = ActivityTestRule(MainActivity::class.java)

    private val initialFortune = 25
    private val initialModel =
        Character(0, "name", initialFortune)

    @Test
    fun characterFortuneCanBeIncremented() {
        val navArgs = listOf(initialModel.buildNavArg())
        activityRule.navTo(R.id.character_details_fragment, navArgs)

        R.id.layout_fragment_character_details.isDisplayed()
        initialFortune.toString().isDisplayed()

        R.id.text_increment_five.click()
        val expectedIncrement1 = initialFortune + 5
        (expectedIncrement1).toString().isDisplayed()

        R.id.text_increment_one.click()
        val expectedIncrement2 = expectedIncrement1 + 1
        (expectedIncrement2).toString().isDisplayed()
    }

    @Test
    fun characterFortuneCanBeDecremented() {
        val navArgs = listOf(initialModel.buildNavArg())
        activityRule.navTo(R.id.character_details_fragment, navArgs)

        R.id.layout_fragment_character_details.isDisplayed()
        initialFortune.toString().isDisplayed()

        R.id.text_decrement_five.click()
        val expectedDecrement1 = initialFortune - 5
        (expectedDecrement1).toString().isDisplayed()

        R.id.text_decrement_one.click()
        val expectedDecrement2 = expectedDecrement1 - 1
        (expectedDecrement2).toString().isDisplayed()
    }
}
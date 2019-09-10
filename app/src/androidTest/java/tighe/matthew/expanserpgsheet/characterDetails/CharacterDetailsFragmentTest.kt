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

        R.id.btn_inc_fortune.click()
        val expectedIncrement = initialFortune + 1
        (expectedIncrement).toString().isDisplayed()

    }

    @Test
    fun characterFortuneCanBeDecremented() {
        val navArgs = listOf(initialModel.buildNavArg())
        activityRule.navTo(R.id.character_details_fragment, navArgs)

        R.id.layout_fragment_character_details.isDisplayed()
        initialFortune.toString().isDisplayed()

        R.id.btn_dec_fortune.click()
        val expectedDecrement = initialFortune - 1
        expectedDecrement.toString().isDisplayed()
    }
}
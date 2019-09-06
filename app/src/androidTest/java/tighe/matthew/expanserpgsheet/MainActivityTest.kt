package tighe.matthew.expanserpgsheet

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testMainActivityHostsNavFragment() {
        R.id.nav_host_fragment.isDisplayed()
    }

    @Test
    fun testCharacterCanBeAddedThenModified() {
        val expectedName = "name"
        val expectedInitialFortune = 25
        // Start fragment is character list
        R.id.layout_fragment_character_list.isDisplayed()

        // Nav and create new character
        R.id.btn_create_new.click()
        R.id.input_name.writeText(expectedName)
        R.id.input_max_fortune.writeText(expectedInitialFortune.toString())
        R.id.btn_save.click()

        // Character has been added to list. Click it to nav to details.
        R.id.layout_fragment_character_list.isDisplayed()
        expectedName.isDisplayed()
        expectedName.click()

        // Character fortune can be modified
        R.id.layout_fragment_character_details.isDisplayed()
        expectedInitialFortune.toString().isDisplayed()
        R.id.text_increment_five.click()
        val incrementedFortune = expectedInitialFortune + 5
        incrementedFortune.toString().isDisplayed()

        R.id.text_decrement_five.click()
        expectedInitialFortune.toString().isDisplayed()
    }

    @Test
    fun characterCanBeAddedAndThenAddedToEncounter() {
        val expectedName = "name"
        val expectedInitialFortune = 25
        // Start fragment is character list
        R.id.layout_fragment_character_list.isDisplayed()

        // Nav and create new character
        R.id.btn_create_new.click()
        R.id.input_name.writeText(expectedName)
        R.id.input_max_fortune.writeText(expectedInitialFortune.toString())
        R.id.btn_save.click()

        // Character has been added to list. Click options and add it to an encounter.
        R.id.layout_fragment_character_list.isDisplayed()
        expectedName.isDisplayed()
        R.id.btn_options.click()
        R.string.add_encounter.click()
        R.id.encounter_fragment.click()

        R.id.layout_encounter_fortune_adjustment.isDisplayed()
    }
}
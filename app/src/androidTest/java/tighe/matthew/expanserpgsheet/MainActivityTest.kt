package tighe.matthew.expanserpgsheet

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tighe.matthew.expanserpgsheet.model.character.Attributes

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testCharacterCanBeAddedThenModified() {
        val expectedName = "name"
        val initialFortune = 25
        val attributes = Attributes(1, 2, 3, 4, 5, 6, 7, 8, 9)
        // Start fragment is character list
        R.id.layout_fragment_character_list.isDisplayed()

        // Nav and create new character
        R.id.btn_create_new.click()
        R.id.input_name.writeText(expectedName)
        R.id.input_max_fortune.writeText(initialFortune.toString())
        enterAttributes(attributes)

        R.id.btn_save.click()

        // Character has been added to list. Click it to nav to details.
        R.id.layout_fragment_character_list.isDisplayed()
        expectedName.isDisplayed()
        expectedName.click()

        // Character fortune is displayed and can be modified
        R.id.layout_fragment_character_details.isDisplayed()
        initialFortune.toString().isDisplayed()
        val updatedFortune = 35
        R.id.details_input_max_fortune.writeText(updatedFortune.toString())
        R.id.character_list_fragment.click()
        expectedName.click()
        updatedFortune.toString().isDisplayed()

        // Attributes are displayed
        attributes.accuracy.toString().isDisplayed()
        attributes.communication.toString().isDisplayed()
        attributes.constitution.toString().isDisplayed()
        attributes.dexterity.toString().isDisplayed()
        attributes.fighting.toString().isDisplayed()
        attributes.intelligence.toString().isDisplayed()
        attributes.perception.toString().isDisplayed()
        attributes.strength.toString().isDisplayed()
        attributes.willpower.toString().isDisplayed()

        // Attribute can be modified
        val updatedAccuracy = 11
        R.id.input_accuracy.writeText(updatedAccuracy.toString())
        R.id.character_list_fragment.click()
        expectedName.click()
        updatedAccuracy.toString().isDisplayed()

        // Leave details page
        R.id.character_list_fragment.click()
        expectedName.isDisplayed()
        expectedName.click()
        updatedFortune.toString().isDisplayed()
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
        enterAttributes(Attributes(1, 2, 3, 4, 5, 6, 7, 8, 9))
        R.id.btn_save.click()

        // Character has been added to list. Click options and add it to an encounter.
        R.id.layout_fragment_character_list.isDisplayed()
        expectedName.isDisplayed()
        R.id.btn_options.click()
        R.string.add_encounter.click()

        // Enter initiative
        val expectedInitiative = 10
        R.id.initiative_entry_edit_text.isDisplayed()
        R.id.initiative_entry_edit_text.writeText(expectedInitiative.toString())
        R.string.add.click()

        R.id.encounter_fragment.click()

        R.id.layout_encounter_fortune_adjustment.isDisplayed()
        expectedName.isDisplayed()
        expectedInitialFortune.toString().isDisplayed()
        "Initiative: $expectedInitiative".isDisplayed()
    }

    private fun enterAttributes(attributes: Attributes) {
        R.id.input_accuracy.writeText(attributes.accuracy.toString())
        R.id.input_communication.writeText(attributes.communication.toString())
        R.id.input_constitution.writeText(attributes.constitution.toString())
        R.id.input_dexterity.writeText(attributes.dexterity.toString())
        R.id.input_fighting.writeText(attributes.fighting.toString())
        R.id.input_intelligence.writeText(attributes.intelligence.toString())
        R.id.input_perception.writeText(attributes.perception.toString())
        R.id.input_strength.writeText(attributes.strength.toString())
        R.id.input_willpower.writeText(attributes.willpower.toString())
    }
}
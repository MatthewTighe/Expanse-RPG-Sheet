package tighe.matthew.expanserpgsheet.characterCreation

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject
import tighe.matthew.expanserpgsheet.*
import tighe.matthew.expanserpgsheet.model.character.Attributes
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository

@RunWith(AndroidJUnit4::class)
class CharacterCreationFragmentTest : KoinTest {
    @get:Rule val activityRule = ActivityTestRule(MainActivity::class.java)

    private val characterRepository by inject<CharacterRepository>()

    @Before
    fun setup() {
        activityRule.navTo(R.id.character_creation_fragment)
    }

    @Test
    fun characterValuesCanBeEnteredAndSaved() {
        val name = "TestCharacter"
        val fortune = 20
        val accuracy = 1
        val communication = 2
        val constitution = 3
        val dexterity = 4
        val fighting = 5
        val intelligence = 6
        val perception = 7
        val strength = 8
        val willpower = 9
        val attributes = Attributes(accuracy, communication, constitution, dexterity, fighting, intelligence, perception, strength, willpower)
        val expectedModel = Character(1, name, fortune, attributes = attributes)

        R.id.layout_fragment_character_creation.isDisplayed()

        R.id.input_name.writeText(name)
        R.id.input_max_fortune.writeText(fortune.toString())
        R.id.input_accuracy.writeText(accuracy.toString())
        R.id.input_communication.writeText(communication.toString())
        R.id.input_constitution.writeText(constitution.toString())
        R.id.input_dexterity.writeText(dexterity.toString())
        R.id.input_fighting.writeText(fighting.toString())
        R.id.input_intelligence.writeText(intelligence.toString())
        R.id.input_perception.writeText(perception.toString())
        R.id.input_strength.writeText(strength.toString())
        R.id.input_willpower.writeText(willpower.toString())

        R.id.btn_save.click()

        val result = runBlocking { characterRepository.load(1) }
        assertEquals(expectedModel, result)
    }

    @Test
    fun errorsAreDisplayedIFieldsAreEmpty() {
        R.id.layout_fragment_character_creation.isDisplayed()

        R.id.btn_save.click()

        activityRule.activity.getString(R.string.text_input_error, "Name").isDisplayed()
        activityRule.activity.getString(R.string.text_input_error, "Accuracy").isDisplayed()
        activityRule.activity.getString(R.string.text_input_error, "Communication").isDisplayed()
        activityRule.activity.getString(R.string.text_input_error, "Constitution").isDisplayed()
        activityRule.activity.getString(R.string.text_input_error, "Dexterity").isDisplayed()
        activityRule.activity.getString(R.string.text_input_error, "Fighting").isDisplayed()
        activityRule.activity.getString(R.string.text_input_error, "Intelligence").isDisplayed()
        activityRule.activity.getString(R.string.text_input_error, "Perception").isDisplayed()
        activityRule.activity.getString(R.string.text_input_error, "Strength").isDisplayed()
        activityRule.activity.getString(R.string.text_input_error, "Willpower").isDisplayed()

    }
}
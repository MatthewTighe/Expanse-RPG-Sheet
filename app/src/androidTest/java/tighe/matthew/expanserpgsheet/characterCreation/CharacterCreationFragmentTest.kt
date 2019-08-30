package tighe.matthew.expanserpgsheet.characterCreation

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tighe.matthew.expanserpgsheet.*
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository

@RunWith(AndroidJUnit4::class)
class CharacterCreationFragmentTest {
    @get:Rule val activityRule = ActivityTestRule(MainActivity::class.java)

    private lateinit var characterRepository: CharacterRepository

    @Before
    fun setup() {
        characterRepository =
            CharacterRepository(activityRule.activity)
        activityRule.navTo(R.id.character_creation_fragment)
    }

    @Test
    fun characterValuesCanBeEnteredAndSaved() {
        R.id.layout_fragment_character_creation.isDisplayed()

        val name = "TestCharacter"
        val fortune = 20
        R.id.input_name.writeText(name)
        R.id.input_max_fortune.writeText(fortune.toString())
        R.id.btn_save.click()

        val expectedModel = Character(1, name, fortune)
        val result = runBlocking { characterRepository.load(1) }
        assertEquals(expectedModel, result)
    }

    @Test
    fun errorIsDisplayedIfNameFieldIsEmpty() {
        R.id.layout_fragment_character_creation.isDisplayed()

        R.id.btn_save.click()

        R.string.error_name_required.isDisplayed()
    }

}
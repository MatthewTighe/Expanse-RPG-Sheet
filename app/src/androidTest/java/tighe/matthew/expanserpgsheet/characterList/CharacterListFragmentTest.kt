package tighe.matthew.expanserpgsheet.characterList

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tighe.matthew.expanserpgsheet.*
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository

@RunWith(AndroidJUnit4::class)
class CharacterListFragmentTest {
    @get:Rule val activityRule = ActivityTestRule(MainActivity::class.java)

    private lateinit var characterRepository: CharacterRepository
    private val modelName = "name"
    private val model = Character(0, "name", 10)

    @Before
    fun setup() {
        characterRepository =
            CharacterRepository(activityRule.activity)
        runBlocking {
            characterRepository.persist(model)
        }
    }

    @Test
    fun testAddButtonNavsToCreation() {
        R.id.layout_fragment_character_list.isDisplayed()

        R.id.btn_create_new.click()

        R.id.layout_fragment_character_creation.isDisplayed()
    }

    @Test
    fun characterCanBeDeleted() {
        R.id.layout_fragment_character_list.isDisplayed()
        modelName.isDisplayed()

        R.id.btn_options.click()

        modelName.doesNotExist()
        R.string.delete.isContained()
        R.string.delete.click()
    }

    @Test
    fun clickingCharacterNavsToDetails() {
        modelName.isDisplayed()

        modelName.click()

        R.id.layout_fragment_character_details.isDisplayed()
    }
}
package tighe.matthew.expanserpgsheet.characterList

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu
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

    // TODO figure out how to assert on context menu item
//    @Test
//    fun clickingOptionsShouldOpenMenuNotNav() {
//        R.id.layout_fragment_character_list.isDisplayed()
//
//        R.id.btn_options.click()
//
//        R.id.item_delete.isDisplayed()
//        R.id.character_details_fragment.isNotDisplayed()
//    }

    @Test
    fun clickingCharacterNavsToDetails() {
        val name = "TestCharacter"
        val fortune = 25
        insertNewCharacter(name, fortune)
        Thread.sleep(10000)
        name.isDisplayed()

        name.click()

        R.id.layout_fragment_character_details.isDisplayed()
    }

    private fun insertNewCharacter(name: String, fortune: Int) {
        val model = Character(0, name, fortune)
        runBlocking { characterRepository.persist(model) }
    }
}
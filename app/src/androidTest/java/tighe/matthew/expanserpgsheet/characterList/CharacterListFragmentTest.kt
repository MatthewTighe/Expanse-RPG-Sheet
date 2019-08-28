package tighe.matthew.expanserpgsheet.characterList

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tighe.matthew.expanserpgsheet.*
import tighe.matthew.expanserpgsheet.model.CharacterModel
import tighe.matthew.expanserpgsheet.repository.CharacterRepository

@RunWith(AndroidJUnit4::class)
class CharacterListFragmentTest {
    @get:Rule val activityRule = ActivityTestRule(MainActivity::class.java)

    private lateinit var characterRepository: CharacterRepository
    private val model = CharacterModel("name", 10)

    @Before
    fun setup() {
        characterRepository = CharacterRepository(activityRule.activity)
        characterRepository.persist(model)
    }

    @Test
    fun testAddButtonNavsToCreation() {
        R.id.layout_fragment_character_list.isDisplayed()

        R.id.btn_create_new.click()

        R.id.layout_fragment_character_creation.isDisplayed()
    }

    // TODO These tests can't be written until a migration to an observable model is made or a refresh button is added.
//    @Test
//    fun clickingOptionsShouldOpenMenuNotNav() {
//        R.id.layout_fragment_character_list.isDisplayed()
//
//        R.id.btn_options.click()
//
//        R.id.delete.isDisplayed()
//        R.id.character_details_fragment.isNotDisplayed()
//    }

//    @Test
//    fun clickingCharacterNavsToDetails() {
//        val name = "TestCharacter"
//        val fortune = 25
//        insertNewCharacter(name, fortune)
//        Thread.sleep(10000)
//        name.isDisplayed()
//
//        name.click()
//
//        R.id.layout_fragment_character_details.isDisplayed()
//    }
//
//    private fun insertNewCharacter(name: String, fortune: Int) {
//        val model = CharacterModel(name, fortune)
//        characterRepository.persist(model)
//    }
}
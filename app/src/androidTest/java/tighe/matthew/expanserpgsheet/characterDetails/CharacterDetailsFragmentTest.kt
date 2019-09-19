package tighe.matthew.expanserpgsheet.characterDetails

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject
import tighe.matthew.expanserpgsheet.*
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository

@RunWith(AndroidJUnit4::class)
class CharacterDetailsFragmentTest : KoinTest {
    @get:Rule val activityRule = ActivityTestRule(MainActivity::class.java)

    private val initialMaxFortune = 25
    private val initialCurrentFortune = 35
    private val initialModel =
        Character(1, "name", initialMaxFortune, initialCurrentFortune)

    private val characterRepository by inject<CharacterRepository>()

    private val navArgs = listOf(NavigationArgument("characterId", 1L))

    @Before
    fun setup() {
        runBlocking {
            characterRepository.persist(initialModel)
        }
        activityRule.navTo(R.id.character_details_fragment, navArgs)
    }

    @Test
    fun maxFortuneCanBeAlteredAndIsPersisted() = runBlocking {
        initialMaxFortune.toString().isDisplayed()

        val updatedFortune = initialMaxFortune + 20
        R.id.details_input_max_fortune.writeText(updatedFortune.toString())
        updatedFortune.toString().isDisplayed()

        val result = characterRepository.load(initialModel.id)

        val expected = initialModel.copy(maxFortune = updatedFortune)
        assertEquals(expected, result)
    }

    @Test
    fun currentFortuneCanBeAlteredAndIsPersisted() = runBlocking {
        initialCurrentFortune.toString().isDisplayed()

        val updatedFortune = initialCurrentFortune + 10
        R.id.details_input_current_fortune.writeText(updatedFortune.toString())
        updatedFortune.toString().isDisplayed()

        val result = characterRepository.load(initialModel.id)

        val expected = initialModel.copy(currentFortune = updatedFortune)
        assertEquals(expected, result)
    }
}
package tighe.matthew.expanserpgsheet.characterDetails

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
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
import tighe.matthew.expanserpgsheet.model.condition.Condition

@RunWith(AndroidJUnit4::class)
class CharacterDetailsFragmentTest : KoinTest {
    @get:Rule val activityRule = ActivityTestRule(MainActivity::class.java)

    private val initialMaxFortune = 25
    private val initialCurrentFortune = 35
    private val initialAttributes = Attributes(1, 2, 3, 4, 5, 6, 7, 8, 9)
    private val initialModel =
        Character(1, "name", initialMaxFortune, initialCurrentFortune, attributes = initialAttributes)

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

    @Test
    fun conditionsCanBeAlteredAndArePersisted() = runBlocking {
        R.id.chip_injured.click()

        val result = characterRepository.observeWithConditions().first()

        val expectedConditions = setOf(Condition.Injured)
        val expected = listOf(initialModel.copy(conditions = expectedConditions))
        assertEquals(expected, result)
    }

    @Test
    fun attributesCanBeAlteredAndArePersisted() = runBlocking {
        R.id.input_accuracy.writeText("10")

        val result = characterRepository.observeBase().first()
        val realResult = characterRepository.observeBase().first()

        val expectedAttributes = initialAttributes.copy(accuracy = 10)
        val expectedCharacter = initialModel.copy(attributes = expectedAttributes)
        assertEquals(listOf(expectedCharacter), realResult)
    }
}
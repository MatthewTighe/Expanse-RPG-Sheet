package tighe.matthew.expanserpgsheet.encounter

import androidx.test.rule.ActivityTestRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tighe.matthew.expanserpgsheet.*
import tighe.matthew.expanserpgsheet.model.character.Attributes
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository
import tighe.matthew.expanserpgsheet.model.condition.Condition
import tighe.matthew.expanserpgsheet.model.encounter.EncounterCharacter
import tighe.matthew.expanserpgsheet.model.encounter.EncounterRepository

class EncounterFragmentTest : KoinTest {

    @get:Rule val activityRule = ActivityTestRule(MainActivity::class.java)

    private val characterRepository by inject<CharacterRepository>()
    private val encounterRepository by inject<EncounterRepository>()

    private val testInitiative = 10
    private val testCharacterId = 1L
    private val originalFortune = 10
    private val testAttributes = Attributes(1, 2, 3, 4, 5, 6, 7, 8, 9)
    private val testCharacter = Character(testCharacterId, "name", originalFortune, attributes = testAttributes)

    @Before
    fun setup() {
        runBlocking {
            characterRepository.persist(testCharacter)
            encounterRepository.addCharacter(testCharacter, testInitiative)
        }
        R.id.encounter_fragment.click()
    }

    @Test
    fun textFieldsAreDisplayed() {
        testCharacter.name.isDisplayed()
        testCharacter.defense.toString().isDisplayed()
        testCharacter.toughness.toString().isDisplayed()
    }

    @Test
    fun characterFortuneCanBeIncremented() {
        originalFortune.toString().isDisplayed()

        R.id.btn_inc_fortune.click()

        (originalFortune + 1).toString().isDisplayed()
    }

    @Test
    fun characterFortuneCanBeDecremented() {
        originalFortune.toString().isDisplayed()

        R.id.btn_dec_fortune.click()

        (originalFortune - 1).toString().isDisplayed()
    }

    @Test
    fun characterFortuneCanBeDirectlyAlteredAndIsPersisted() {
        originalFortune.toString().isDisplayed()

        val updatedFortune = originalFortune + 10
        R.id.edit_text_fortune.writeText(updatedFortune.toString())

        R.id.character_list_fragment.click()
        R.id.encounter_fragment.click()

        updatedFortune.toString().isDisplayed()
    }

    @Test
    fun characterConditionsCanBeAlteredAndIsPersisted() = runBlocking {
        R.id.chip_injured.click()

        val result = characterRepository.observeWithConditions().first()

        val expectedConditions = setOf(Condition.Injured)
        val expected = listOf(testCharacter.copy(conditions = expectedConditions))
        assertEquals(expected, result)
    }

    @Test
    fun clearAllOptionRemovesAllCharacters() = runBlocking {
        val testCharacter2 = testCharacter.copy(id = 2, name = "bobbie")
        characterRepository.persist(testCharacter2)
        encounterRepository.addCharacter(testCharacter2, testInitiative + 1)

        R.id.item_clear_all.menuClick()

        val result = encounterRepository.getEncounter().first()

        assertEquals(listOf<EncounterCharacter>(), result)
    }

    @Test
    fun attributesAreDisplayed() {
        "ACR: 1".isDisplayed()
        "COM: 2".isDisplayed()
        "CON: 3".isDisplayed()
        "DEX: 4".isDisplayed()
        "FTN: 5".isDisplayed()
        "INT: 6".isDisplayed()
        "PER: 7".isDisplayed()
        "STR: 8".isDisplayed()
        "WIL: 9".isDisplayed()
    }

    @Test
    fun listDoesNotDoesDisappearIfNavItemIsClicked() {
        testCharacter.name.isDisplayed()

        R.id.encounter_fragment.click()

        testCharacter.name.isDisplayed()
    }
}
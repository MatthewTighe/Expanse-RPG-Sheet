package tighe.matthew.expanserpgsheet.encounter

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject
import tighe.matthew.expanserpgsheet.*
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository
import tighe.matthew.expanserpgsheet.model.encounter.EncounterRepository

@RunWith(AndroidJUnit4::class)
class EncounterFragmentTest : KoinTest {

    @get:Rule val activityRule = ActivityTestRule(MainActivity::class.java)

    private val characterRepository by inject<CharacterRepository>()
    private val encounterRepository by inject<EncounterRepository>()

    private val testInitiative = 10
    private val testCharacterId = 1L
    private val originalFortune = 10
    private val testCharacter = Character(testCharacterId, "name", originalFortune)

    @Before
    fun setup() {
        runBlocking {
            characterRepository.persist(testCharacter)
            encounterRepository.addCharacter(testCharacter, testInitiative)
        }
        R.id.encounter_fragment.click()
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
}
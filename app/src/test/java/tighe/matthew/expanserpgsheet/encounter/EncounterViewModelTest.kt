package tighe.matthew.expanserpgsheet.encounter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tighe.matthew.expanserpgsheet.generateEncounterCharacter
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository
import tighe.matthew.expanserpgsheet.model.condition.Condition
import tighe.matthew.expanserpgsheet.model.encounter.EncounterDetail
import tighe.matthew.expanserpgsheet.model.encounter.EncounterCharacter
import tighe.matthew.expanserpgsheet.model.encounter.EncounterRepository

class EncounterViewModelTest {
    @get:Rule val rule = InstantTaskExecutorRule()
    @UseExperimental
    private val mainThreadSurrogate = newSingleThreadContext("Main")

    @MockK private lateinit var mockEncounterRepo: EncounterRepository

    @MockK private lateinit var mockCharacterRepo: CharacterRepository

    @MockK private lateinit var mockViewStateObserver: Observer<EncounterViewState>

    private lateinit var viewModel: EncounterViewModel

    private val testCharacter = Character(0, "name", 10)
    private val testDetail = EncounterDetail(3, 2, 0)
    private val testEncounterCharacter = EncounterCharacter(testCharacter, testDetail)
    private val testList = listOf(testEncounterCharacter)

    @Before
    @ExperimentalCoroutinesApi
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = EncounterViewModel(mockEncounterRepo, mockCharacterRepo)
    }

    @Test
    fun `ViewState is updated with encounter information`() = runBlockingTest {
        coEvery { mockEncounterRepo.getEncounter() } returns flow {
            emit(testList)
        }

        viewModel.observeViewState().observeForever(mockViewStateObserver)

        coVerify { mockViewStateObserver.onChanged(EncounterViewState(testList)) }
    }

    @Test
    fun `Increment action increases current fortune`() {
        coEvery { mockEncounterRepo.getEncounter() } returns flow {
            emit(testList)
        }

        viewModel.submitAction(EncounterAction.IncrementFortune(testEncounterCharacter))

        val expectedCharacter = testCharacter.copy(currentFortune = testCharacter.currentFortune + 1)
        val expected = testEncounterCharacter.copy(character = expectedCharacter)
        coVerify { mockEncounterRepo.updateEncounterCharacter(expected) }
    }

    @Test
    fun `Decrement action decreases current fortune`() {
        coEvery { mockEncounterRepo.getEncounter() } returns flow {
            emit(testList)
        }

        viewModel.submitAction(EncounterAction.DecrementFortune(testEncounterCharacter))

        val expectedCharacter = testCharacter.copy(currentFortune = testCharacter.currentFortune - 1)
        val expected = testEncounterCharacter.copy(character = expectedCharacter)
        coVerify { mockEncounterRepo.updateEncounterCharacter(expected) }
    }

    @Test
    fun `Set fortune action parses well-formed strings and delegates to model`() {
        coEvery { mockEncounterRepo.getEncounter() } returns flow {
            emit(testList)
        }

        val updatedFortune = testCharacter.currentFortune + 10
        viewModel.submitAction(EncounterAction.SetFortune(updatedFortune.toString(), testEncounterCharacter))

        val expectedCharacter = testCharacter.copy(currentFortune = updatedFortune)
        val expected = testEncounterCharacter.copy(character = expectedCharacter)
        coVerify { mockEncounterRepo.updateEncounterCharacter(expected) }
    }

    @Test
    fun `Set fortune action defaults to 0 with empty strings`() {
        coEvery { mockEncounterRepo.getEncounter() } returns flow {
            emit(testList)
        }

        viewModel.submitAction(EncounterAction.SetFortune("", testEncounterCharacter))

        val expectedCharacter = testCharacter.copy(currentFortune = 0)
        val expected = testEncounterCharacter.copy(character = expectedCharacter)
        coVerify { mockEncounterRepo.updateEncounterCharacter(expected) }
    }

    @Test
    fun `Set fortune action defaults to 0 with malformed strings`() {
        coEvery { mockEncounterRepo.getEncounter() } returns flow {
            emit(testList)
        }

        viewModel.submitAction(EncounterAction.SetFortune("@+%1", testEncounterCharacter))

        val expectedCharacter = testCharacter.copy(currentFortune = 0)
        val expected = testEncounterCharacter.copy(character = expectedCharacter)
        coVerify { mockEncounterRepo.updateEncounterCharacter(expected) }
    }

    @Test
    fun `ConditionChecked action delegates to the character repository`() {
        viewModel.submitAction(EncounterAction.ConditionChecked(Condition.Injured, testEncounterCharacter.character))

        coVerify { mockCharacterRepo.addCondition(Condition.Injured, testEncounterCharacter.character) }
    }

    @Test
    fun `ConditionUnchecked action delegates to the character repository`() {
        viewModel.submitAction(EncounterAction.ConditionUnchecked(Condition.Injured, testEncounterCharacter.character))

        coVerify { mockCharacterRepo.removeCondition(Condition.Injured, testEncounterCharacter.character) }
    }

    @Test
    fun `Moved action finds correct positions and delegates them to repository`() {
        val fromPosition = 0
        val toPosition = 1
        val movedCharacter = generateEncounterCharacter(position = fromPosition)
        val displacedCharacter = generateEncounterCharacter(position = toPosition)

        val action = EncounterAction.CharacterMoved(
            movedCharacter,
            fromPosition,
            displacedCharacter,
            toPosition
        )
        viewModel.submitAction(action)

        val updatedMovedDetails =
            movedCharacter.detail.copy(position = action.toPosition)
        val updatedDisplacedDetails =
            displacedCharacter.detail.copy(position = action.fromPosition)

        val expectedMoved = movedCharacter.copy(detail = updatedMovedDetails)
        val expectedDisplaced = movedCharacter.copy(detail = updatedDisplacedDetails)
        coVerify {
            mockEncounterRepo.updateEncounterCharacter(expectedMoved)
            mockEncounterRepo.updateEncounterCharacter(expectedDisplaced)
        }
    }

    @Test
    fun `Remove action delegates to repository`() {
        val character = generateEncounterCharacter(0)

        viewModel.submitAction(EncounterAction.CharacterRemoved(character, 0))

        coVerify { mockEncounterRepo.removeEncounterCharacter(character, 0) }
    }

    @Test
    fun `Clear all action delegates to repository`() {
        viewModel.submitAction(EncounterAction.ClearAll)

        coVerify { mockEncounterRepo.removeAll() }
    }
}
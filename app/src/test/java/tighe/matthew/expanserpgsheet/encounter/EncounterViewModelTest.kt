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
import tighe.matthew.expanserpgsheet.model.encounter.CharacterEncounterDetail
import tighe.matthew.expanserpgsheet.model.encounter.Encounter
import tighe.matthew.expanserpgsheet.model.encounter.EncounterCharacter
import tighe.matthew.expanserpgsheet.model.encounter.EncounterRepository

class EncounterViewModelTest {
    @get:Rule val rule = InstantTaskExecutorRule()
    private val mainThreadSurrogate = newSingleThreadContext("Main")

    @MockK private lateinit var mockEncounterRepo: EncounterRepository

    @MockK private lateinit var mockViewStateObserver: Observer<EncounterViewState>

    private lateinit var viewModel: EncounterViewModel

    private val testCharacter = Character(0, "name", 10)
    private val testDetail = CharacterEncounterDetail(3, 2, 0)
    private val testEncounterCharacter = EncounterCharacter(testCharacter, testDetail)
    private val testEncounter = Encounter(listOf(testEncounterCharacter))

    @Before
    @ExperimentalCoroutinesApi
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = EncounterViewModel(mockEncounterRepo)
    }

    @Test
    fun `ViewState is updated with encounter information`() = runBlockingTest {
        coEvery { mockEncounterRepo.getEncounter() } returns flow {
            emit(testEncounter)
        }

        viewModel.observeViewState().observeForever(mockViewStateObserver)

        coVerify { mockViewStateObserver.onChanged(EncounterViewState(testEncounter)) }
    }

    @Test
    fun `Increment action increases current fortune`() {
        coEvery { mockEncounterRepo.getEncounter() } returns flow {
            emit(testEncounter)
        }

        viewModel.submitAction(EncounterAction.IncrementFortune(testEncounterCharacter))

        val expectedCharacter = testCharacter.copy(currentFortune = testCharacter.currentFortune + 1)
        val expected = testEncounterCharacter.copy(character = expectedCharacter)
        coVerify { mockEncounterRepo.updateEncounterCharacter(expected) }
    }

    @Test
    fun `Decrement action decreases current fortune`() {
        coEvery { mockEncounterRepo.getEncounter() } returns flow {
            emit(testEncounter)
        }

        viewModel.submitAction(EncounterAction.DecrementFortune(testEncounterCharacter))

        val expectedCharacter = testCharacter.copy(currentFortune = testCharacter.currentFortune - 1)
        val expected = testEncounterCharacter.copy(character = expectedCharacter)
        coVerify { mockEncounterRepo.updateEncounterCharacter(expected) }
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
}
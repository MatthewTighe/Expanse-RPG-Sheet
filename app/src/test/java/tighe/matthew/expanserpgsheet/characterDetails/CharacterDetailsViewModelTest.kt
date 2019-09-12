package tighe.matthew.expanserpgsheet.characterDetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository

class CharacterDetailsViewModelTest {
    @get:Rule val rule = InstantTaskExecutorRule()
    private val mainThreadSurrogate = newSingleThreadContext("Main")

    private val mockViewStateObserver = mockk<Observer<CharacterDetailsViewState>>(relaxUnitFun = true)
    private val mockRepo = mockk<CharacterRepository>(relaxUnitFun = true)

    private val testInitialFortune = 10
    private val testInitialCharacterModel =
        Character(0, "name", testInitialFortune)

    private lateinit var viewModel: CharacterDetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)

        coEvery { mockRepo.load(0) } returns testInitialCharacterModel
        coEvery { mockRepo.observeAll() } returns flow {
            emit(listOf(testInitialCharacterModel))
        }

        viewModel = CharacterDetailsViewModel(0, mockRepo)
        viewModel.observeViewState().observeForever(mockViewStateObserver)
    }

    @Test
    fun `Changes to max fortune are delegated to the model`() {
        val newFortune = 20
        viewModel.submitAction(CharacterDetailsAction.ChangeMaxFortune(newFortune))

        val expected = testInitialCharacterModel.copy(maxFortune = newFortune)
        coVerify { mockRepo.update(expected) }
    }

    @Test
    fun `Changes to current fortune are delegated to the model`() {
        val newFortune = 20
        viewModel.submitAction(CharacterDetailsAction.ChangeCurrentFortune(20))

        val expected = testInitialCharacterModel.copy(currentFortune = newFortune)
        coVerify { mockRepo.update(expected) }
    }

    @Test
    fun `Changes to the model are propagated to an observer`() {
        verify {
            mockViewStateObserver.onChanged(CharacterDetailsViewState(testInitialCharacterModel))
        }
    }
}
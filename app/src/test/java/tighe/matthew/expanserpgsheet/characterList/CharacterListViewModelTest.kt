package tighe.matthew.expanserpgsheet.characterList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository
import tighe.matthew.expanserpgsheet.model.encounter.EncounterRepository

class CharacterListViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()
    private val mainThreadSurrogate = newSingleThreadContext("Main")

    private val mockCharacterRepo = mockk<CharacterRepository>(relaxUnitFun = true)
    private val mockEncounterRepo = mockk<EncounterRepository>(relaxUnitFun = true)
    private val mockViewStateObserver = mockk<Observer<CharacterListViewState>>(relaxUnitFun = true)
    private val mockEventObserver = mockk<Observer<Event>>(relaxUnitFun = true)

    private lateinit var viewModel: CharacterListViewModel

    @Before
    @ExperimentalCoroutinesApi
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)

        viewModel = CharacterListViewModel(mockCharacterRepo, mockEncounterRepo)
        viewModel.observeEvent().observeForever(mockEventObserver)
    }

    @Test
    fun `Refresh action updates view and loads from the repository`() = runBlockingTest {
        val char1 = Character(0, "name1", 10)
        val char2 = Character(0, "name2", 15)
        val expectedList = listOf(char1, char2)

        coEvery { mockCharacterRepo.observeAll() } returns flow {
            emit(expectedList)
        }

        viewModel.observeViewState().observeForever(mockViewStateObserver)

        coVerify {
            mockCharacterRepo.observeAll()
            mockViewStateObserver.onChanged(CharacterListViewState(expectedList))
        }

        confirmVerified(mockCharacterRepo, mockViewStateObserver)
    }

    @Test
    fun `Add action navigates to characterCreation`() {
        viewModel.submitAction(CharacterListAction.Add)

        verify { mockEventObserver.onChanged(Event.Navigate(R.id.character_creation_fragment)) }
    }

    @Test
    fun `Delete action delegates to model`() {
        val characterModel = Character(0, "test1", 15)
        viewModel.submitAction(CharacterListAction.Delete(characterModel))

        coVerify { mockCharacterRepo.delete(characterModel) }
    }

    @Test
    fun `Character clicked navigates to character details with bundled character`() {
        val model = Character(0, "name", 15)

        viewModel.submitAction(CharacterListAction.CharacterClicked(model))

        val expectedArgs = listOf(model.buildNavArg())
        verify { mockEventObserver.onChanged(Event.Navigate(R.id.character_details_fragment, expectedArgs)) }
    }
}
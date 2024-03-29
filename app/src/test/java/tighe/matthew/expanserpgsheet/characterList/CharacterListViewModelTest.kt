package tighe.matthew.expanserpgsheet.characterList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.NavigationArgument
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository
import tighe.matthew.expanserpgsheet.model.encounter.EncounterRepository

class CharacterListViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()
    @UseExperimental
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
    fun `ViewState is initialized using repository data`() = runBlockingTest {
        val char1 = Character(0, "name1", 10)
        val char2 = Character(0, "name2", 15)
        val expectedList = listOf(char1, char2)

        coEvery { mockCharacterRepo.observeBase() } returns flow {
            emit(expectedList)
        }

        viewModel.observeViewState().observeForever(mockViewStateObserver)

        coVerify {
            mockCharacterRepo.observeBase()
            mockViewStateObserver.onChanged(CharacterListViewState(listOf()))
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

        val navArg = NavigationArgument("characterId", model.id)
        verify {
            mockEventObserver.onChanged(
                Event.Navigate(R.id.character_details_fragment, listOf(navArg))
            )
        }
    }

    @Test
    fun `Snackbar event is posted if a character is already in an encounter`() {
        val model = Character(0, "name", 15)

        coEvery { mockEncounterRepo.characterIsInEncounter(model) } returns true

        viewModel.submitAction(CharacterListAction.AddToEncounterClicked(model))

        mockEventObserver.onChanged(Event.Snackbar(R.string.character_already_in_encounter))
    }

    @Test
    fun `Clicking add to encounter posts viewstate that dialog should be displayed`() {
        val model = Character(0, "name", 15)

        coEvery { mockEncounterRepo.characterIsInEncounter(model) } returns false
        coEvery { mockCharacterRepo.observeBase() } returns flow {
            emit(listOf(model))
        }
        viewModel.observeViewState().observeForever(mockViewStateObserver)

        viewModel.submitAction(CharacterListAction.AddToEncounterClicked(model))

        val expected = CharacterListViewState(listOf(model), true, model)
        verify { mockViewStateObserver.onChanged(expected) }
    }

    @Test
    fun `Snackbar event is posted once character is added to encounter`() {
        val model = Character(0, "name", 15)

        viewModel.submitAction(CharacterListAction.InitiativeEntered(0, model))

        mockEventObserver.onChanged(Event.Snackbar(R.string.character_already_in_encounter))
    }

    @Test
    fun `Submitting initiative should clear dialog display flag`() {
        val model = Character(0, "name", 15)

        coEvery { mockEncounterRepo.characterIsInEncounter(model) } returns false
        coEvery { mockCharacterRepo.observeBase() } returns flow {
            emit(listOf(model))
        }
        viewModel.observeViewState().observeForever(mockViewStateObserver)

        viewModel.submitAction(CharacterListAction.AddToEncounterClicked(model))
        viewModel.submitAction(CharacterListAction.InitiativeEntered(0, model))

        val first = CharacterListViewState(listOf(model), true, model)
        val final = CharacterListViewState(listOf(model), false, null)
        verify {
            mockViewStateObserver.onChanged(first)
            mockViewStateObserver.onChanged(final)
        }
    }
}
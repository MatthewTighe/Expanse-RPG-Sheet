package tighe.matthew.expanserpgsheet.characterList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository

class CharacterListViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()
    @ExperimentalCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("Main")

    private val mockRepo = mockk<CharacterRepository>(relaxUnitFun = true)
    private val mockViewStateObserver = mockk<Observer<CharacterListViewState>>(relaxUnitFun = true)
    private val mockEventObserver = mockk<Observer<Event>>(relaxUnitFun = true)

    private val charactersLiveData = MutableLiveData<List<Character>>()

    private lateinit var viewModel: CharacterListViewModel

    @Before
    @ExperimentalCoroutinesApi
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)

        viewModel = CharacterListViewModel(mockRepo)
        viewModel.observeViewState().observeForever(mockViewStateObserver)
        viewModel.observeEvent().observeForever(mockEventObserver)
    }

    @Test
    fun `Refresh action updates view and loads from the repository`() {
        val char1 = Character(0, "name1", 10)
        val char2 = Character(0, "name2", 15)
        val expectedList = listOf(char1, char2)

        coEvery { mockRepo.observeAll() } returns charactersLiveData

        charactersLiveData.postValue(expectedList)
        viewModel.submitAction(CharacterListAction.Refresh)

        coVerify {
            mockRepo.observeAll()
            mockViewStateObserver.onChanged(CharacterListViewState(loading = true, characterList = listOf()))
            mockViewStateObserver.onChanged(CharacterListViewState(loading = false, characterList = expectedList))
        }

        confirmVerified(mockRepo, mockViewStateObserver)
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

        coVerify { mockRepo.delete(characterModel) }
    }

    @Test
    fun `Character clicked navigates to character details with bundled character`() {
        val model = Character(0, "name", 15)

        viewModel.submitAction(CharacterListAction.CharacterClicked(model))

        val expectedArgs = listOf(model.buildNavArg())
        verify { mockEventObserver.onChanged(Event.Navigate(R.id.character_details_fragment, expectedArgs)) }
    }
}
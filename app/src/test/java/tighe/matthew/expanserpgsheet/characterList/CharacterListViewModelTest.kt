package tighe.matthew.expanserpgsheet.characterList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.model.CharacterModel
import tighe.matthew.expanserpgsheet.repository.CharacterRepository

class CharacterListViewModelTest {
    @get:Rule val rule = InstantTaskExecutorRule()

    private val mockRepo = mockk<CharacterRepository>(relaxUnitFun = true)
    private val mockViewStateObserver = mockk<Observer<CharacterListViewState>>(relaxUnitFun = true)
    private val mockEventObserver = mockk<Observer<Event>>(relaxUnitFun = true)

    private lateinit var viewModel: CharacterListViewModel

    @Before
    fun setup() {
        viewModel = CharacterListViewModel(mockRepo)
        viewModel.observeViewState().observeForever(mockViewStateObserver)
        viewModel.observeEvent().observeForever(mockEventObserver)
    }

    @Test
    fun `Add action navigates to characterCreation`() {
        viewModel.submitAction(CharacterListAction.Add)

        verify { mockEventObserver.onChanged(Event.Navigate(R.id.characterCreationFragment)) }
    }

    @Test
    fun `Character clicked navigates to character details with bundled character`() {
        val model = CharacterModel("name", 15)

        viewModel.submitAction(CharacterListAction.CharacterClicked(model))

        val expectedArgs = listOf(model.buildNavArg())
        verify { mockEventObserver.onChanged(Event.Navigate(R.id.characterDetailsFragment, expectedArgs)) }
    }

    @Test
    fun `Refresh action updates view and loads from the repository`() {
        val char1 = CharacterModel("name1", 10)
        val char2 = CharacterModel("name2", 15)
        val expectedList = listOf(char1, char2)

        every { mockRepo.loadAll() } returns expectedList

        viewModel.submitAction(CharacterListAction.Refresh)

        verify {
            mockRepo.loadAll()
            mockViewStateObserver.onChanged(CharacterListViewState(loading = true, characterList = listOf()))
            mockViewStateObserver.onChanged(CharacterListViewState(loading = false, characterList = expectedList))
        }

        confirmVerified(mockRepo, mockViewStateObserver)
    }
}
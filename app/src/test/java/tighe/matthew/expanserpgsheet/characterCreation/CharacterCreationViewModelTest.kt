package tighe.matthew.expanserpgsheet.characterCreation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import tighe.matthew.expanserpgsheet.*
import tighe.matthew.expanserpgsheet.model.CharacterModel
import tighe.matthew.expanserpgsheet.repository.CharacterRepository

class CharacterCreationViewModelTest {
    @get:Rule val rule = InstantTaskExecutorRule()

    private val mockEventObserver = mockk<Observer<Event?>>(relaxUnitFun = true)
    private val mockViewStateObserver = mockk<Observer<ViewState>>(relaxed = true)
    private val mockRepo = mockk<CharacterRepository>(relaxUnitFun = true)

    private lateinit var viewModel: CharacterCreationViewModel

    @Before
    fun setup() {
        viewModel = CharacterCreationViewModel(mockRepo)
        viewModel.observeEvent().observeForever(mockEventObserver)
        viewModel.observeViewState().observeForever(mockViewStateObserver)
    }

    @Test
    fun `Save action persists model to repository`() {
        val model = CharacterModel("name", 10)

        viewModel.submitAction(CharacterCreationAction.NameInput(model.name))
        viewModel.submitAction(CharacterCreationAction.MaxFortuneInput(model.maxFortune))
        viewModel.submitAction(CharacterCreationAction.Save)

        verify { mockRepo.persist(model) }
        verify { mockEventObserver.onChanged(Event.Navigate(R.id.character_list_fragment)) }
    }

    @Test
    fun `Save action triggers errors in view state if name field is empty`() {
        viewModel.submitAction(CharacterCreationAction.Save)

        val expected = CharacterCreationViewState(nameError = NameError(errorEnabled = true))
        verify { mockViewStateObserver.onChanged(expected) }
    }

    @Test
    fun `Updating name input with a blank name triggers error`() {
        viewModel.submitAction(CharacterCreationAction.NameInput(""))

        val expected = CharacterCreationViewState(nameError = NameError(errorEnabled = true))
        verify { mockViewStateObserver.onChanged(expected) }
    }

    @Test
    fun `Updating name while error is present removes error`() {
        viewModel.submitAction(CharacterCreationAction.NameInput(""))
        viewModel.submitAction(CharacterCreationAction.NameInput("name"))

        val expectedError = CharacterCreationViewState(nameError = NameError(errorEnabled = true))
        val expected = CharacterCreationViewState(nameError = NameError(errorEnabled = false))
        verify {
            mockViewStateObserver.onChanged(expectedError)
            mockViewStateObserver.onChanged(expected)
        }
    }
}
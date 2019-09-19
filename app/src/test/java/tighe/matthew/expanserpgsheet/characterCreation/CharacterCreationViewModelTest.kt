package tighe.matthew.expanserpgsheet.characterCreation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tighe.matthew.expanserpgsheet.*
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository

class CharacterCreationViewModelTest {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    @UseExperimental
    private val mainThreadSurrogate = newSingleThreadContext("Main")

    private val mockEventObserver = mockk<Observer<Event?>>(relaxUnitFun = true)
    private val mockViewStateObserver = mockk<Observer<ViewState>>(relaxed = true)
    private val mockRepo = mockk<CharacterRepository>(relaxUnitFun = true)

    private lateinit var viewModel: CharacterCreationViewModel

    @Before
    @ExperimentalCoroutinesApi
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)

        viewModel = CharacterCreationViewModel(mockRepo)
        viewModel.observeEvent().observeForever(mockEventObserver)
        viewModel.observeViewState().observeForever(mockViewStateObserver)
    }

    @Test
    fun `Save action persists model to repository`() {
        val model = Character(0, "name", 10)

        viewModel.submitAction(CharacterCreationAction.NameInput(model.name))
        viewModel.submitAction(CharacterCreationAction.MaxFortuneInput(model.maxFortune))
        viewModel.submitAction(CharacterCreationAction.Save)

        coVerify { mockRepo.persist(model) }
        verify { mockEventObserver.onChanged(Event.Navigate(R.id.character_list_fragment)) }
    }

    @Test
    fun `Save action triggers errors in view state if name field is empty`() {
        viewModel.submitAction(CharacterCreationAction.Save)

        val expected = CharacterCreationViewState(nameError = NameError(errorEnabled = true))
        verify { mockViewStateObserver.onChanged(expected) }
    }

    @Test
    fun `Current fortune will match max on save action`() {
        val model = Character(0, "name", 10)
        val modifiedFortune = 15

        viewModel.submitAction(CharacterCreationAction.NameInput(model.name))
        viewModel.submitAction(CharacterCreationAction.MaxFortuneInput(model.maxFortune))
        viewModel.submitAction(CharacterCreationAction.MaxFortuneInput(modifiedFortune))
        viewModel.submitAction(CharacterCreationAction.Save)

        coVerify { mockRepo.persist(model.copy(currentFortune = 15, maxFortune = 15)) }
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
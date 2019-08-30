package tighe.matthew.expanserpgsheet.characterDetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository

class CharacterDetailsViewModelTest {
    @get:Rule val rule = InstantTaskExecutorRule()

    val mockViewStateObserver = mockk<Observer<CharacterDetailsViewState>>(relaxUnitFun = true)
    val mockRepo = mockk<CharacterRepository>(relaxUnitFun = true)

    private val testInitialFortune = 10
    private val testInitialCharacterModel =
        Character(0, "name", testInitialFortune)

    private lateinit var viewModel: CharacterDetailsViewModel

    @Before
    fun setup() {
        viewModel = CharacterDetailsViewModel(mockRepo)
        viewModel.observeViewState().observeForever(mockViewStateObserver)
    }

    @Test
    fun `ViewState is updated once a character is received`() {
        viewModel.submitAction(CharacterDetailsAction.CharacterReceived(testInitialCharacterModel))

        val expectedViewState = CharacterDetailsViewState(testInitialFortune)
        verify { mockViewStateObserver.onChanged(expectedViewState) }
    }

    @Test
    fun `ViewState is can be incremented`() {
        viewModel.submitAction(CharacterDetailsAction.CharacterReceived(testInitialCharacterModel))
        viewModel.submitAction(CharacterDetailsAction.IncrementFortune(10))

        val expectedFortune = 20
        val expectedViewState = CharacterDetailsViewState(expectedFortune)
        verify { mockViewStateObserver.onChanged(expectedViewState) }
    }

    @Test
    fun `ViewState is can be decrement`() {
        viewModel.submitAction(CharacterDetailsAction.CharacterReceived(testInitialCharacterModel))
        viewModel.submitAction(CharacterDetailsAction.DecrementFortune(10))

        val expectedFortune = 0
        val expectedViewState = CharacterDetailsViewState(expectedFortune)
        verify { mockViewStateObserver.onChanged(expectedViewState) }
    }
}
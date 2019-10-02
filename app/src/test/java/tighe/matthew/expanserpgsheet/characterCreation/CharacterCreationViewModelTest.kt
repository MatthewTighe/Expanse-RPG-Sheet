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
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.ViewState
import tighe.matthew.expanserpgsheet.attributes.AttributeError
import tighe.matthew.expanserpgsheet.attributes.AttributeInput
import tighe.matthew.expanserpgsheet.model.character.AttributeType
import tighe.matthew.expanserpgsheet.model.character.Attributes
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository

class CharacterCreationViewModelTest {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val mainThreadSurrogate = newSingleThreadContext("Main")

    private val mockEventObserver = mockk<Observer<Event?>>(relaxUnitFun = true)
    private val mockViewStateObserver = mockk<Observer<ViewState>>(relaxed = true)
    private val mockRepo = mockk<CharacterRepository>(relaxUnitFun = true)

    private lateinit var viewModel: CharacterCreationViewModel

    private val testAttributes = Attributes(1, 2, 3, 4, 5, 6, 7, 8, 9)
    private val testCharacter = Character(0, "name", 10, attributes = testAttributes)

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
        viewModel.submitAction(CharacterCreationAction.NameChanged(testCharacter.name))
        viewModel.submitAction(CharacterCreationAction.MaxFortuneChanged(testCharacter.maxFortune.toString()))
        submitAttributes(testAttributes)

        viewModel.submitAction(CharacterCreationAction.Save)

        coVerify { mockRepo.persist(testCharacter) }
        verify { mockEventObserver.onChanged(Event.Navigate(R.id.character_list_fragment)) }
    }

    @Test
    fun `Save action triggers errors in view state if fields are empty`() {
        viewModel.submitAction(CharacterCreationAction.Save)

        val attributeErrors = Attributes().map {
            AttributeError(it.type, errorEnabled = true)
        }
        val expected = CharacterCreationViewState(
            nameError = NameError(errorEnabled = true),
            attributeErrors = attributeErrors
        )
        verify { mockViewStateObserver.onChanged(expected) }
    }

    @Test
    fun `Current fortune will match max on save action`() {
        val model = Character(0, "name", 15, attributes = testAttributes)

        viewModel.submitAction(CharacterCreationAction.NameChanged(model.name))
        viewModel.submitAction(CharacterCreationAction.MaxFortuneChanged(model.maxFortune.toString()))
        submitAttributes(testAttributes)
        viewModel.submitAction(CharacterCreationAction.Save)

        coVerify { mockRepo.persist(model.copy(currentFortune = 15, maxFortune = 15)) }
    }

    @Test
    fun `Updating name input with a blank name triggers error`() {
        viewModel.submitAction(CharacterCreationAction.NameChanged(""))

        val expected = CharacterCreationViewState(nameError = NameError(errorEnabled = true))
        verify { mockViewStateObserver.onChanged(expected) }
    }

    @Test
    fun `Updating name while error is present removes error`() {
        viewModel.submitAction(CharacterCreationAction.NameChanged(""))
        viewModel.submitAction(CharacterCreationAction.NameChanged(testCharacter.name))

        val expectedError = CharacterCreationViewState(nameError = NameError(errorEnabled = true))
        val expected = CharacterCreationViewState(nameError = NameError(errorEnabled = false))
        verify {
            mockViewStateObserver.onChanged(expectedError)
            mockViewStateObserver.onChanged(expected)
        }
    }

    @Test
    fun `Updating attribute with blank input triggers error`() {
        val initialInput =
            AttributeInput(AttributeType.ACCURACY, "10")
        val blankInput =
            AttributeInput(AttributeType.ACCURACY, "")

        viewModel.submitAction(CharacterCreationAction.AttributeChanged(initialInput))
        viewModel.submitAction(CharacterCreationAction.AttributeChanged(blankInput))

        val expectedError = AttributeError(
            AttributeType.ACCURACY,
            errorEnabled = true
        )
        val expectedErrorState = CharacterCreationViewState(attributeErrors = listOf(expectedError))

        verify {
            mockViewStateObserver.onChanged(expectedErrorState)
        }
    }

    @Test
    fun `Updating attribute while error is present disables it`() {
        val initialInput =
            AttributeInput(AttributeType.ACCURACY, "")
        val filledInput =
            AttributeInput(AttributeType.ACCURACY, "10")

        viewModel.submitAction(CharacterCreationAction.AttributeChanged(initialInput))
        viewModel.submitAction(CharacterCreationAction.AttributeChanged(filledInput))

        val expectedError = AttributeError(
            AttributeType.ACCURACY,
            errorEnabled = true
        )
        val disabledError = AttributeError(
            AttributeType.ACCURACY,
            errorEnabled = false
        )
        val expectedErrorState = CharacterCreationViewState(attributeErrors = listOf(expectedError))
        val expectedFixedState = CharacterCreationViewState(attributeErrors = listOf(disabledError))

        verify {
            mockViewStateObserver.onChanged(expectedErrorState)
            mockViewStateObserver.onChanged(expectedFixedState)
        }
    }

    private fun submitAttributes(attributes: Attributes, exclude: List<AttributeType> = listOf()) {
        for (attribute in attributes) {
            if (exclude.contains(attribute.type)) continue
            val attributeInput = AttributeInput(
                attribute.type,
                attribute.value.toString()
            )
            viewModel.submitAction(CharacterCreationAction.AttributeChanged(attributeInput))
        }
    }
}
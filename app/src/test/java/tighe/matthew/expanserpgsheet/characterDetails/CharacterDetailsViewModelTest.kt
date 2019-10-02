package tighe.matthew.expanserpgsheet.characterDetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tighe.matthew.expanserpgsheet.attributes.AttributeError
import tighe.matthew.expanserpgsheet.attributes.AttributeInput
import tighe.matthew.expanserpgsheet.model.character.AttributeType
import tighe.matthew.expanserpgsheet.model.character.Attributes
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository
import tighe.matthew.expanserpgsheet.model.condition.Condition

class CharacterDetailsViewModelTest {
    @get:Rule val rule = InstantTaskExecutorRule()
    @UseExperimental
    private val mainThreadSurrogate = newSingleThreadContext("Main")

    private val mockViewStateObserver = mockk<Observer<CharacterDetailsViewState>>(relaxUnitFun = true)
    private val mockRepo = mockk<CharacterRepository>(relaxUnitFun = true)

    private val testInitialFortune = 10
    private val testInitialAttributes = Attributes(1, 2, 3, 4, 5, 6, 7, 8, 9)
    private val testInitialCharacterModel =
        Character(0, "name", testInitialFortune, attributes = testInitialAttributes)

    private lateinit var viewModel: CharacterDetailsViewModel

    @Before
    @ExperimentalCoroutinesApi
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)

        coEvery { mockRepo.load(0) } returns testInitialCharacterModel
        coEvery { mockRepo.observeWithConditions() } returns flow {
            emit(listOf(testInitialCharacterModel))
        }

        viewModel = CharacterDetailsViewModel(0, mockRepo)
        viewModel.observeViewState().observeForever(mockViewStateObserver)
    }

    @Test
    fun `Changes to max fortune are delegated to the model`() {
        val newFortune = 20
        viewModel.submitAction(CharacterDetailsAction.MaxFortuneChanged(newFortune))

        val expected = testInitialCharacterModel.copy(maxFortune = newFortune)
        coVerify { mockRepo.update(expected) }
    }

    @Test
    fun `Changes to current fortune are delegated to the model`() {
        val newFortune = 20
        viewModel.submitAction(CharacterDetailsAction.CurrentFortuneChanged(20))

        val expected = testInitialCharacterModel.copy(currentFortune = newFortune)
        coVerify { mockRepo.update(expected) }
    }

    @Test
    fun `Updating attribute with blank input triggers error`() {
        val initialInput =
            AttributeInput(AttributeType.ACCURACY, "10")
        val blankInput =
            AttributeInput(AttributeType.ACCURACY, "")

        viewModel.submitAction(CharacterDetailsAction.AttributeChanged(initialInput))
        viewModel.submitAction(CharacterDetailsAction.AttributeChanged(blankInput))

        val expectedAttributes = testInitialAttributes.copy(accuracy = Attributes.UNFILLED_ATTRIBUTE)
        val expectedCharacter = testInitialCharacterModel.copy(attributes = expectedAttributes)
        val expectedError = AttributeError(
            AttributeType.ACCURACY,
            errorEnabled = true
        )
        val expectedErrorState = CharacterDetailsViewState(
            character = expectedCharacter,
            attributeErrors = listOf(expectedError)
        )

        verify {
            mockViewStateObserver.onChanged(expectedErrorState)
        }
    }

    @Test
    fun `Updating attribute while error is present disables it`() {
        val initialInput =
            AttributeInput(AttributeType.ACCURACY, "")
        val filledInput = AttributeInput(
            AttributeType.ACCURACY,
            testInitialAttributes.accuracy.toString()
        )

        viewModel.submitAction(CharacterDetailsAction.AttributeChanged(initialInput))
        viewModel.submitAction(CharacterDetailsAction.AttributeChanged(filledInput))

        val expectedAttributes = testInitialAttributes.copy(accuracy = Attributes.UNFILLED_ATTRIBUTE)
        val expectedChangedCharacter = testInitialCharacterModel.copy(attributes = expectedAttributes)
        val expectedError = AttributeError(
            AttributeType.ACCURACY,
            errorEnabled = true
        )
        val disabledError = AttributeError(
            AttributeType.ACCURACY,
            errorEnabled = false
        )

        val expectedErrorState = CharacterDetailsViewState(
            character = expectedChangedCharacter,
            attributeErrors = listOf(expectedError)
        )

        val expectedFixedState = CharacterDetailsViewState(
            character = testInitialCharacterModel,
            attributeErrors = listOf(disabledError)
        )

        verify {
            mockViewStateObserver.onChanged(expectedErrorState)
            mockViewStateObserver.onChanged(expectedFixedState)
        }
    }


    @Test
    fun `ConditionChecked action delegates to repository for adding new condition`() {
        viewModel.submitAction(CharacterDetailsAction.ConditionChecked(Condition.Injured, testInitialCharacterModel))

        coVerify { mockRepo.addCondition(Condition.Injured, testInitialCharacterModel) }
    }

    @Test
    fun `ConditionUnchecked action delegates to repository for removing condition`() {
        viewModel.submitAction(CharacterDetailsAction.ConditionUnchecked(Condition.Injured, testInitialCharacterModel))

        coVerify { mockRepo.removeCondition(Condition.Injured, testInitialCharacterModel) }
    }

    @Test
    fun `Changes to the model are propagated to an observer`() {
        verify {
            mockViewStateObserver.onChanged(CharacterDetailsViewState(testInitialCharacterModel))
        }
    }
}
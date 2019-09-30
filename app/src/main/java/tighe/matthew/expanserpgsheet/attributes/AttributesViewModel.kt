package tighe.matthew.expanserpgsheet.attributes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tighe.matthew.expanserpgsheet.*
import tighe.matthew.expanserpgsheet.model.character.AttributeType
import tighe.matthew.expanserpgsheet.model.character.Attributes
import java.lang.NumberFormatException

class AttributesViewModel : ViewModel(), BaseViewModel<AttributesViewState, AttributesAction> {
    override fun observeEvent(): SingleLiveEvent<Event> {
        return SingleLiveEvent()
    }

    private val viewState = MutableLiveData<AttributesViewState>()
    override fun observeViewState(): LiveData<AttributesViewState> {
        return viewState
    }

    override fun submitAction(action: AttributesAction) {
        return when (action) {
            is AttributesAction.AttributeInput -> {
                handleAttributeInput(action)
            }
        }
    }

    private fun handleAttributeInput(action: AttributesAction.AttributeInput) {
        val attributes = reduceUpdatedAttributes(action.type, action.input)
        val errors = reduceUpdatedErrors(action.type, action.input)
        val newViewState = AttributesViewState(attributes, errors)
        viewState.postValue(newViewState)
    }

    private fun reduceUpdatedAttributes(
        type: AttributeType,
        value: String
    ): Attributes {
        val currentAttributes = viewState.value?.attributes ?: Attributes.UNFILLED_ATTRIBUTES
        val newValue = try {
            value.toInt()
        } catch (err: NumberFormatException) {
            Attributes.UNFILLED_ATTRIBUTE
        }
        return when (type) {
            AttributeType.ACCURACY -> currentAttributes.copy(accuracy = newValue)
            AttributeType.COMMUNICATION -> currentAttributes.copy(communication = newValue)
            AttributeType.CONSTITUTION -> currentAttributes.copy(constitution = newValue)
            AttributeType.DEXTERITY -> currentAttributes.copy(dexterity = newValue)
            AttributeType.FIGHTING -> currentAttributes.copy(fighting = newValue)
            AttributeType.INTELLIGENCE -> currentAttributes.copy(intelligence = newValue)
            AttributeType.PERCEPTION -> currentAttributes.copy(perception = newValue)
            AttributeType.STRENGTH -> currentAttributes.copy(strength = newValue)
            AttributeType.WILLPOWER -> currentAttributes.copy(willpower = newValue)
        }
    }

    private fun reduceUpdatedErrors(type: AttributeType, input: String): List<AttributeError> {
        return try {
            input.toInt()
            val disabledError = AttributeError(type, errorEnabled = false)
            val updatedErrorList = viewState.value?.errors?.filter { it.type != type } ?: listOf()
            updatedErrorList.plus(disabledError)
        } catch (err: NumberFormatException) {
            val enabledError = AttributeError(type, errorEnabled = true)
            viewState.value?.errors?.plus(enabledError) ?: listOf(enabledError)
        }
    }
}
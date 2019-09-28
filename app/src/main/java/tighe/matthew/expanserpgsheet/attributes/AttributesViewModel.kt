package tighe.matthew.expanserpgsheet.attributes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tighe.matthew.expanserpgsheet.BaseViewModel
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.SingleLiveEvent
import tighe.matthew.expanserpgsheet.model.character.AttributeType
import tighe.matthew.expanserpgsheet.model.character.Attributes
import tighe.matthew.expanserpgsheet.toIntOrZero

class AttributesViewModel : ViewModel(), BaseViewModel<AttributesViewState, AttributesAction> {
    override fun observeEvent(): SingleLiveEvent<Event> { return SingleLiveEvent() }

    private val viewState = MutableLiveData<AttributesViewState>()
    override fun observeViewState(): LiveData<AttributesViewState> {
        return viewState
    }

    override fun submitAction(action: AttributesAction) {
        return when (action) {
            is AttributesAction.AttributeInput -> {
                val newAttributes = reduceUpdatedAttributes(action)
                viewState.postValue(AttributesViewState(newAttributes))
            }
        }
    }

    private fun reduceUpdatedAttributes(
        action: AttributesAction.AttributeInput
    ): Attributes {
        val currentAttributes = viewState.value?.attributes ?: Attributes()
        val type = action.type
        // TODO error checking/display
        val input = action.input.toIntOrZero()
        return when (type) {
            AttributeType.ACCURACY -> currentAttributes.copy(accuracy = input)
            AttributeType.COMMUNICATION -> currentAttributes.copy(communication = input)
            AttributeType.CONSTITUTION -> currentAttributes.copy(constitution = input)
            AttributeType.DEXTERITY -> currentAttributes.copy(dexterity = input)
            AttributeType.FIGHTING -> currentAttributes.copy(fighting = input)
            AttributeType.INTELLIGENCE -> currentAttributes.copy(intelligence = input)
            AttributeType.PERCEPTION -> currentAttributes.copy(perception = input)
            AttributeType.STRENGTH -> currentAttributes.copy(strength = input)
            AttributeType.WILLPOWER -> currentAttributes.copy(willpower = input)
        }
    }
}
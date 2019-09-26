package tighe.matthew.expanserpgsheet.attributes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tighe.matthew.expanserpgsheet.BaseViewModel
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.SingleLiveEvent
import tighe.matthew.expanserpgsheet.model.character.Attributes
import tighe.matthew.expanserpgsheet.toIntOrZero

class AttributesViewModel : ViewModel(), BaseViewModel<AttributesViewState, AttributesAction> {
    override fun observeEvent(): SingleLiveEvent<Event> { return SingleLiveEvent() }

    private val baseAttributes = Attributes()
    private val viewState = MutableLiveData<AttributesViewState>().apply {
        postValue(AttributesViewState(baseAttributes))
    }
    override fun observeViewState(): LiveData<AttributesViewState> {
        return viewState
    }

    override fun submitAction(action: AttributesAction) {
        val newAttributes = when (action) {
            is AttributesAction.AccuracyInput -> {
                viewState.value!!.attributes.copy(accuracy = action.accuracy.toIntOrZero())
            }
            is AttributesAction.CommunicationInput -> {
                viewState.value!!.attributes.copy(communication = action.communication.toIntOrZero())
            }
            is AttributesAction.ConstitutionInput -> {
                viewState.value!!.attributes.copy(constitution = action.constitution.toIntOrZero())
            }
            is AttributesAction.DexterityInput -> {
                viewState.value!!.attributes.copy(dexterity = action.dexterity.toIntOrZero())
            }
            is AttributesAction.FightingInput -> {
                viewState.value!!.attributes.copy(fighting = action.fighting.toIntOrZero())
            }
            is AttributesAction.IntelligenceInput -> {
                viewState.value!!.attributes.copy(intelligence = action.intelligence.toIntOrZero())
            }
            is AttributesAction.PerceptionInput -> {
                viewState.value!!.attributes.copy(perception = action.perception.toIntOrZero())
            }
            is AttributesAction.StrengthInput -> {
                viewState.value!!.attributes.copy(strength = action.strength.toIntOrZero())
            }
            is AttributesAction.WillpowerInput -> {
                viewState.value!!.attributes.copy(willpower = action.willpower.toIntOrZero())
            }
        }
        viewState.postValue(AttributesViewState(newAttributes))
    }
}
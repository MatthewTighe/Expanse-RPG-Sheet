package tighe.matthew.expanserpgsheet.characterCreation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import tighe.matthew.expanserpgsheet.*
import tighe.matthew.expanserpgsheet.attributes.AttributeError
import tighe.matthew.expanserpgsheet.attributes.AttributeReducer
import tighe.matthew.expanserpgsheet.model.character.Attributes
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository

internal class CharacterCreationViewModel(
    private val repository: CharacterRepository
) : ViewModel(),
    BaseViewModel<CharacterCreationViewState, CharacterCreationAction> {

    private val viewState = MutableLiveData<CharacterCreationViewState>().apply {
        postValue(CharacterCreationViewState())
    }
    override fun observeViewState(): LiveData<CharacterCreationViewState> { return viewState }

    private val event = SingleLiveEvent<Event>()
    override fun observeEvent(): SingleLiveEvent<Event> { return event }

    // Mutable state isn't my favorite way to go, but I keep finding that reducing updates from a
    // nullable livedata is unfun and unsexy. See reduceAttributeErrors for a real good example of that
    var model: Character = Character(0)

    override fun submitAction(action: CharacterCreationAction) {
        return when (action) {
            is CharacterCreationAction.NameChanged -> {
                val updatedViewState = reduceNameUpdate(action)
                viewState.postValue(updatedViewState)
            }
            is CharacterCreationAction.MaxFortuneChanged -> {
                val updatedFortune = action.fortune.toIntOrZero()
                model = model.copy(maxFortune = updatedFortune, currentFortune = updatedFortune)
            }
            is CharacterCreationAction.AttributeChanged -> {
                val updatedViewState = reduceAttributeInput(action)
                viewState.postValue(updatedViewState)
            }
            is CharacterCreationAction.Save -> {
                handleSaveAction()
            }
        }
    }

    private fun handleSaveAction() {
        if (modelIsComplete()) {
            viewModelScope.launch { repository.persist(model) }
            event.postValue(Event.Navigate(R.id.character_list_fragment))
            return
        }

        val nameError = NameError(errorEnabled = model.name.isBlank())

        val attributeErrors = getAttributeErrors()

        val updatedViewState = CharacterCreationViewState(nameError, attributeErrors)
        viewState.postValue(updatedViewState)
    }

    private fun modelIsComplete(): Boolean {
        return model.name.isNotBlank() && model.attributes.all { it.value != Attributes.UNFILLED_ATTRIBUTE }
    }

    private fun reduceNameUpdate(action: CharacterCreationAction.NameChanged): CharacterCreationViewState {
        model = model.copy(name = action.name)
        val currentViewState = viewState.value!!
        return if (action.name.isBlank()) {
            currentViewState.copy(nameError = NameError(errorEnabled = true))
        } else {
            currentViewState.copy(nameError = NameError(errorEnabled = false))
        }
    }

    private fun reduceAttributeInput(action: CharacterCreationAction.AttributeChanged): CharacterCreationViewState {
        val updatedAttributes = AttributeReducer.reduceAttributeInput(model.attributes, action.input)
        model = model.copy(attributes = updatedAttributes)

        val updatedErrors = AttributeReducer.reduceErrors(viewState.value?.attributeErrors, action.input)
        return viewState.value?.copy(attributeErrors = updatedErrors)
            ?: CharacterCreationViewState(attributeErrors = updatedErrors)
    }

    private fun getAttributeErrors(): List<AttributeError> {
        return model.attributes.map { data ->
            val enabled = data.value == Attributes.UNFILLED_ATTRIBUTE
            AttributeError(
                data.type,
                errorEnabled = enabled
            )
        }
    }
}
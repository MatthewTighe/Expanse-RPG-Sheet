package tighe.matthew.expanserpgsheet.characterCreation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import tighe.matthew.expanserpgsheet.*
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

    // TODO reduce this from view state instead of having mutable state
    var model: Character = Character(0)

    override fun submitAction(action: CharacterCreationAction) {
        return when (action) {
            is CharacterCreationAction.NameInput -> {
                val updatedViewState = reduceNameUpdate(action)
                viewState.postValue(updatedViewState)
            }
            is CharacterCreationAction.MaxFortuneInput -> {
                val updatedFortune = action.fortune.toIntOrZero()
                model = model.copy(maxFortune = updatedFortune, currentFortune = updatedFortune)
            }
            is CharacterCreationAction.AttributesUpdate -> {
                model = model.copy(attributes = action.attributes)
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

        val updatedViewState = viewState.value!!.copy(nameError = nameError)
        viewState.postValue(updatedViewState)
    }

    private fun modelIsComplete(): Boolean {
        return model.name.isNotBlank()
    }

    private fun reduceNameUpdate(action: CharacterCreationAction.NameInput): CharacterCreationViewState {
        model = model.copy(name = action.name)
        val currentViewState = viewState.value!!
        return if (action.name.isBlank()) {
            currentViewState.copy(nameError = NameError(errorEnabled = true))
        } else {
            currentViewState.copy(nameError = NameError(errorEnabled = false))
        }
    }
}
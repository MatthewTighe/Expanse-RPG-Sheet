package tighe.matthew.expanserpgsheet.characterCreation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tighe.matthew.expanserpgsheet.*
import tighe.matthew.expanserpgsheet.model.CharacterModel
import tighe.matthew.expanserpgsheet.repository.CharacterRepository

internal class CharacterCreationViewModel(
    private val repository: CharacterRepository
) : ViewModel(), BaseViewModel<CharacterCreationViewState, CharacterCreationAction> {

    private val viewState = MutableLiveData<CharacterCreationViewState>().apply {
        postValue(CharacterCreationViewState())
    }
    override fun observeViewState(): LiveData<CharacterCreationViewState> { return viewState }

    private val event = SingleLiveEvent<Event>()
    override fun observeEvent(): SingleLiveEvent<Event> { return event }

    var model: CharacterModel = CharacterModel()

    override fun submitAction(action: CharacterCreationAction) {
        return when (action) {
            is CharacterCreationAction.NameInput -> {
                model = model.copy(name = action.name)
                val currentViewState = viewState.value!!
                val updatedViewState = if (action.name.isBlank()) {
                    currentViewState.copy(nameError = NameError(errorEnabled = true))
                } else {
                    currentViewState.copy(nameError = NameError(errorEnabled = false))
                }
                viewState.postValue(updatedViewState)
            }
            is CharacterCreationAction.MaxFortuneInput -> {
                model = model.copy(maxFortune = action.fortune)
            }
            is CharacterCreationAction.Save -> {
                handleSaveAction()
            }
        }
    }

    private fun handleSaveAction() {
        if (modelIsComplete()) {
            repository.persist(model)
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
}
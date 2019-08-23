package tighe.matthew.expanserpgsheet.characterCreation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tighe.matthew.expanserpgsheet.BaseViewModel
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.SingleLiveEvent
import tighe.matthew.expanserpgsheet.model.CharacterModel
import tighe.matthew.expanserpgsheet.repository.CharacterRepository

internal class CharacterCreationViewModel(
    private val repository: CharacterRepository
) : ViewModel(), BaseViewModel<CharacterCreationViewState, CharacterCreationAction> {

    private val viewState = MutableLiveData<CharacterCreationViewState>()
    override fun observeViewState(): LiveData<CharacterCreationViewState> { return viewState }

    private val event = SingleLiveEvent<Event>()
    override fun observeEvent(): SingleLiveEvent<Event> { return event }

    var model: CharacterModel = CharacterModel()

    override fun submitAction(action: CharacterCreationAction) {
        return when (action) {
            is CharacterCreationAction.NameInput -> {
                model = model.copy(name = action.name)
            }
            is CharacterCreationAction.MaxFortuneInput -> {
                model = model.copy(maxFortune = action.fortune)
            }
            is CharacterCreationAction.Save -> {
                repository.persist(model)
                event.postValue(Event.Navigate(R.id.character_list_fragment))
            }
        }
    }
}
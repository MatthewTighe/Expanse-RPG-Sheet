package tighe.matthew.expanserpgsheet.characterCreation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tighe.matthew.expanserpgsheet.BaseViewModel
import tighe.matthew.expanserpgsheet.Event

internal class CharacterCreationViewModel :
    ViewModel(),
    BaseViewModel<CharacterCreationViewState, CharacterCreationAction> {

    private val viewState = MutableLiveData<CharacterCreationViewState>()
    override fun observeViewState(): LiveData<CharacterCreationViewState> { return viewState }

    private val event = MutableLiveData<Event>()
    override fun observeEvent(): LiveData<Event> { return event }

    override fun submitAction(action: CharacterCreationAction) {
        return when (action) {
            is CharacterCreationAction.NameInput -> {}
            is CharacterCreationAction.MaxFortuneInput -> {}
            is CharacterCreationAction.Save -> {}
        }
    }
}
package tighe.matthew.expanserpgsheet.characterDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tighe.matthew.expanserpgsheet.BaseViewModel
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.SingleLiveEvent

internal class CharacterDetailsViewModel : ViewModel(), BaseViewModel<CharacterDetailsViewState, CharacterDetailsAction> {
    private val viewState = MutableLiveData<CharacterDetailsViewState>()
    override fun observeViewState(): LiveData<CharacterDetailsViewState> { return viewState }

    private val event = SingleLiveEvent<Event>()
    override fun observeEvent(): SingleLiveEvent<Event> { return event }

    private var currentFortune = 0

    override fun submitAction(action: CharacterDetailsAction) {
        return when (action) {
            is CharacterDetailsAction.CharacterReceived -> {
                currentFortune = action.character.maxFortune
                viewState.postValue(CharacterDetailsViewState(currentFortune))
            }
            is CharacterDetailsAction.IncrementFortune -> {
                currentFortune += action.value
                viewState.postValue(CharacterDetailsViewState(currentFortune))
            }
            is CharacterDetailsAction.DecrementFortune -> {
                currentFortune -= action.value
                viewState.postValue(CharacterDetailsViewState(currentFortune))
            }
        }
    }
}
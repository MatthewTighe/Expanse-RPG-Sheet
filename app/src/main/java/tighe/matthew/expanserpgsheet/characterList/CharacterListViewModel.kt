package tighe.matthew.expanserpgsheet.characterList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tighe.matthew.expanserpgsheet.BaseViewModel
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.R

internal class CharacterListViewModel : ViewModel(), BaseViewModel<CharacterListViewState, CharacterListAction> {
    private val event = MutableLiveData<Event>()
    override fun observeEvent(): LiveData<Event> {
        return event
    }

    private val viewState = MutableLiveData<CharacterListViewState>()
    override fun observeViewState(): LiveData<CharacterListViewState> {
        return viewState
    }

    override fun submitAction(action: CharacterListAction) {
        return when (action) {
            is CharacterListAction.Add -> {
                event.postValue(Event.Navigate(R.id.charactionCreationFragment))
            }
            is CharacterListAction.Refresh -> {}
        }
    }
}
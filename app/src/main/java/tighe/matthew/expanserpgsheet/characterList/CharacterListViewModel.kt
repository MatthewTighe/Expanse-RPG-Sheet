package tighe.matthew.expanserpgsheet.characterList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tighe.matthew.expanserpgsheet.*
import tighe.matthew.expanserpgsheet.repository.CharacterRepository

internal class CharacterListViewModel(
    private val repository: CharacterRepository
) : ViewModel(), BaseViewModel<CharacterListViewState, CharacterListAction> {
    private val event = SingleLiveEvent<Event>()
    override fun observeEvent(): SingleLiveEvent<Event> { return event }

    private val viewState = MutableLiveData<CharacterListViewState>()
    override fun observeViewState(): LiveData<CharacterListViewState> { return viewState }

    override fun submitAction(action: CharacterListAction) {
        return when (action) {
            is CharacterListAction.Add -> {
                event.postValue(Event.Navigate(R.id.characterCreationFragment))
            }
            is CharacterListAction.Refresh -> {
                viewState.postValue(CharacterListViewState(loading = true))
                val characters = repository.loadAll()
                viewState.postValue(CharacterListViewState(characterList = characters))
            }
        }
    }
}
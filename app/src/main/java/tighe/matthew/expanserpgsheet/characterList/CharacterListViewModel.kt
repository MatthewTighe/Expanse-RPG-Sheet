package tighe.matthew.expanserpgsheet.characterList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import tighe.matthew.expanserpgsheet.BaseViewModel
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.SingleLiveEvent
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository
import kotlin.coroutines.CoroutineContext

internal class CharacterListViewModel(
    private val repository: CharacterRepository
) : ViewModel(),
    BaseViewModel<CharacterListViewState, CharacterListAction>,
    CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val event = SingleLiveEvent<Event>()
    override fun observeEvent(): SingleLiveEvent<Event> { return event }

    private val viewState = MutableLiveData<CharacterListViewState>()
    override fun observeViewState(): LiveData<CharacterListViewState> { return viewState }

    override fun submitAction(action: CharacterListAction) {
        when (action) {
            is CharacterListAction.Refresh -> {
                viewState.postValue(CharacterListViewState(loading = true))
                repository.observeAll().observeForever { it?.let { characters ->
                    viewState.postValue(CharacterListViewState(characterList = characters))
                } }
            }
            is CharacterListAction.Add -> {
                event.postValue(Event.Navigate(R.id.character_creation_fragment))
            }
            is CharacterListAction.Delete -> {
                this.launch {
                    repository.delete(action.character)
                }
            }
            is CharacterListAction.CharacterClicked -> {
                val navArgs = listOf(action.character.buildNavArg())
                event.postValue(Event.Navigate(R.id.character_details_fragment, navArgs))
            }
        }
    }
}
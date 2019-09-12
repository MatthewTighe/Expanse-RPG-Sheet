package tighe.matthew.expanserpgsheet.characterDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tighe.matthew.expanserpgsheet.BaseViewModel
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.SingleLiveEvent
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository
import kotlin.coroutines.CoroutineContext

internal class CharacterDetailsViewModel(
    private val characterId: Long,
    private val repository: CharacterRepository
) : ViewModel(), BaseViewModel<CharacterDetailsViewState, CharacterDetailsAction>, CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        job.cancel()
    }

    private val character: Character = runBlocking { repository.load(characterId) }

    private val viewState = MutableLiveData<CharacterDetailsViewState>().apply {
        postValue(CharacterDetailsViewState(character))
    }
    @ExperimentalCoroutinesApi
    override fun observeViewState(): LiveData<CharacterDetailsViewState> {
        this.launch {
            repository.observeAll().onEach { characters ->
                val updatedCharacter = characters.find { it.id == characterId }!!
                val updatedViewState = viewState.value!!.copy(
                    character = updatedCharacter
                )
                viewState.postValue(updatedViewState)
            }.launchIn(this)
        }
        return viewState
    }

    private val event = SingleLiveEvent<Event>()
    override fun observeEvent(): SingleLiveEvent<Event> { return event }

    override fun submitAction(action: CharacterDetailsAction) {
        when (action) {
            is CharacterDetailsAction.ChangeMaxFortune -> {
                val currentCharacter = viewState.value!!.character
                val updatedCharacter = currentCharacter.copy(maxFortune = action.newFortune)
                this.launch {
                    repository.update(updatedCharacter)
                }
            }
            is CharacterDetailsAction.ChangeCurrentFortune -> {
                val currentCharacter = viewState.value!!.character
                val updatedCharacter = currentCharacter.copy(currentFortune = action.newFortune)
                this.launch {
                    repository.update(updatedCharacter)
                }
            }
        }
    }
}
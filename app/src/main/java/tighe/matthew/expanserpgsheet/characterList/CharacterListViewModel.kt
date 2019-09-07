package tighe.matthew.expanserpgsheet.characterList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tighe.matthew.expanserpgsheet.BaseViewModel
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.SingleLiveEvent
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository
import tighe.matthew.expanserpgsheet.model.encounter.EncounterRepository
import kotlin.coroutines.CoroutineContext

internal class CharacterListViewModel(
    private val characterRepository: CharacterRepository,
    private val encounterRepository: EncounterRepository
) : ViewModel(),
    BaseViewModel<CharacterListViewState, CharacterListAction>,
    CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val event = SingleLiveEvent<Event>()
    override fun observeEvent(): SingleLiveEvent<Event> { return event }

    private val viewState = MutableLiveData<CharacterListViewState>().apply {
        postValue(CharacterListViewState())
    }
    @ExperimentalCoroutinesApi
    override fun observeViewState(): LiveData<CharacterListViewState> {
        characterRepository.observeAll().onEach { characters ->
            val update = viewState.value?.copy(characterList = characters) ?:
            CharacterListViewState(characterList = characters)
            viewState.postValue(update)
        }.launchIn(this)
        return viewState
    }

    override fun submitAction(action: CharacterListAction) {
        when (action) {
            is CharacterListAction.Add -> {
                event.postValue(Event.Navigate(R.id.character_creation_fragment))
            }
            is CharacterListAction.AddToEncounterClicked -> {
                this.launch {
                    val character = action.character
                    if (encounterRepository.characterIsInEncounter(character)) {
                        // TODO event.postValue(Snackbar)
                    } else {
                        val update = viewState.value!!.copy(
                            initiativeDialogShouldBeDisplayed = true,
                            characterBeingManipulated = action.character
                        )
                        viewState.postValue(update)
                    }
                }
            }
            is CharacterListAction.InitiativeEntered -> {
                this.launch {
                    encounterRepository.addCharacter(action.character!!, action.initiative)
                    // TODO event.postValue(Snackbar)
                }
            }
            is CharacterListAction.Delete -> {
                this.launch {
                    characterRepository.delete(action.character)
                }
            }
            is CharacterListAction.CharacterClicked -> {
                val navArgs = listOf(action.character.buildNavArg())
                event.postValue(Event.Navigate(R.id.character_details_fragment, navArgs))
            }
        }
    }
}
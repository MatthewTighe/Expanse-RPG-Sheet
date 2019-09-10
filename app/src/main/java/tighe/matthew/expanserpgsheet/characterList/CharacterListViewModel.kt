package tighe.matthew.expanserpgsheet.characterList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tighe.matthew.expanserpgsheet.*
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
            val update = viewState.value?.copy(characterList = characters)
            ?: CharacterListViewState(characterList = characters)
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
                        event.postValue(Event.Snackbar(R.string.character_already_in_encounter))
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
                    event.postValue(Event.Snackbar(R.string.character_added_encounter))
                    val update = viewState.value!!.copy(
                        initiativeDialogShouldBeDisplayed = false,
                        characterBeingManipulated = null
                    )
                    viewState.postValue(update)
                }
            }
            is CharacterListAction.Delete -> {
                this.launch {
                    characterRepository.delete(action.character)
                }
            }
            is CharacterListAction.CharacterClicked -> {
                val navArg = NavigationArgument("characterId", action.character.id)
                event.postValue(Event.Navigate(R.id.character_details_fragment, listOf(navArg)))
            }
        }
    }
}
package tighe.matthew.expanserpgsheet.encounter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tighe.matthew.expanserpgsheet.BaseViewModel
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.SingleLiveEvent
import tighe.matthew.expanserpgsheet.model.encounter.EncounterCharacter
import tighe.matthew.expanserpgsheet.model.encounter.EncounterRepository
import kotlin.coroutines.CoroutineContext

class EncounterViewModel(
    private val encounterRepository: EncounterRepository
) : ViewModel(), BaseViewModel<EncounterViewState, EncounterAction>, CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val event = SingleLiveEvent<Event>()
    override fun observeEvent(): SingleLiveEvent<Event> { return event }

    private val viewState = MutableLiveData<EncounterViewState>()
    @ExperimentalCoroutinesApi
    override fun observeViewState(): LiveData<EncounterViewState> {
        encounterRepository.getEncounter().onEach { encounter ->
            viewState.postValue(EncounterViewState(encounter))
        }.launchIn(this)
        return viewState
    }

    override fun submitAction(action: EncounterAction) {
        when (action) {
            is EncounterAction.IncrementFortune -> {
                handleFortuneChange(action.encounterCharacter, 1)
            }
            is EncounterAction.DecrementFortune -> {
                handleFortuneChange(action.encounterCharacter, -1)
            }
        }
    }

    private fun handleFortuneChange(encounterCharacter: EncounterCharacter, changeAmount: Int) {
        val character = encounterCharacter.character

        val updatedCharacter = character.copy(currentFortune = character.currentFortune + changeAmount)
        val updatedEncounterCharacter = encounterCharacter.copy(character = updatedCharacter)

        this.launch {
            encounterRepository.updateEncounterCharacter(updatedEncounterCharacter)
        }
    }
}
package tighe.matthew.expanserpgsheet.encounter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.LiveDataViewModel
import tighe.matthew.expanserpgsheet.SingleLiveEvent
import tighe.matthew.expanserpgsheet.model.encounter.EncounterCharacter
import tighe.matthew.expanserpgsheet.model.encounter.EncounterRepository

class EncounterViewModel(
    private val encounterRepository: EncounterRepository
) : ViewModel(), LiveDataViewModel<EncounterViewState, EncounterAction> {

    private val event = SingleLiveEvent<Event>()
    override fun observeEvent(): SingleLiveEvent<Event> { return event }

    private val viewState = MutableLiveData<EncounterViewState>()
    @ExperimentalCoroutinesApi
    override fun observeViewState(): LiveData<EncounterViewState> {
        encounterRepository.getEncounter().onEach { encounter ->
            viewState.postValue(EncounterViewState(encounter))
        }.launchIn(viewModelScope)
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
            is EncounterAction.SetFortune -> {
                handleSetFortune(action)
            }
            is EncounterAction.CharacterMoved -> {
                handleMovedAction(action)
            }
            is EncounterAction.CharacterRemoved -> {
                viewModelScope.launch {
                    encounterRepository.removeEncounterCharacter(
                        action.removedCharacter, action.position
                    )
                }
            }
        }
    }

    private fun handleFortuneChange(encounterCharacter: EncounterCharacter, changeAmount: Int) {
        val newFortune = encounterCharacter.character.currentFortune + changeAmount
        persistFortuneChange(encounterCharacter, newFortune)
    }

    private fun handleSetFortune(action: EncounterAction.SetFortune) {
        val newFortune = try {
            action.fortuneEntered.toInt()
        } catch (err: RuntimeException) {
            0
        }
        persistFortuneChange(action.encounterCharacter, newFortune)
    }

    private fun persistFortuneChange(encounterCharacter: EncounterCharacter, newFortune: Int) {
        val character = encounterCharacter.character

        val updatedCharacter = character.copy(currentFortune = newFortune)
        val updatedEncounterCharacter = encounterCharacter.copy(character = updatedCharacter)

        viewModelScope.launch {
            encounterRepository.updateEncounterCharacter(updatedEncounterCharacter)
        }
    }

    private fun handleMovedAction(action: EncounterAction.CharacterMoved) {
        val movedCharacter = action.movedCharacter
        val displacedCharacter = action.displacedCharacter
        val updatedMovedDetails =
            movedCharacter.detail.copy(position = action.toPosition)
        val updatedDisplacedDetails =
            displacedCharacter.detail.copy(position = action.fromPosition)

        val updatedMoved = movedCharacter.copy(detail = updatedMovedDetails)
        val updatedDisplaced = movedCharacter.copy(detail = updatedDisplacedDetails)

        viewModelScope.launch {
            encounterRepository.updateEncounterCharacter(updatedMoved)
            encounterRepository.updateEncounterCharacter(updatedDisplaced)
        }
    }
}
package tighe.matthew.expanserpgsheet.characterDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tighe.matthew.expanserpgsheet.BaseViewModel
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.SingleLiveEvent
import tighe.matthew.expanserpgsheet.attributes.AttributeReducer
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository

internal class CharacterDetailsViewModel(
    private val characterId: Long,
    private val repository: CharacterRepository
) : ViewModel(), BaseViewModel<CharacterDetailsViewState, CharacterDetailsAction> {

    private val character: Character = runBlocking { repository.load(characterId) }

    private val viewState = MutableLiveData<CharacterDetailsViewState>().apply {
        postValue(CharacterDetailsViewState(character))
    }
    @ExperimentalCoroutinesApi
    override fun observeViewState(): LiveData<CharacterDetailsViewState> {
        viewModelScope.launch {
            repository.observeWithConditions().onEach { characters ->
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
            is CharacterDetailsAction.MaxFortuneChanged -> {
                val currentCharacter = viewState.value!!.character
                val updatedCharacter = currentCharacter.copy(maxFortune = action.newFortune)
                viewModelScope.launch {
                    repository.update(updatedCharacter)
                }
            }
            is CharacterDetailsAction.CurrentFortuneChanged -> {
                val currentCharacter = viewState.value!!.character
                val updatedCharacter = currentCharacter.copy(currentFortune = action.newFortune)
                viewModelScope.launch {
                    repository.update(updatedCharacter)
                }
            }
            is CharacterDetailsAction.AttributeChanged -> {
                handleAttributeInput(action)
            }
            is CharacterDetailsAction.ConditionChecked -> {
                viewModelScope.launch {
                    repository.addCondition(action.condition, action.character)
                }
            }
            is CharacterDetailsAction.ConditionUnchecked -> {
                viewModelScope.launch {
                    repository.removeCondition(action.condition, action.character)
                }
            }
        }
    }

    private fun handleAttributeInput(action: CharacterDetailsAction.AttributeChanged) {
        val updatedAttributes = AttributeReducer.reduceAttributeInput(
            viewState.value?.character?.attributes, action.attributeInput
        )
        val updatedCharacter = viewState.value!!.character.copy(attributes = updatedAttributes)

        val updatedErrors = AttributeReducer.reduceErrors(viewState.value?.attributeErrors, action.attributeInput)

        val updatedViewState = viewState.value?.copy(
            character = updatedCharacter,
            attributeErrors = updatedErrors
        ) ?: CharacterDetailsViewState(character = character, attributeErrors = updatedErrors)

        viewModelScope.launch { repository.update(updatedCharacter) }

        viewState.postValue(updatedViewState)
    }
}
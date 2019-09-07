package tighe.matthew.expanserpgsheet.characterList

import tighe.matthew.expanserpgsheet.Action
import tighe.matthew.expanserpgsheet.model.character.Character

internal sealed class CharacterListAction : Action {
    object Refresh : CharacterListAction()
    object Add : CharacterListAction()
    data class AddToEncounterClicked(val character: Character) : CharacterListAction()
    data class InitiativeEntered(val initiative: Int, val character: Character?) : CharacterListAction()
    data class Delete(val character: Character) : CharacterListAction()
    data class CharacterClicked(val character: Character) : CharacterListAction()
}
package tighe.matthew.expanserpgsheet.characterList

import tighe.matthew.expanserpgsheet.Action
import tighe.matthew.expanserpgsheet.model.CharacterModel

internal sealed class CharacterListAction : Action {
    object Refresh : CharacterListAction()
    object Add : CharacterListAction()
    data class Delete(val character: CharacterModel) : CharacterListAction()
    data class CharacterClicked(val character: CharacterModel) : CharacterListAction()
}
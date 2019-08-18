package tighe.matthew.expanserpgsheet.characterList

import tighe.matthew.expanserpgsheet.Action
import tighe.matthew.expanserpgsheet.model.CharacterModel

internal sealed class CharacterListAction : Action {
    object Add : CharacterListAction()
    data class CharacterClicked(val character: CharacterModel) : CharacterListAction()
    object Refresh : CharacterListAction()
}
package tighe.matthew.expanserpgsheet.characterList

import tighe.matthew.expanserpgsheet.Action

internal sealed class CharacterListAction : Action {
    object Add : CharacterListAction()
    object Refresh : CharacterListAction()
}
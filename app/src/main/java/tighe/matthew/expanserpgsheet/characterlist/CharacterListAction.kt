package tighe.matthew.expanserpgsheet.characterlist

import tighe.matthew.expanserpgsheet.Action

sealed class CharacterListAction : Action {
    object Add : CharacterListAction()
}
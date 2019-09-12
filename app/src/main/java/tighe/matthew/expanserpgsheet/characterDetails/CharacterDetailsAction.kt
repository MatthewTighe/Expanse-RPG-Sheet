package tighe.matthew.expanserpgsheet.characterDetails

import tighe.matthew.expanserpgsheet.Action

internal sealed class CharacterDetailsAction : Action {
    data class ChangeMaxFortune(val newFortune: Int) : CharacterDetailsAction()
    data class ChangeCurrentFortune(val newFortune: Int) : CharacterDetailsAction()
}
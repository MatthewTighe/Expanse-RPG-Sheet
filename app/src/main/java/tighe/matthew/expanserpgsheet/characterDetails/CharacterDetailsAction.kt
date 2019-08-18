package tighe.matthew.expanserpgsheet.characterDetails

import tighe.matthew.expanserpgsheet.Action

internal sealed class CharacterDetailsAction : Action {
    data class IncrementFortune(val value: Int) : CharacterDetailsAction()
    data class DecrementFortune(val value: Int) : CharacterDetailsAction()
}
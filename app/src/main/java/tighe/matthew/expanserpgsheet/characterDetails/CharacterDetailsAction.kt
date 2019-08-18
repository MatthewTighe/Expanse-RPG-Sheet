package tighe.matthew.expanserpgsheet.characterDetails

import tighe.matthew.expanserpgsheet.Action
import tighe.matthew.expanserpgsheet.model.CharacterModel

internal sealed class CharacterDetailsAction : Action {
    data class CharacterReceived(val character: CharacterModel) : CharacterDetailsAction()
    data class IncrementFortune(val value: Int) : CharacterDetailsAction()
    data class DecrementFortune(val value: Int) : CharacterDetailsAction()
}
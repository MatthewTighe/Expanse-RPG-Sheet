package tighe.matthew.expanserpgsheet.characterDetails

import tighe.matthew.expanserpgsheet.Action
import tighe.matthew.expanserpgsheet.model.character.Character

internal sealed class CharacterDetailsAction : Action {
    object IncrementFortune : CharacterDetailsAction()
    object DecrementFortune : CharacterDetailsAction()
}
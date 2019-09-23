package tighe.matthew.expanserpgsheet.characterDetails

import tighe.matthew.expanserpgsheet.Action
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.condition.Condition

internal sealed class CharacterDetailsAction : Action {
    data class ChangeMaxFortune(val newFortune: Int) : CharacterDetailsAction()
    data class ChangeCurrentFortune(val newFortune: Int) : CharacterDetailsAction()
    data class ConditionChecked(val condition: Condition, val character: Character) : CharacterDetailsAction()
    data class ConditionUnchecked(val condition: Condition, val character: Character) : CharacterDetailsAction()
}
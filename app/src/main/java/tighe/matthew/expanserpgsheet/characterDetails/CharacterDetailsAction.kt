package tighe.matthew.expanserpgsheet.characterDetails

import tighe.matthew.expanserpgsheet.Action
import tighe.matthew.expanserpgsheet.controller.AttributeInput
import tighe.matthew.expanserpgsheet.model.character.Attributes
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.condition.Condition

internal sealed class CharacterDetailsAction : Action {
    data class MaxFortuneChanged(val newFortune: Int) : CharacterDetailsAction()
    data class CurrentFortuneChanged(val newFortune: Int) : CharacterDetailsAction()
    data class AttributeChanged(val attributeInput: AttributeInput) : CharacterDetailsAction()
    data class ConditionChecked(val condition: Condition, val character: Character) : CharacterDetailsAction()
    data class ConditionUnchecked(val condition: Condition, val character: Character) : CharacterDetailsAction()
}
package tighe.matthew.expanserpgsheet.encounter

import tighe.matthew.expanserpgsheet.Action
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.condition.Condition
import tighe.matthew.expanserpgsheet.model.encounter.EncounterCharacter

sealed class EncounterAction : Action {
    data class IncrementFortune(val encounterCharacter: EncounterCharacter) : EncounterAction()
    data class DecrementFortune(val encounterCharacter: EncounterCharacter) : EncounterAction()

    data class SetFortune(
        val fortuneEntered: String,
        val encounterCharacter: EncounterCharacter
    ) : EncounterAction()

    data class ConditionChecked(
        val condition: Condition,
        val character: Character
    ) : EncounterAction()

    data class ConditionUnchecked(
        val condition: Condition,
        val character: Character
    ) : EncounterAction()

    data class CharacterMoved(
        val movedCharacter: EncounterCharacter,
        val fromPosition: Int,
        val displacedCharacter: EncounterCharacter,
        val toPosition: Int
    ) : EncounterAction()

    data class CharacterRemoved(
        val removedCharacter: EncounterCharacter,
        val position: Int
    ) : EncounterAction()

    object ClearAll : EncounterAction()
}
package tighe.matthew.expanserpgsheet.encounter

import tighe.matthew.expanserpgsheet.Action
import tighe.matthew.expanserpgsheet.model.encounter.EncounterCharacter

sealed class EncounterAction : Action {
    data class IncrementFortune(val encounterCharacter: EncounterCharacter) : EncounterAction()
    data class DecrementFortune(val encounterCharacter: EncounterCharacter) : EncounterAction()
}
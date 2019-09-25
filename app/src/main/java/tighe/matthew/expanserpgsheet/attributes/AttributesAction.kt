package tighe.matthew.expanserpgsheet.attributes

import tighe.matthew.expanserpgsheet.Action

sealed class AttributesAction : Action {
    data class AccuracyInput(val accuracy: String) : AttributesAction()
    data class CommunicationInput(val communication: String) : AttributesAction()
    data class ConstitutionInput(val constitution: String) : AttributesAction()
    data class DexterityInput(val dexterity: String) : AttributesAction()
    data class FightingInput(val fighting: String) : AttributesAction()
    data class IntelligenceInput(val intelligence: String) : AttributesAction()
    data class PerceptionInput(val perception: String) : AttributesAction()
    data class StrengthInput(val strength: String) : AttributesAction()
    data class WillpowerInput(val willpower: String) : AttributesAction()
}
package tighe.matthew.expanserpgsheet.characterCreation

import tighe.matthew.expanserpgsheet.Action
import tighe.matthew.expanserpgsheet.attributes.AttributeInput

internal sealed class CharacterCreationAction : Action {
    data class NameChanged(val name: String) : CharacterCreationAction()
    data class MaxFortuneChanged(val fortune: String) : CharacterCreationAction()
    data class AttributeChanged(val input: AttributeInput) : CharacterCreationAction()
    object Save : CharacterCreationAction()
}
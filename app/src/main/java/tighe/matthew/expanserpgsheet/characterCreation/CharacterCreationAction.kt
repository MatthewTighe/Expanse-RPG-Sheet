package tighe.matthew.expanserpgsheet.characterCreation

import tighe.matthew.expanserpgsheet.Action
import tighe.matthew.expanserpgsheet.controller.AttributeInput
import tighe.matthew.expanserpgsheet.model.character.Attributes

internal sealed class CharacterCreationAction : Action {
    data class NameChanged(val name: String) : CharacterCreationAction()
    data class MaxFortuneChanged(val fortune: String) : CharacterCreationAction()
    data class AttributeChanged(val input: AttributeInput) : CharacterCreationAction()
    object Save : CharacterCreationAction()
}
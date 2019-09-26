package tighe.matthew.expanserpgsheet.characterCreation

import tighe.matthew.expanserpgsheet.Action
import tighe.matthew.expanserpgsheet.model.character.Attributes

internal sealed class CharacterCreationAction : Action {
    data class NameInput(val name: String) : CharacterCreationAction()
    data class MaxFortuneInput(val fortune: String) : CharacterCreationAction()
    data class AttributesUpdate(val attributes: Attributes) : CharacterCreationAction()
    object Save : CharacterCreationAction()
}
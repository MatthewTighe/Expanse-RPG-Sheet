package tighe.matthew.expanserpgsheet.characterCreation

import tighe.matthew.expanserpgsheet.Action
import tighe.matthew.expanserpgsheet.attributes.AttributeInput
import tighe.matthew.expanserpgsheet.model.character.Armor

internal sealed class CharacterCreationAction : Action {
    data class NameChanged(val name: String) : CharacterCreationAction()
    data class MaxFortuneChanged(val fortune: String) : CharacterCreationAction()
    data class AttributeChanged(val input: AttributeInput) : CharacterCreationAction()
    data class ArmorChanged(val armor: Armor) : CharacterCreationAction()
    object Save : CharacterCreationAction()
}
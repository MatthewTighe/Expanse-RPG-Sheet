package tighe.matthew.expanserpgsheet.attributes

import tighe.matthew.expanserpgsheet.Action
import tighe.matthew.expanserpgsheet.model.character.AttributeType

sealed class AttributesAction : Action {
    data class AttributeInput(val type: AttributeType, val input: String): AttributesAction()
}
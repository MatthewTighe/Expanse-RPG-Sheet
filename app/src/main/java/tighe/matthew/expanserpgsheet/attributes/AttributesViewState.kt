package tighe.matthew.expanserpgsheet.attributes

import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.TextInputFieldError
import tighe.matthew.expanserpgsheet.ViewState
import tighe.matthew.expanserpgsheet.model.character.AttributeType
import tighe.matthew.expanserpgsheet.model.character.Attributes

data class AttributesViewState(
    val attributes: Attributes,
    val errors: List<AttributeError> = listOf()
) : ViewState

data class AttributeError(
    val type: AttributeType,
    override val errorEnabled: Boolean = false,
    override val fieldName: Int = when (type) {
        AttributeType.ACCURACY -> R.string.accuracy
        AttributeType.COMMUNICATION -> R.string.communication
        AttributeType.CONSTITUTION -> R.string.constitution
        AttributeType.DEXTERITY -> R.string.dexterity
        AttributeType.FIGHTING -> R.string.fighting
        AttributeType.INTELLIGENCE -> R.string.intelligence
        AttributeType.PERCEPTION -> R.string.perception
        AttributeType.STRENGTH -> R.string.strength
        AttributeType.WILLPOWER -> R.string.willpower
    }
) : TextInputFieldError
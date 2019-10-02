package tighe.matthew.expanserpgsheet.attributes

import tighe.matthew.expanserpgsheet.model.character.AttributeType
import tighe.matthew.expanserpgsheet.model.character.Attributes

object AttributeReducer {
    fun reduceAttributeInput(attributes: Attributes?, attributeInput: AttributeInput): Attributes {
        val attributesUsed = attributes ?: Attributes.UNFILLED_ATTRIBUTES
        val updatedValue = try {
            attributeInput.value.toInt()
        } catch (err: NumberFormatException) {
            Attributes.UNFILLED_ATTRIBUTE
        }
        return when(attributeInput.type) {
            AttributeType.ACCURACY -> attributesUsed.copy(accuracy = updatedValue)
            AttributeType.COMMUNICATION -> attributesUsed.copy(communication = updatedValue)
            AttributeType.CONSTITUTION -> attributesUsed.copy(constitution = updatedValue)
            AttributeType.DEXTERITY -> attributesUsed.copy(dexterity = updatedValue)
            AttributeType.FIGHTING -> attributesUsed.copy(fighting = updatedValue)
            AttributeType.INTELLIGENCE -> attributesUsed.copy(intelligence = updatedValue)
            AttributeType.PERCEPTION -> attributesUsed.copy(perception = updatedValue)
            AttributeType.STRENGTH ->  attributesUsed.copy(strength = updatedValue)
            AttributeType.WILLPOWER -> attributesUsed.copy(willpower = updatedValue)
        }
    }

    fun reduceErrors(errors: List<AttributeError>?, attributeInput: AttributeInput): List<AttributeError> {
        val errorsUsed = errors ?: listOf()
        val enabled = try {
            attributeInput.value.toInt()
            false
        } catch (err: NumberFormatException) {
            true
        }
        val updatedError = AttributeError(
            attributeInput.type,
            errorEnabled = enabled
        )
        return errorsUsed.filter {
            it.type != attributeInput.type
        }.plus(updatedError)
    }
}
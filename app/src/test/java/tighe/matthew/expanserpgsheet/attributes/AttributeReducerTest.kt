package tighe.matthew.expanserpgsheet.attributes

import org.junit.Assert.*
import org.junit.Test
import tighe.matthew.expanserpgsheet.model.character.AttributeType
import tighe.matthew.expanserpgsheet.model.character.Attributes

class AttributeReducerTest {
    private val filledAttributes = Attributes(1, 1, 1, 1, 1, 1, 1, 1, 1)

    @Test
    fun `Reducing attributes with new input overwrites old value`() {
        val input = AttributeInput(AttributeType.ACCURACY, "2")

        val result = AttributeReducer.reduceAttributeInput(filledAttributes, input)

        val expected = filledAttributes.copy(accuracy = 2)
        assertEquals(expected, result)
    }

    @Test
    fun `Attribute reduction defaults to unfilled attributes`() {
        val input = AttributeInput(AttributeType.ACCURACY, "10")

        val result = AttributeReducer.reduceAttributeInput(null, input)

        val expected = Attributes.UNFILLED_ATTRIBUTES.copy(accuracy = 10)
        assertEquals(expected, result)
    }

    @Test
    fun `If an input cannot be transformed to an int, the attribute defaults to unfilled when reduced`() {
        val input = AttributeInput(AttributeType.ACCURACY, "")

        val result = AttributeReducer.reduceAttributeInput(filledAttributes, input)

        val expected = filledAttributes.copy(accuracy = Attributes.UNFILLED_ATTRIBUTE)
        assertEquals(expected, result)
    }

    @Test
    fun `Reducing errors defaults to an empty list`() {
        val input = AttributeInput(AttributeType.ACCURACY, "1")

        val result = AttributeReducer.reduceErrors(null, input)

        val expected = listOf(AttributeError(AttributeType.ACCURACY, errorEnabled = false))
        assertEquals(expected, result)
    }

    @Test
    fun `Error is disabled if input is formatted correctly`() {
        val errors = listOf(AttributeError(AttributeType.ACCURACY, errorEnabled = true))
        val input = AttributeInput(AttributeType.ACCURACY, "1")

        val result = AttributeReducer.reduceErrors(errors, input)

        val expected = listOf(AttributeError(AttributeType.ACCURACY, errorEnabled = false))
        assertEquals(expected, result)
    }

    @Test
    fun `Error is enabled if input is formatted incorrectly`() {
        val errors = listOf(AttributeError(AttributeType.ACCURACY, errorEnabled = false))
        val input = AttributeInput(AttributeType.ACCURACY, "")

        val result = AttributeReducer.reduceErrors(errors, input)

        val expected = listOf(AttributeError(AttributeType.ACCURACY, errorEnabled = true))
        assertEquals(expected, result)
    }

    @Test
    fun `New errors do not affect previous list`() {
        val errors = listOf(AttributeError(AttributeType.ACCURACY, errorEnabled = false))
        val input = AttributeInput(AttributeType.DEXTERITY, "")

        val result = AttributeReducer.reduceErrors(errors, input)

        val expected = listOf(
            AttributeError(AttributeType.ACCURACY, errorEnabled = false),
            AttributeError(AttributeType.DEXTERITY, errorEnabled = true)
        )
        assertEquals(expected, result)
    }
}
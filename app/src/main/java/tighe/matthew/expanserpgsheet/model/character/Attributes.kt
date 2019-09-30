package tighe.matthew.expanserpgsheet.model.character

data class Attributes(
    val accuracy: Int = 0,
    val communication: Int = 0,
    val constitution: Int = 0,
    val dexterity: Int = 0,
    val fighting: Int = 0,
    val intelligence: Int = 0,
    val perception: Int = 0,
    val strength: Int = 0,
    val willpower: Int = 0
) : Iterable<AttributeData> {
    override fun iterator(): Iterator<AttributeData> {
        return listOf(
            AttributeData(AttributeType.ACCURACY, accuracy),
            AttributeData(AttributeType.COMMUNICATION, communication),
            AttributeData(AttributeType.CONSTITUTION, constitution),
            AttributeData(AttributeType.DEXTERITY, dexterity),
            AttributeData(AttributeType.FIGHTING, fighting),
            AttributeData(AttributeType.INTELLIGENCE, intelligence),
            AttributeData(AttributeType.PERCEPTION, perception),
            AttributeData(AttributeType.STRENGTH, strength),
            AttributeData(AttributeType.WILLPOWER, willpower)
        ).iterator()
    }

    companion object {
        const val UNFILLED_ATTRIBUTE = Int.MIN_VALUE
        val UNFILLED_ATTRIBUTES = Attributes(
            UNFILLED_ATTRIBUTE,
            UNFILLED_ATTRIBUTE,
            UNFILLED_ATTRIBUTE,
            UNFILLED_ATTRIBUTE,
            UNFILLED_ATTRIBUTE,
            UNFILLED_ATTRIBUTE,
            UNFILLED_ATTRIBUTE,
            UNFILLED_ATTRIBUTE,
            UNFILLED_ATTRIBUTE
        )
    }
}

data class AttributeData(val type: AttributeType, val value: Int)

enum class AttributeType {
    ACCURACY, COMMUNICATION, CONSTITUTION, DEXTERITY, FIGHTING, INTELLIGENCE, PERCEPTION, STRENGTH,
    WILLPOWER
}
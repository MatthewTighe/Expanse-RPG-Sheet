package tighe.matthew.expanserpgsheet.model.character

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import tighe.matthew.expanserpgsheet.model.condition.Condition

@Entity
data class Character @Ignore constructor(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    val maxFortune: Int = 0,
    val currentFortune: Int = maxFortune,
    @Embedded val attributes: Attributes = Attributes.UNFILLED_ATTRIBUTES,
    @Ignore val conditions: Set<Condition> = setOf()
) {
    constructor(id: Long, name: String, maxFortune: Int, currentFortune: Int, attributes: Attributes) :
            this(id, name, maxFortune, currentFortune, attributes, setOf())

    val defense: Int
        get() = 10 + attributes.dexterity

    val toughness: Int
        get() = attributes.constitution
}
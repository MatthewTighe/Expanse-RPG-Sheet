package tighe.matthew.expanserpgsheet.model.character

import androidx.room.*
import tighe.matthew.expanserpgsheet.model.condition.Condition

@Entity
@TypeConverters(ArmorTypeConverter::class)
data class Character @Ignore constructor(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    val maxFortune: Int = 0,
    val currentFortune: Int = maxFortune,
    @Embedded val attributes: Attributes = Attributes.UNFILLED_ATTRIBUTES,
    val armor: Armor = Armor.None,
    @Ignore val conditions: Set<Condition> = setOf()
) {
    constructor(id: Long, name: String, maxFortune: Int, currentFortune: Int, attributes: Attributes, armor: Armor) :
            this(id, name, maxFortune, currentFortune, attributes, armor, setOf())

    val defense: Int
        get() = 10 + attributes.dexterity

    val toughness: Int
        get() = attributes.constitution
}
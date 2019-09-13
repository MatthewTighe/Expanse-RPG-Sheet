package tighe.matthew.expanserpgsheet.model.condition

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.TypeConverters
import tighe.matthew.expanserpgsheet.model.character.Character

@Entity(tableName = "character_condition",
    primaryKeys = ["condition", "characterId"],
    foreignKeys = [ForeignKey(
        entity = Character::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("characterId"),
        onDelete = ForeignKey.CASCADE)],
    indices = [Index(value = ["characterId"])])
@TypeConverters(ConditionTypeConverter::class)
data class CharacterCondition(
    val condition: Condition,
    val characterId: Long
)
package tighe.matthew.expanserpgsheet.model.character

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
    @Ignore val conditions: List<Condition> = listOf()
) {
    constructor(id: Long, name: String, maxFortune: Int, currentFortune: Int) : this(id, name, maxFortune, currentFortune, listOf())
}
package tighe.matthew.expanserpgsheet.model.character

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Character(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String = "",
    val maxFortune: Int = 0,
    val currentFortune: Int = maxFortune
)
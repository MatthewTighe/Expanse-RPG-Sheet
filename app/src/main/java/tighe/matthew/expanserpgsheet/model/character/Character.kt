package tighe.matthew.expanserpgsheet.model.character

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import tighe.matthew.expanserpgsheet.model.Model

@Parcelize
@Entity
data class Character(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String = "",
    val maxFortune: Int = 0
) : Model {
    override val bundleKey: String
        get() = "character"
}
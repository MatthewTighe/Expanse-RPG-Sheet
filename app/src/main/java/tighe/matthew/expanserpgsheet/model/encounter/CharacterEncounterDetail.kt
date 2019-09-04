package tighe.matthew.expanserpgsheet.model.encounter

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import tighe.matthew.expanserpgsheet.model.Model
import tighe.matthew.expanserpgsheet.model.character.Character

@Parcelize
@Entity(tableName = "character_encounter_detail")
data class CharacterEncounterDetail(
    @PrimaryKey val position: Int,
    val initiative: Int,
    val characterId: Long
) : Model {
    override val bundleKey: String
        get() = "encounterCharacter"
}
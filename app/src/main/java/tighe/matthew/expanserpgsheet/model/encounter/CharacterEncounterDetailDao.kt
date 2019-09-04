package tighe.matthew.expanserpgsheet.model.encounter

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterEncounterDetailDao {
    @Insert
    suspend fun insert(encounterCharacterDetails: CharacterEncounterDetail)

    @Query("SELECT * FROM character_encounter_detail")
    fun getAll(): List<CharacterEncounterDetail>

    @Query("SELECT * FROM character_encounter_detail")
    fun flowAll(): Flow<List<CharacterEncounterDetail>>

    @Query("DELETE FROM character_encounter_detail")
    suspend fun deleteAll()
}
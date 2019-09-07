package tighe.matthew.expanserpgsheet.model.encounter

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterEncounterDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(encounterCharacterDetails: CharacterEncounterDetail)

    @Update
    suspend fun update(encounterCharacterDetails: CharacterEncounterDetail)

    @Query("SELECT * FROM character_encounter_detail")
    suspend fun getAll(): List<CharacterEncounterDetail>

    @Query("SELECT * FROM character_encounter_detail")
    fun flowAll(): Flow<List<CharacterEncounterDetail>>

    @Query("DELETE FROM character_encounter_detail")
    suspend fun deleteAll()
}
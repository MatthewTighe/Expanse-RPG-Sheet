package tighe.matthew.expanserpgsheet.model.encounter

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EncounterDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(encounterDetails: EncounterDetail)

    @Update
    suspend fun update(encounterDetails: EncounterDetail)

    @Query("SELECT * FROM character_encounter_detail")
    suspend fun getAll(): List<EncounterDetail>

    @Query("SELECT * FROM character_encounter_detail")
    fun observeAll(): Flow<List<EncounterDetail>>

    @Query("DELETE FROM character_encounter_detail")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(detail: EncounterDetail)
}
package tighe.matthew.expanserpgsheet.model.condition

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterConditionDao {
    @Insert
    suspend fun insert(characterCondition: CharacterCondition)

    @Delete
    suspend fun delete(characterCondition: CharacterCondition)

    @Query("SELECT * FROM character_condition WHERE characterId = :characterId")
    suspend fun getCharacterConditionsById(characterId: Long): List<CharacterCondition>

    @Query("SELECT * FROM character_condition")
    fun observeAll(): Flow<List<CharacterCondition>>
}
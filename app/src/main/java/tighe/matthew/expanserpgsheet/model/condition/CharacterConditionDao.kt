package tighe.matthew.expanserpgsheet.model.condition

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CharacterConditionDao {
    @Insert
    suspend fun insert(characterCondition: CharacterCondition)

    @Query("SELECT * FROM character_condition WHERE characterId = :characterId")
    suspend fun getCharacterConditionsById(characterId: Long): List<CharacterCondition>
}
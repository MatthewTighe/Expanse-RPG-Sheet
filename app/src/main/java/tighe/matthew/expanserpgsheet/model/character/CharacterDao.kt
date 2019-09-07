package tighe.matthew.expanserpgsheet.model.character

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: Character): Long

    @Update
    suspend fun update(character: Character)

    @Delete
    suspend fun delete(character: Character)

    @Query("SELECT * FROM character")
    fun loadAll(): List<Character>

    @Query("SELECT * FROM character")
    fun observeAll(): Flow<List<Character>>

    @Query("SELECT * FROM character WHERE id = :id")
    suspend fun getById(id: Long): Character
}
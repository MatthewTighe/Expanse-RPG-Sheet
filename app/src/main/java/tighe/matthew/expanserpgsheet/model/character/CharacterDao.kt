package tighe.matthew.expanserpgsheet.model.character

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: Character): Long

    @Delete
    suspend fun delete(character: Character)

    @Query("SELECT * FROM character")
    fun loadAll(): List<Character>

    @Query("SELECT * FROM character")
    fun observeAll(): Flow<List<Character>>

    @Query("SELECT * FROM character WHERE id = :id")
    suspend fun getById(id: Long): Character
}
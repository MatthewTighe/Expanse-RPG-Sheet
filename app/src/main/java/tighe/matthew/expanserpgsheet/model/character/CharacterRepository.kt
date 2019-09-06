package tighe.matthew.expanserpgsheet.model.character

import android.content.Context
import kotlinx.coroutines.flow.Flow
import tighe.matthew.expanserpgsheet.model.AppDatabase

class CharacterRepository(context: Context) {
    private val characterDao = AppDatabase.getInstance(context).characterDao()

    suspend fun persist(model: Character) {
        characterDao.insert(model)
    }

    suspend fun load(id: Long): Character {
        return characterDao.getById(id)
    }

    fun observeAll(): Flow<List<Character>> {
        return characterDao.observeAll()
    }

    suspend fun delete(model: Character) {
        characterDao.delete(model)
    }
}
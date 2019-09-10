package tighe.matthew.expanserpgsheet.model.character

import kotlinx.coroutines.flow.Flow

class CharacterRepository(private val characterDao: CharacterDao) {

    suspend fun persist(model: Character) {
        characterDao.insert(model)
    }

    suspend fun load(id: Long): Character {
        return characterDao.getById(id)
    }

    suspend fun update(character: Character) {
        characterDao.update(character)
    }

    fun observeAll(): Flow<List<Character>> {
        return characterDao.observeAll()
    }

    suspend fun delete(model: Character) {
        characterDao.delete(model)
    }
}
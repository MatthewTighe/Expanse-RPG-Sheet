package tighe.matthew.expanserpgsheet.model.character

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tighe.matthew.expanserpgsheet.model.condition.CharacterConditionDao
import tighe.matthew.expanserpgsheet.model.condition.Condition

class CharacterRepository(private val characterDao: CharacterDao, private val conditionDao: CharacterConditionDao) {

    suspend fun persist(model: Character) {
        characterDao.insert(model)
    }

    suspend fun load(id: Long): Character {
        val character = characterDao.getById(id)
        return character.copy(conditions = getConditionsForId(id) )
    }

    suspend fun update(character: Character) {
        characterDao.update(character)
    }

    fun observeAll(): Flow<List<Character>> {
//        return characterDao.observeAll()
        return characterDao.observeAll().map { list -> list.map { character ->
            val conditions = getConditionsForId(character.id)
            character.copy(conditions = conditions)
        }}
    }

    suspend fun delete(model: Character) {
        characterDao.delete(model)
    }

    private suspend fun getConditionsForId(id: Long): List<Condition> {
        return conditionDao.getCharacterConditionsById(id).map { it.condition }
    }
}
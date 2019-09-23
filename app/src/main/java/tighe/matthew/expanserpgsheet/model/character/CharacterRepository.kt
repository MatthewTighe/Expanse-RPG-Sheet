package tighe.matthew.expanserpgsheet.model.character

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import tighe.matthew.expanserpgsheet.model.condition.CharacterCondition
import tighe.matthew.expanserpgsheet.model.condition.CharacterConditionDao
import tighe.matthew.expanserpgsheet.model.condition.Condition

class CharacterRepository(private val characterDao: CharacterDao, private val conditionDao: CharacterConditionDao) {

    suspend fun persist(model: Character) {
        characterDao.insert(model)
    }

    suspend fun load(id: Long): Character {
        val character = characterDao.getById(id)
        return character.copy(conditions = getConditionsForId(id).toSet())
    }

    suspend fun update(character: Character) {
        characterDao.update(character)
    }

    suspend fun addCondition(condition: Condition, character: Character) {
        val characterCondition = CharacterCondition(condition, character.id)
        conditionDao.insert(characterCondition)
    }

    suspend fun removeCondition(condition: Condition, character: Character) {
        val characterCondition = CharacterCondition(condition, character.id)
        conditionDao.delete(characterCondition)
    }

    fun observeBase(): Flow<List<Character>> {
        return characterDao.observeAll()
    }

    @ExperimentalCoroutinesApi
    fun observeWithConditions(): Flow<List<Character>> {
        val conditionFlow = conditionDao.observeAll()
        val characterFlow = characterDao.observeAll()
        return characterFlow.combine(conditionFlow) { characterList, conditionList ->
            characterList.map { character ->
                val conditions = conditionList
                    .filter { it.characterId == character.id }
                    .map { it.condition }
                character.copy(conditions = conditions.toSet())
            }
        }
    }

    suspend fun delete(model: Character) {
        characterDao.delete(model)
    }

    private suspend fun getConditionsForId(id: Long): List<Condition> {
        return conditionDao.getCharacterConditionsById(id).map { it.condition }
    }
}
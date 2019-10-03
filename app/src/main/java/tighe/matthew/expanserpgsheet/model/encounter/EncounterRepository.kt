package tighe.matthew.expanserpgsheet.model.encounter

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterDao
import tighe.matthew.expanserpgsheet.model.condition.CharacterConditionDao

class EncounterRepository(
    private val characterDao: CharacterDao,
    private val conditionDao: CharacterConditionDao,
    private val encounterDetailDao: EncounterDetailDao
) {

    @ExperimentalCoroutinesApi
    fun getEncounter(): Flow<List<EncounterCharacter>> {
        val characterFlow = characterDao.observeAll()
        val conditionFlow = conditionDao.observeAll()
        val encounterDetailFlow = encounterDetailDao.observeAll()

        return characterFlow
            .combine(conditionFlow) { characterList, conditionList ->
                characterList.map { character ->
                    val conditions = conditionList
                        .filter { it.characterId == character.id }
                        .map { it.condition }
                    character.copy(conditions = conditions.toSet())
                }
            }
            .combine(encounterDetailFlow) { characterList, encounterDetailList ->
                encounterDetailList.map { encounterDetail ->
                    val character = characterList.find { it.id == encounterDetail.characterId }!!
                    EncounterCharacter(character, encounterDetail)
                }.sortedBy { it.detail.position }
            }
    }

    suspend fun addCharacter(character: Character, initiative: Int) {
        if (characterIsInEncounter(character)) return
        val position = getNewPositionByInitiative(initiative)

        val modificationAction: (suspend (EncounterDetail) -> Unit) = { detail ->
            val updatedDetail = detail.copy(position = detail.position + 1)
            encounterDetailDao.insert(updatedDetail)
        }

        updateCurrentPositions(position, modificationAction)
        val encounterCharacterDetail = EncounterDetail(
            characterId = character.id,
            initiative = initiative,
            position = position
        )
        encounterDetailDao.insert(encounterCharacterDetail)
    }

    suspend fun characterIsInEncounter(character: Character): Boolean {
        val allDetails = encounterDetailDao.getAll()
        return allDetails.any { it.characterId == character.id }
    }

    suspend fun updateEncounterCharacter(character: EncounterCharacter) {
        characterDao.update(character.character)
        encounterDetailDao.update(character.detail)
    }

    suspend fun removeEncounterCharacter(character: EncounterCharacter, position: Int) {
        encounterDetailDao.delete(character.detail)

        val modificationAction: (suspend (EncounterDetail) -> Unit) = { detail ->
            val updatedDetail = detail.copy(position = detail.position - 1)
            encounterDetailDao.insert(updatedDetail)
            encounterDetailDao.delete(detail)
        }

        updateCurrentPositions(position, modificationAction)
    }

    suspend fun removeAll() {
        encounterDetailDao.deleteAll()
    }

    private suspend fun getNewPositionByInitiative(initiative: Int): Int {
        val details = encounterDetailDao.getAll()
        // New position should be the first where initiative is greater than an existing.
        val index = details.indexOfFirst { encounterCharacter ->
            initiative >= encounterCharacter.initiative
        }
        return if (index == -1) details.size else index
    }

    private suspend fun updateCurrentPositions(
        changedPosition: Int,
        modificationAction: suspend (detail: EncounterDetail) -> Unit
    ) {
        val details = encounterDetailDao.getAll()
        for (detail in details) {
            if (detail.position >= changedPosition) {
                modificationAction(detail)
            }
        }
    }
}
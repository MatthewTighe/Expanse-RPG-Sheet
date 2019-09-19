package tighe.matthew.expanserpgsheet.model.encounter

import kotlinx.coroutines.flow.*
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterDao

class EncounterRepository(
    private val characterDao: CharacterDao,
    private val characterEncounterDetailDao: CharacterEncounterDetailDao
) {

    fun getEncounter(): Flow<Encounter> {
        return characterEncounterDetailDao.flowAll().map { list ->
            val sorted = list.sortedBy { detail -> detail.position }
            Encounter(sorted.toEncounterCharacters())
        }
    }

    suspend fun addCharacter(character: Character, initiative: Int) {
        if (characterIsInEncounter(character)) return
        val position = getNewPositionByInitiative(initiative)

        val modificationAction: (suspend (CharacterEncounterDetail) -> Unit) = { detail ->
            val updatedDetail = detail.copy(position = detail.position + 1)
            characterEncounterDetailDao.insert(updatedDetail)
        }

        updateCurrentPositions(position, modificationAction)
        val encounterCharacterDetail = CharacterEncounterDetail(
            characterId = character.id,
            initiative = initiative,
            position = position
        )
        characterEncounterDetailDao.insert(encounterCharacterDetail)
    }

    suspend fun characterIsInEncounter(character: Character): Boolean {
        val allDetails = characterEncounterDetailDao.getAll()
        return allDetails.any { it.characterId == character.id }
    }

    suspend fun updateEncounterCharacter(character: EncounterCharacter) {
        characterDao.update(character.character)
        characterEncounterDetailDao.update(character.detail)
    }

    suspend fun removeEncounterCharacter(character: EncounterCharacter, position: Int) {
        characterEncounterDetailDao.delete(character.detail)

        val modificationAction: (suspend (CharacterEncounterDetail) -> Unit) = { detail ->
            val updatedDetail = detail.copy(position = detail.position - 1)
            characterEncounterDetailDao.insert(updatedDetail)
            characterEncounterDetailDao.delete(detail)
        }

        updateCurrentPositions(position, modificationAction)
    }

    private suspend fun List<CharacterEncounterDetail>.toEncounterCharacters(): List<EncounterCharacter> {
        return this.map { encounterCharacterDetail ->
            val character = characterDao.getById(encounterCharacterDetail.characterId)
            EncounterCharacter(character, encounterCharacterDetail)
        }
    }

    private suspend fun getNewPositionByInitiative(initiative: Int): Int {
        val details = characterEncounterDetailDao.getAll()
        // New position should be the first where initiative is greater than an existing.
        val index = details.indexOfFirst { encounterCharacter ->
            initiative >= encounterCharacter.initiative
        }
        return if (index == -1) details.size else index
    }

    private suspend fun updateCurrentPositions(
        changedPosition: Int,
        modificationAction: suspend (detail: CharacterEncounterDetail) -> Unit
    ) {
        val details = characterEncounterDetailDao.getAll()
        for (detail in details) {
            if (detail.position >= changedPosition) {
                modificationAction(detail)
            }
        }
    }
}
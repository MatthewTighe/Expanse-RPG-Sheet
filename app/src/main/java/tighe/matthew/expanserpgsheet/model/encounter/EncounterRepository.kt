package tighe.matthew.expanserpgsheet.model.encounter

import kotlinx.coroutines.flow.*
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterDao

class EncounterRepository(
    private val characterDao: CharacterDao,
    private val characterEncounterDetailDao: CharacterEncounterDetailDao
) {
    private var testInit = 0

    fun getEncounter(): Flow<Encounter> {
        return characterEncounterDetailDao.flowAll().map { list ->
            val sorted = list.sortedByDescending { detail -> detail.position }
            Encounter(sorted.toEncounterCharacters())
        }
    }

    suspend fun addCharacter(character: Character, initiative: Int) {
        val position = getNewPositionByInitiative(initiative)
        testInit += 1
        val encounterCharacterDetail = CharacterEncounterDetail(
            characterId = character.id,
            initiative = initiative,
            position = testInit
        )
        characterEncounterDetailDao.insert(encounterCharacterDetail)
    }

    suspend fun updateEncounterCharacter(character: EncounterCharacter) {
        characterDao.update(character.character)
        characterEncounterDetailDao.update(character.detail)
    }

    private suspend fun List<CharacterEncounterDetail>.toEncounterCharacters(): List<EncounterCharacter> {
        return this.map { encounterCharacterDetail ->
            val character = characterDao.getById(encounterCharacterDetail.characterId)
            EncounterCharacter(character, encounterCharacterDetail)
        }
    }

    private suspend fun getNewPositionByInitiative(initiative: Int): Int {
        val details = characterEncounterDetailDao.getAll()
        val index = details.indexOfFirst { encounterCharacter ->
            encounterCharacter.initiative <= initiative
        }
        return if (index == -1) 0 else index
    }
}
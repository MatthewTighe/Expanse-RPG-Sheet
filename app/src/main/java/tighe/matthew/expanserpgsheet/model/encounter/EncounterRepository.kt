package tighe.matthew.expanserpgsheet.model.encounter

import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import tighe.matthew.expanserpgsheet.model.AppDatabase
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterDao

class EncounterRepository(
    private val characterDao: CharacterDao,
    private val characterEncounterDetailDao: CharacterEncounterDetailDao
) {
    private val encounterCharacters = mutableListOf<EncounterCharacter>()

    private var testInit = 0

    fun getEncounter(): Flow<Encounter> {
        return characterEncounterDetailDao.flowAll().map { list ->
            val sorted = list.sortedByDescending { detail -> detail.position }
            encounterCharacters.clear()
            encounterCharacters.addAll(sorted.toEncounterCharacters())
            Encounter(encounterCharacters)
        }
    }

    suspend fun addCharacter(character: Character, initiative: Int) {
        val position = getNewPositionByInitiative(initiative)
        testInit += 1
        val encounterCharacter = CharacterEncounterDetail(
            characterId = character.id,
            initiative = initiative,
            position = testInit
        )
        characterEncounterDetailDao.insert(encounterCharacter)
    }

    private suspend fun List<CharacterEncounterDetail>.toEncounterCharacters(): List<EncounterCharacter> {
        return this.map { encounterCharacterDetail ->
            val character = characterDao.getById(encounterCharacterDetail.characterId)
            EncounterCharacter(character, encounterCharacterDetail)
        }
    }

    private fun getNewPositionByInitiative(initiative: Int): Int {
        val index = encounterCharacters.indexOfFirst { encounterCharacter ->
            encounterCharacter.detail.initiative <= initiative
        }
        return if (index == -1) 0 else index
    }
}
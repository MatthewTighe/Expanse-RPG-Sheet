package tighe.matthew.expanserpgsheet.model.encounter

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterDao

class EncounterRepositoryTest {

    @MockK lateinit var mockCharacterDao: CharacterDao

    @MockK lateinit var mockCharacterEncounterDetailDao: CharacterEncounterDetailDao

    private val testId = 1L
    private val testDetail = CharacterEncounterDetail(0, 1, testId)
    private val testCharacter = Character(testId, "name", 10)
    private val testEncounterCharacter = EncounterCharacter(testCharacter, testDetail)

    private lateinit var repo: EncounterRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repo = EncounterRepository(mockCharacterDao, mockCharacterEncounterDetailDao)
    }

    @Test
    fun `Encounter is built from characters and their associated details`() = runBlockingTest {
        val testFlow = flow {
            emit(listOf(testDetail))
        }
        every { mockCharacterEncounterDetailDao.flowAll() } returns testFlow
        coEvery { mockCharacterDao.getById(testId) } returns testCharacter

        val result = repo.getEncounter().first()

        val expectedEncounter = Encounter(listOf(testEncounterCharacter))
        assertEquals(expectedEncounter, result)
    }

    @Test
    fun `Position is generated based on higher initiative`() = runBlockingTest {
        val character = Character(2, "two")
        val originalDetail = CharacterEncounterDetail(0, 1, 1)
        val addedDetail = CharacterEncounterDetail(0, 2, 2)

        coEvery { mockCharacterEncounterDetailDao.getAll() } returns listOf(originalDetail)

        repo.addCharacter(character, addedDetail.initiative)

        coVerify { mockCharacterEncounterDetailDao.insert(addedDetail) }
    }

    @Test
    fun `Details are shifted when a character with higher initiative is added`() = runBlockingTest {
        val lowInitiativeDetail = CharacterEncounterDetail(1, 2, 0)
        val middleInitiativeDetail = CharacterEncounterDetail(0, 4, 0)
        val initialDetails = listOf(lowInitiativeDetail, middleInitiativeDetail)

        val addedDetail = CharacterEncounterDetail(0, 10, 1)
        val character = Character(1, "name")

        coEvery { mockCharacterEncounterDetailDao.getAll() } returns initialDetails

        repo.addCharacter(character, addedDetail.initiative)

        val updatedLow = lowInitiativeDetail.copy(position = lowInitiativeDetail.position + 1)
        val updatedMiddle = middleInitiativeDetail.copy(position = middleInitiativeDetail.position + 1)
        coVerify {
            mockCharacterEncounterDetailDao.insert(updatedLow)
            mockCharacterEncounterDetailDao.insert(updatedMiddle)
            mockCharacterEncounterDetailDao.insert(addedDetail)
        }
    }

    @Test
    fun `updateEncounterCharacter calls character and detail Daos separately`() = runBlockingTest {
        repo.updateEncounterCharacter(testEncounterCharacter)

        coVerify { mockCharacterDao.update(testEncounterCharacter.character) }
        coVerify { mockCharacterEncounterDetailDao.update(testEncounterCharacter.detail) }
    }

    @Test
    fun `Characters cannot be added twice to the same encounter`() = runBlockingTest {
        val character = Character(0, "name", 10)
        val details = CharacterEncounterDetail(1, 0, 0)

        coEvery { mockCharacterEncounterDetailDao.getAll() } returns listOf(details)
        coEvery { mockCharacterEncounterDetailDao.flowAll() } returns flow {
            emit(listOf(details))
        }
        coEvery { mockCharacterDao.getById(0) } returns character

        repo.addCharacter(character, 0)
        repo.addCharacter(character, 0)

        val result = repo.getEncounter().first()
        assertEquals(1, result.characters.size)
    }
}
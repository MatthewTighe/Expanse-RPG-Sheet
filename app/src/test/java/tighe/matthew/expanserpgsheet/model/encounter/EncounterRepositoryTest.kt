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

    @MockK lateinit var mockEncounterDetailDao: EncounterDetailDao

    private val testId = 1L
    private val testDetail = EncounterDetail(0, 1, testId)
    private val testCharacter = Character(testId, "name", 10)
    private val testEncounterCharacter = EncounterCharacter(testCharacter, testDetail)

    private lateinit var repo: EncounterRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repo = EncounterRepository(mockCharacterDao, mockEncounterDetailDao)
    }

    @Test
    fun `Encounter is built from characters and their associated details`() = runBlockingTest {
        val testFlow = flow {
            emit(listOf(testDetail))
        }
        every { mockEncounterDetailDao.flowAll() } returns testFlow
        coEvery { mockCharacterDao.getById(testId) } returns testCharacter

        val result = repo.getEncounter().first()

        val expectedEncounter = Encounter(listOf(testEncounterCharacter))
        assertEquals(expectedEncounter, result)
    }

    @Test
    fun `Position is generated based on higher initiative`() = runBlockingTest {
        val character = Character(2, "two")
        val originalDetail = EncounterDetail(0, 1, 1)
        val addedDetail = EncounterDetail(0, 2, 2)

        coEvery { mockEncounterDetailDao.getAll() } returns listOf(originalDetail)

        repo.addCharacter(character, addedDetail.initiative)

        coVerify { mockEncounterDetailDao.insert(addedDetail) }
    }

    // Order of the initial lists matters for tests of initiative ordering. In real scenarios,
    // the lists should be ordered automatically during insertion into the database.

    @Test
    fun `High initiative entries shift every other entry down`() = runBlockingTest {
        val lowInitiativeDetail = EncounterDetail(1, 2, 0)
        val middleInitiativeDetail = EncounterDetail(0, 4, 0)
        val initialDetails = listOf(middleInitiativeDetail, lowInitiativeDetail)

        val addedDetail = EncounterDetail(0, 10, 1)
        val character = Character(1, "name")

        coEvery { mockEncounterDetailDao.getAll() } returns initialDetails

        repo.addCharacter(character, addedDetail.initiative)

        val updatedLow = lowInitiativeDetail.copy(position = lowInitiativeDetail.position + 1)
        val updatedMiddle = middleInitiativeDetail.copy(position = middleInitiativeDetail.position + 1)
        coVerify {
            mockEncounterDetailDao.insert(updatedLow)
            mockEncounterDetailDao.insert(updatedMiddle)
            mockEncounterDetailDao.insert(addedDetail)
        }
    }

    @Test
    fun `Adding a character with middle initiative shifts only lower initiatives down`() = runBlockingTest {
        val lowInitiativeDetail = EncounterDetail(1, 2, 0)
        val highInitiativeDetail = EncounterDetail(0, 10, 0)
        val initialDetails = listOf(highInitiativeDetail, lowInitiativeDetail)

        val addedDetail = EncounterDetail(1, 5, 1)
        val character = Character(1, "name")

        coEvery { mockEncounterDetailDao.getAll() } returns initialDetails

        repo.addCharacter(character, addedDetail.initiative)

        val updatedLow = lowInitiativeDetail.copy(position = lowInitiativeDetail.position + 1)
        coVerify {
            mockEncounterDetailDao.insert(updatedLow)
            mockEncounterDetailDao.insert(addedDetail)
        }
        val incorrectlyShiftedFirstEntry = highInitiativeDetail.copy(position = highInitiativeDetail.position + 1)
        coVerify(exactly = 0) { mockEncounterDetailDao.insert(incorrectlyShiftedFirstEntry) }
    }

    @Test
    fun `Adding a character with low initiative does not shift any other characters down`() = runBlockingTest {
        val middleInitiativeDetail = EncounterDetail(1, 5, 0)
        val highInitiativeDetail = EncounterDetail(0, 10, 0)
        val initialDetails = listOf(highInitiativeDetail, middleInitiativeDetail)

        val addedDetail = EncounterDetail(2, 2, 1)
        val character = Character(1, "name")

        coEvery { mockEncounterDetailDao.getAll() } returns initialDetails

        repo.addCharacter(character, addedDetail.initiative)

        coVerify {
            mockEncounterDetailDao.insert(addedDetail)
        }
        val incorrectlyShiftedMidEntry = middleInitiativeDetail.copy(position = middleInitiativeDetail.position + 1)
        val incorrectlyShiftedFirstEntry = highInitiativeDetail.copy(position = highInitiativeDetail.position + 1)
        coVerify(exactly = 0) {
            mockEncounterDetailDao.insert(incorrectlyShiftedMidEntry)
            mockEncounterDetailDao.insert(incorrectlyShiftedFirstEntry)
        }
    }

    @Test
    fun `Positions are shifted up when a character is removed`() = runBlockingTest {
        val bottomCharacter = EncounterDetail(2, 2, 1)
        val middleCharacter = EncounterDetail(1, 3, 1)
        val topCharacter = EncounterDetail(0, 5, 1)

        val characterToRemove = Character(1, "name")
        val middleEncounterCharacter = EncounterCharacter(characterToRemove, middleCharacter)

        val allCharacterDetails = listOf(bottomCharacter, middleCharacter, topCharacter)
        coEvery { mockEncounterDetailDao.getAll() } returns allCharacterDetails

        repo.removeEncounterCharacter(middleEncounterCharacter, 1)

        val updatedBottom = bottomCharacter.copy(position = bottomCharacter.position - 1)
        coVerify {
            mockEncounterDetailDao.delete(middleCharacter)
            mockEncounterDetailDao.insert(updatedBottom)
            mockEncounterDetailDao.delete(bottomCharacter)
        }
    }

    @Test
    fun `updateEncounterCharacter calls character and detail Daos separately`() = runBlockingTest {
        repo.updateEncounterCharacter(testEncounterCharacter)

        coVerify { mockCharacterDao.update(testEncounterCharacter.character) }
        coVerify { mockEncounterDetailDao.update(testEncounterCharacter.detail) }
    }

    @Test
    fun `Characters cannot be added twice to the same encounter`() = runBlockingTest {
        val character = Character(0, "name", 10)
        val details = EncounterDetail(1, 0, 0)

        coEvery { mockEncounterDetailDao.getAll() } returns listOf(details)
        coEvery { mockEncounterDetailDao.flowAll() } returns flow {
            emit(listOf(details))
        }
        coEvery { mockCharacterDao.getById(0) } returns character

        repo.addCharacter(character, 0)
        repo.addCharacter(character, 0)

        val result = repo.getEncounter().first()
        assertEquals(1, result.characters.size)
    }
}
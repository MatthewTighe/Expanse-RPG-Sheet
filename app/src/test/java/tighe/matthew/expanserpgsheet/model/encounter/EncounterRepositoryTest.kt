package tighe.matthew.expanserpgsheet.model.encounter

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
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

        val expectedCharacter = EncounterCharacter(testCharacter, testDetail)
        val expectedEncounter = Encounter(listOf(expectedCharacter))
        assertEquals(expectedEncounter, result)
    }

    @Test
    fun `Position is generated based on initiative`() {
        // TODO
    }
}
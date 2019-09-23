package tighe.matthew.expanserpgsheet.model.character

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import tighe.matthew.expanserpgsheet.model.condition.CharacterCondition
import tighe.matthew.expanserpgsheet.model.condition.CharacterConditionDao
import tighe.matthew.expanserpgsheet.model.condition.Condition

class CharacterRepositoryTest {

    @MockK private lateinit var mockCharacterDao: CharacterDao
    @MockK private lateinit var mockConditionDao: CharacterConditionDao

    private lateinit var repo: CharacterRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repo = CharacterRepository(mockCharacterDao, mockConditionDao)
    }

    @Test
    fun `observeWithConditions builds characters with their conditions`() = runBlockingTest {
        val testId = 1L
        val testCharacter = Character(id = testId, name = "test")

        val testCharacterCondition = CharacterCondition(Condition.Injured, testId)

        coEvery { mockConditionDao.observeAll() } returns flow { emit(listOf(testCharacterCondition)) }
        coEvery { mockCharacterDao.observeAll() } returns flow { emit(listOf(testCharacter)) }

        val result = repo.observeWithConditions().first()

        val expected = listOf(testCharacter.copy(conditions = setOf(Condition.Injured)))
        assertEquals(expected, result)
    }
}
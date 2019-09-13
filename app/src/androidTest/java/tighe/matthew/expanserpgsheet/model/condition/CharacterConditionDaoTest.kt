package tighe.matthew.expanserpgsheet.model.condition

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tighe.matthew.expanserpgsheet.model.AppDatabase
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterDao

@RunWith(AndroidJUnit4::class)
class CharacterConditionDaoTest {
    @get:Rule val rule = InstantTaskExecutorRule()

    private val testCharacterId = 1L
    private val testCharacter = Character(testCharacterId, "name")

    private lateinit var db: AppDatabase
    private lateinit var characterDao: CharacterDao
    private lateinit var characterConditionDao: CharacterConditionDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        characterDao = db.characterDao()
        characterConditionDao = db.characterConditionDao()

        runBlocking {
            characterDao.insert(testCharacter)
        }
    }

    @Test
    fun injuryConditionIsConvertedSuccessfully() = runBlocking {
        val condition = CharacterCondition(Condition.Injured, testCharacterId)

        characterConditionDao.insert(condition)
        val result = characterConditionDao.getCharacterConditionsById(testCharacterId)

        assertEquals(condition, result.first())
    }

    @Test
    fun woundedConditionIsConvertedSuccessfully() = runBlocking {
        val condition = CharacterCondition(Condition.Wounded, testCharacterId)

        characterConditionDao.insert(condition)
        val result = characterConditionDao.getCharacterConditionsById(testCharacterId)

        assertEquals(condition, result.first())
    }

    @Test
    fun takenOutConditionIsConvertedSuccessfully() = runBlocking {
        val condition = CharacterCondition(Condition.TakenOut, testCharacterId)

        characterConditionDao.insert(condition)
        val result = characterConditionDao.getCharacterConditionsById(testCharacterId)

        assertEquals(condition, result.first())
    }
}
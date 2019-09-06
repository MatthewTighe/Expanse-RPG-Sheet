package tighe.matthew.expanserpgsheet.model.encounter

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
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
class CharacterEncounterDetailDaoTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var detailDao: CharacterEncounterDetailDao
    private lateinit var characterDao: CharacterDao

    private val characterId = 1L
    private val detail = CharacterEncounterDetail(0, 1, characterId)
    private val character = Character(characterId, "name", 0)

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        detailDao = db.characterEncounterDetailDao()
        characterDao = db.characterDao()
        runBlocking { characterDao.insert(character) }
    }

    @Test
    fun detailCanBeInserted() = runBlocking {
        detailDao.insert(detail)

        val result = detailDao.getAll()

        assertEquals(listOf(detail), result)
    }

    @Test
    fun detailsCanBeObserved() = runBlocking {
        detailDao.insert(detail)

        val result = detailDao.flowAll().first()

        assertEquals(listOf(detail), result)
    }

    @Test
    fun detailsCanBeCleared() = runBlocking {
        detailDao.insert(detail)
        detailDao.deleteAll()

        val result = detailDao.getAll()

        assertEquals(listOf<CharacterEncounterDetail>(), result)
    }

    @Test
    fun deletingCharacterRemovesItFromDetailsTable() = runBlocking {
        detailDao.insert(detail)
        characterDao.delete(character)

        val result = detailDao.getAll()

        assertEquals(listOf<CharacterEncounterDetail>(), result)
    }
}
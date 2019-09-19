package tighe.matthew.expanserpgsheet.model.character

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tighe.matthew.expanserpgsheet.model.AppDatabase

@RunWith(AndroidJUnit4::class)
class CharacterDaoTest {
    @get:Rule val rule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var characterDao: CharacterDao

    private val character = Character(0, "name", 10)

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        characterDao = db.characterDao()
    }

    @Test
    fun primaryKeyIsRespected() = runBlocking {
        val char1 = character.copy(id = 1, name = "1")
        val char2 = character.copy(id = 1, name = "2")

        characterDao.insert(char1)
        characterDao.insert(char2)

        val result = characterDao.observeAll().first()

        assertEquals(1, result.size)
        assertEquals(char2, result[0])
    }

    @Test
    fun characterCanBeInserted() = runBlocking {
        val id = characterDao.insert(character)

        val result = characterDao.getById(id)
        assertEquals(character.name, result.name)
        assertEquals(character.maxFortune, result.maxFortune)
    }

    @Test
    fun characterCanBeUpdated() = runBlocking {
        val id = characterDao.insert(character)

        val updated = character.copy(id = id, name = "update")
        characterDao.update(updated)

        val result = characterDao.getById(id)
        assertEquals(updated, result)
    }

    @Test
    fun characterCanBeDeleted() = runBlocking {
        val id = characterDao.insert(character)

        val updatedCharacter = character.copy(id = id)
        characterDao.delete(updatedCharacter)

        val result = characterDao.getById(id)
        assertNull(result)
    }

    @Test
    fun characterListCanBeObserved() = runBlocking {
        val id = characterDao.insert(character)

        val data = characterDao.observeAll().first()

        val expected = listOf(character.copy(id = id))
        assertEquals(expected, data)
    }

    @Test
    fun characterCanBeRetrievedById() = runBlocking {
        val id = characterDao.insert(character)

        val result = characterDao.getById(id)

        val expected = character.copy(id = id)
        assertEquals(expected, result)
    }
}
package tighe.matthew.expanserpgsheet.model.character

import android.content.Context
import androidx.lifecycle.LiveData
import tighe.matthew.expanserpgsheet.appendStringSet
import tighe.matthew.expanserpgsheet.delete
import tighe.matthew.expanserpgsheet.model.AppDatabase
import tighe.matthew.expanserpgsheet.model.DatabaseEntity
import tighe.matthew.expanserpgsheet.model.Repository
import tighe.matthew.expanserpgsheet.putInt
import tighe.matthew.expanserpgsheet.removeFromStringSet

class CharacterRepository(context: Context) : Repository<Character> {
    private val characterDao = AppDatabase.getInstance(context).characterDao()

    override suspend fun persist(model: Character) {
        characterDao.insert(model)
    }

    override suspend fun load(id: Long): Character {
        return characterDao.getById(id)
    }

    override fun observeAll(): LiveData<List<Character>> {
        return characterDao.observeAll()
    }

    override suspend fun delete(model: Character) {
        characterDao.delete(model)
    }
}
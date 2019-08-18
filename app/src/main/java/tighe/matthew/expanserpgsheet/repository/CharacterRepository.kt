package tighe.matthew.expanserpgsheet.repository

import android.content.Context
import tighe.matthew.expanserpgsheet.appendStringSet
import tighe.matthew.expanserpgsheet.model.CharacterModel
import tighe.matthew.expanserpgsheet.putInt

class CharacterRepository(context: Context) : Repository<CharacterModel> {
    private val cacheName = "CharacterCache"
    private val cache = context.getSharedPreferences(cacheName, Context.MODE_PRIVATE)
    private val characterListKey = "allCharacters"

    override fun persist(model: CharacterModel) {
        cache.putInt(model.name, model.maxFortune)
        cache.appendStringSet(characterListKey, model.name)
    }

    override fun load(key: String): CharacterModel {
        val maxFortune = cache.getInt(key, 0)
        return CharacterModel(key, maxFortune)
    }

    fun loadAll(): List<CharacterModel> {
        val characters = cache.getStringSet(characterListKey, setOf()) ?: setOf()
        return characters.map { characterName ->
            load(characterName)
        }
    }
}
package tighe.matthew.expanserpgsheet.repository

import android.content.Context
import tighe.matthew.expanserpgsheet.model.CharacterModel
import tighe.matthew.expanserpgsheet.putInt

class CharacterRepository(context: Context) : Repository<CharacterModel> {
    private val cacheName = "CharacterCache"
    private val cache = context.getSharedPreferences(cacheName, Context.MODE_PRIVATE)

    override fun persist(model: CharacterModel) {
        cache.putInt(model.name, model.maxFortune)
    }

    override fun load(key: String): CharacterModel {
        val maxFortune = cache.getInt(key, 0)
        return CharacterModel(name = key, maxFortune = maxFortune)
    }
}
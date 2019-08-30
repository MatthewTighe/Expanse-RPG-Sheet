package tighe.matthew.expanserpgsheet.model

import androidx.lifecycle.LiveData

interface Repository<M : Model> {
    suspend fun persist(model: M)

    suspend fun load(id: Long): M

    fun observeAll(): LiveData<List<M>>

    suspend fun delete(model: M)
}
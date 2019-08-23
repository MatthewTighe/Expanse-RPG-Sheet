package tighe.matthew.expanserpgsheet.repository

import tighe.matthew.expanserpgsheet.model.Model

interface Repository<M : Model> {
    fun persist(model: M)

    fun load(key: String): M
}
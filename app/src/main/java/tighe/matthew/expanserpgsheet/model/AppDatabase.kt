package tighe.matthew.expanserpgsheet.model

import androidx.room.Database
import androidx.room.RoomDatabase
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterDao
import tighe.matthew.expanserpgsheet.model.condition.CharacterCondition
import tighe.matthew.expanserpgsheet.model.condition.CharacterConditionDao
import tighe.matthew.expanserpgsheet.model.encounter.CharacterEncounterDetail
import tighe.matthew.expanserpgsheet.model.encounter.CharacterEncounterDetailDao

@Database(
    entities = [
        Character::class,
        CharacterEncounterDetail::class,
        CharacterCondition::class
    ], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun characterEncounterDetailDao(): CharacterEncounterDetailDao
    abstract fun characterConditionDao(): CharacterConditionDao
}
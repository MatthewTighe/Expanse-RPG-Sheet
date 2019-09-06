package tighe.matthew.expanserpgsheet.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.character.CharacterDao
import tighe.matthew.expanserpgsheet.model.encounter.CharacterEncounterDetail
import tighe.matthew.expanserpgsheet.model.encounter.CharacterEncounterDetailDao

@Database(entities = [Character::class, CharacterEncounterDetail::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun characterEncounterDetailDao(): CharacterEncounterDetailDao
}
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
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun characterEncounterDetailDao(): CharacterEncounterDetailDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context): AppDatabase = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()
    }
}
package tighe.matthew.expanserpgsheet

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import tighe.matthew.expanserpgsheet.characterCreation.CharacterCreationViewModel
import tighe.matthew.expanserpgsheet.characterDetails.CharacterDetailsViewModel
import tighe.matthew.expanserpgsheet.characterList.CharacterListViewModel
import tighe.matthew.expanserpgsheet.encounter.EncounterViewModel
import tighe.matthew.expanserpgsheet.model.AppDatabase
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository
import tighe.matthew.expanserpgsheet.model.encounter.EncounterRepository

val appModule = module {
    single {
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "app-database")
            .build()
    }
    single { get<AppDatabase>().characterDao() }
    single { get<AppDatabase>().characterEncounterDetailDao() }
    single { get<AppDatabase>().characterConditionDao() }
    single { CharacterRepository(get(), get()) }
    single { EncounterRepository(get(), get()) }
    viewModel { CharacterListViewModel(get(), get()) }
    viewModel { CharacterCreationViewModel(get()) }
    viewModel { (characterId: Long) -> CharacterDetailsViewModel(characterId, get()) }
    viewModel { EncounterViewModel(get()) }
}

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@Application)
            modules(appModule)
        }
    }
}
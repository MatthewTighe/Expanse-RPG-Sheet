package tighe.matthew.expanserpgsheet

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import tighe.matthew.expanserpgsheet.characterCreation.CharacterCreationViewModel
import tighe.matthew.expanserpgsheet.repository.CharacterRepository

val appModule = module {
    single { CharacterRepository() }
    viewModel { CharacterCreationViewModel(get()) }
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
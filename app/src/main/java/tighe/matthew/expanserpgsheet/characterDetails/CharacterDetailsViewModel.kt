package tighe.matthew.expanserpgsheet.characterDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import tighe.matthew.expanserpgsheet.BaseViewModel
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.SingleLiveEvent

internal class CharacterDetailsViewModel : ViewModel(), BaseViewModel<CharacterDetailsViewState, CharacterDetailsAction> {
    override fun observeViewState(): LiveData<CharacterDetailsViewState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun observeEvent(): SingleLiveEvent<Event> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun submitAction(action: CharacterDetailsAction) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
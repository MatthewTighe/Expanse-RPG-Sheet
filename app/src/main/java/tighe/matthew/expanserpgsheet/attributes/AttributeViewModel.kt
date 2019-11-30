package tighe.matthew.expanserpgsheet.attributes

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import tighe.matthew.expanserpgsheet.model.character.Attributes

abstract class ObservableViewModel : ViewModel(), Observable {
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class AttributeViewModel : ObservableViewModel() {
    var attributes = Attributes()

    @Bindable
    fun getAccuracy(): Int {
        return attributes.accuracy
    }


}
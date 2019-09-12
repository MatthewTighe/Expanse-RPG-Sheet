package tighe.matthew.expanserpgsheet.model

import android.text.Editable
import android.text.TextWatcher

class AfterTextWatcher(private val action: (String) -> Unit) : TextWatcher {
    override fun afterTextChanged(p0: Editable?) {
        action(p0.toString())
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
}
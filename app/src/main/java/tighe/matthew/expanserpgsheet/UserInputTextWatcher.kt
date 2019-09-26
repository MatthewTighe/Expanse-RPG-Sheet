package tighe.matthew.expanserpgsheet

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText

class UserInputTextWatcher(
    private val editText: EditText,
    private val action: (String) -> Unit
) : TextWatcher {
    companion object {
        const val IGNORE_CHANGE = "ignore"
        const val ACCEPT_CHANGE = "accept"
    }

    override fun afterTextChanged(p0: Editable?) {
        if (editText.tag == IGNORE_CHANGE) return
        action(p0.toString())
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
}

fun TextInputEditText.getWatcher(action: (String) -> Unit): UserInputTextWatcher {
    return UserInputTextWatcher(this, action)
}

@Synchronized fun EditText.setTextBeforeWatching(watcher: TextWatcher, text: String) {
    this.tag = UserInputTextWatcher.IGNORE_CHANGE
    this.removeTextChangedListener(watcher)
    this.setText(text)
    this.setSelection(this.text!!.length)
    this.addTextChangedListener(watcher)
    this.tag = UserInputTextWatcher.ACCEPT_CHANGE
}
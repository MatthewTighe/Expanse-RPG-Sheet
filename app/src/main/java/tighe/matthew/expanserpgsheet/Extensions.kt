package tighe.matthew.expanserpgsheet

import android.app.Activity
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

fun Fragment.navTo(event: Event.Navigate) {
    val bundle = navArgsToBundle(event.navigationArgs)
    this.findNavController().navigate(event.fragment, bundle)
}

fun Activity.shortSnack(@StringRes message: Int) {
    val view = this.findViewById<BottomNavigationView>(R.id.bottom_nav_main)
    val snack = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
    snack.anchorView =view
    snack.show()
}

fun TextInputEditText.onTextFinished(action: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            action(p0.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    })
}

fun SharedPreferences.putInt(key: String, value: Int) {
    with(this.edit()) {
        putInt(key, value)
        apply()
    }
}

fun SharedPreferences.appendStringSet(key: String, value: String) {
    val set = this.getStringSet(key, setOf()) ?: setOf()
    with(this.edit()) {
        putStringSet(key, set.plus(value))
        apply()
    }
}

fun SharedPreferences.removeFromStringSet(key: String, value: String) {
    val set = this.getStringSet(key, setOf()) ?: setOf()
    with(this.edit()) {
        putStringSet(key, set.minus(value))
        apply()
    }
}

fun SharedPreferences.delete(key: String) {
    with(this.edit()) {
        remove(key)
        apply()
    }
}
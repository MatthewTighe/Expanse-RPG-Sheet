package tighe.matthew.expanserpgsheet

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

fun Fragment.navTo(event: Event.Navigate) {
    val bundle = event.navigationArgs.toBundle()
    this.findNavController().navigate(event.fragment, bundle)
}

fun List<NavigationArgument>.toBundle(): Bundle {
    val navArgs = this
    val bundle = bundleOf()
    with(bundle) {
        for (navArg in navArgs) {
            when (navArg.value) {
                is Long -> putLong(navArg.key, navArg.value)
            }
        }
    }
    return bundle
}

fun Activity.shortSnack(@StringRes message: Int) {
    val view = this.findViewById<BottomNavigationView>(R.id.bottom_nav_main)
    val snack = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
    snack.anchorView = view
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

fun String.toIntOrZero(): Int {
    return try {
        this.toInt()
    } catch (err: NullPointerException) {
        0
    }
}
package tighe.matthew.expanserpgsheet

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.navTo(event: Event.Navigate) {
    this.findNavController().navigate(event.fragment, event.bundle)
}
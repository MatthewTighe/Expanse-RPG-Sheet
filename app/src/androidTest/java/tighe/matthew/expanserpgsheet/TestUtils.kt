package tighe.matthew.expanserpgsheet

import androidx.annotation.IdRes
import androidx.navigation.findNavController
import androidx.test.rule.ActivityTestRule

fun ActivityTestRule<MainActivity>.navTo(@IdRes fragmentId: Int, navArgs: List<NavigationArgument> = listOf()) {
    val bundle = navArgs.toBundle()
    this.activity.findNavController(R.id.nav_host_fragment).navigate(fragmentId, bundle)
}

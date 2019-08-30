package tighe.matthew.expanserpgsheet.model

import android.os.Parcelable
import tighe.matthew.expanserpgsheet.NavigationArgument

interface DatabaseEntity<M: Model> {
    fun constructModel(): M
}

interface Model : Parcelable {
    val bundleKey: String
    fun buildNavArg(): NavigationArgument {
        return NavigationArgument(bundleKey, this)
    }
}
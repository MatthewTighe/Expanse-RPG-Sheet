package tighe.matthew.expanserpgsheet.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterModel(
    val name: String = "",
    val maxFortune: Int = 0
) : Model, Parcelable {
    companion object {
        val bundleKey = "character"
    }
}
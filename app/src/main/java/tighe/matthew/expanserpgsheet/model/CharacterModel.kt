package tighe.matthew.expanserpgsheet.model

import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterModel(
    val name: String = "",
    val maxFortune: Int = 0
) : Model {
    companion object {
        val bundleKey = "character"
    }
}
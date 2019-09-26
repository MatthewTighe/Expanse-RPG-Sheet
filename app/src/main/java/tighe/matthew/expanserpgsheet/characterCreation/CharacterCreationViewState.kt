package tighe.matthew.expanserpgsheet.characterCreation

import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.TextInputFieldError
import tighe.matthew.expanserpgsheet.ViewState

internal data class CharacterCreationViewState(
    val nameError: NameError = NameError()
) : ViewState

internal data class NameError(
    override val errorEnabled: Boolean = false,
    override val fieldName: Int = R.string.name
) : TextInputFieldError
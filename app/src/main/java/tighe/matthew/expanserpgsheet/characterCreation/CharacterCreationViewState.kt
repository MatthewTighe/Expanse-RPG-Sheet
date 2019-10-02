package tighe.matthew.expanserpgsheet.characterCreation

import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.TextInputFieldError
import tighe.matthew.expanserpgsheet.ViewState
import tighe.matthew.expanserpgsheet.controller.AttributeError

internal data class CharacterCreationViewState(
    val nameError: NameError = NameError(),
    val attributeErrors: List<AttributeError> = listOf()
) : ViewState

internal data class NameError(
    override val errorEnabled: Boolean = false,
    override val fieldName: Int = R.string.name
) : TextInputFieldError
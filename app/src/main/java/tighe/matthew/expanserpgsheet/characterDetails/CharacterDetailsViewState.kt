package tighe.matthew.expanserpgsheet.characterDetails

import tighe.matthew.expanserpgsheet.ViewState
import tighe.matthew.expanserpgsheet.attributes.AttributeError
import tighe.matthew.expanserpgsheet.model.character.Character

data class CharacterDetailsViewState(
    val character: Character,
    val attributeErrors: List<AttributeError> = listOf()
) : ViewState
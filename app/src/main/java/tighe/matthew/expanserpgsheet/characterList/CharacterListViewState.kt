package tighe.matthew.expanserpgsheet.characterList

import tighe.matthew.expanserpgsheet.ViewState
import tighe.matthew.expanserpgsheet.model.character.Character

internal data class CharacterListViewState(
    val characterList: List<Character> = emptyList(),
    val initiativeDialogShouldBeDisplayed: Boolean = false,
    val characterBeingManipulated: Character? = null
) : ViewState
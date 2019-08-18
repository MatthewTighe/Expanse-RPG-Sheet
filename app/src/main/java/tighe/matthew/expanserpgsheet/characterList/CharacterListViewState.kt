package tighe.matthew.expanserpgsheet.characterList

import tighe.matthew.expanserpgsheet.ViewState
import tighe.matthew.expanserpgsheet.model.CharacterModel

internal data class CharacterListViewState(
    val loading: Boolean = false,
    val characterList: List<CharacterModel> = emptyList()
): ViewState
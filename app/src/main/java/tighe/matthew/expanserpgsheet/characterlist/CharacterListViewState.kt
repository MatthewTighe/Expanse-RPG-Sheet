package tighe.matthew.expanserpgsheet.characterlist

import tighe.matthew.expanserpgsheet.ViewState
import tighe.matthew.expanserpgsheet.ViewStateEvent

data class CharacterListViewState(
    val loading: Boolean = false,
    val data: List<String> = emptyList(),
    val error: ViewStateEvent? = null
) : ViewState
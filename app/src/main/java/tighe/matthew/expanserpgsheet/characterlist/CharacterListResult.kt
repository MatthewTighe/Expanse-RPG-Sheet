package tighe.matthew.expanserpgsheet.characterlist

import androidx.annotation.IdRes

sealed class CharacterListResult {
    object Loading : CharacterListResult()
    data class Error(@IdRes val errorMessage: Int) : CharacterListResult()
    // TODO Model characters
    data class Success(val characterList: List<String>) : CharacterListResult()
}
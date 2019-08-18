package tighe.matthew.expanserpgsheet.characterCreation

import tighe.matthew.expanserpgsheet.Action

internal sealed class CharacterCreationAction : Action {
    data class NameInput(val name: String) : CharacterCreationAction()
    data class MaxFortuneInput(val fortune: Int) : CharacterCreationAction()
    object Save : CharacterCreationAction()
}
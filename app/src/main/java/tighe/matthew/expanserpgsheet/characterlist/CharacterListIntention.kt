package tighe.matthew.expanserpgsheet.characterlist

import tighe.matthew.expanserpgsheet.Intention

sealed class CharacterListIntention : Intention<CharacterListAction> {
    object UserClickedAdd : CharacterListIntention()

    override fun mapToAction(): CharacterListAction = when (this) {
        UserClickedAdd -> CharacterListAction.Add
    }
}
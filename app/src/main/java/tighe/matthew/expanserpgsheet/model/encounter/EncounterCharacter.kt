package tighe.matthew.expanserpgsheet.model.encounter

import tighe.matthew.expanserpgsheet.model.character.Character

data class EncounterCharacter(
    val character: Character,
    val detail: CharacterEncounterDetail
)
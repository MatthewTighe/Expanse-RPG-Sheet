package tighe.matthew.expanserpgsheet

import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.encounter.EncounterDetail
import tighe.matthew.expanserpgsheet.model.encounter.EncounterCharacter

fun generateEncounterCharacter(position: Int): EncounterCharacter {
    val charId = 1L
    val char = Character(charId, "name")
    val detail = EncounterDetail(position, 0, charId)
    return EncounterCharacter(char, detail)
}
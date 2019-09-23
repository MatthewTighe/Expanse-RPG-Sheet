package tighe.matthew.expanserpgsheet.encounter

import tighe.matthew.expanserpgsheet.ViewState
import tighe.matthew.expanserpgsheet.model.encounter.EncounterCharacter

data class EncounterViewState(val encounterCharacters: List<EncounterCharacter>) : ViewState
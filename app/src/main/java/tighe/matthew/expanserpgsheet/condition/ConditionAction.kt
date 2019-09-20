package tighe.matthew.expanserpgsheet.condition

import com.google.android.material.chip.Chip
import tighe.matthew.expanserpgsheet.Action

sealed class ConditionAction : Action {
    data class chipClicked(val chip: Chip) : ConditionAction()
}
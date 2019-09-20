package tighe.matthew.expanserpgsheet.condition

import com.google.android.material.chip.Chip
import tighe.matthew.expanserpgsheet.ViewState

data class ConditionViewState(
    val checkedChips: List<Chip>
) : ViewState
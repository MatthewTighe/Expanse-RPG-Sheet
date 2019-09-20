package tighe.matthew.expanserpgsheet.condition

import android.view.View
import android.widget.LinearLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.layout_collapsible_conditions.view.*
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.condition.Condition

class ConditionViewController(
    private val layout: LinearLayout,
    private val viewModel: ConditionViewModel
) {
    private val btnCollapse: MaterialButton = layout.findViewById(R.id.btn_conditions_collapsible)
    private val chipGroup: ChipGroup = layout.findViewById(R.id.chip_group_conditions)
    private val conditionChips = mapOf<Condition, Chip>(
        Condition.Injured to layout.findViewById(R.id.chip_injured),
        Condition.Wounded to layout.findViewById(R.id.chip_wounded),
        Condition.TakenOut to layout.findViewById(R.id.chip_taken_out)
    )

    private var expanded = true

    init {
        btnCollapse.setOnClickListener {
            handleBtnClick()
        }

//        for ((condition, chip) in conditionChips) {
//            setChipState(character, condition, chip)
//            setChipListener(condition, chip)
//        }
    }

    private fun handleBtnClick() {
        if (expanded) {
            chipGroup.visibility = View.GONE
            btnCollapse.setIconResource(R.drawable.ic_expand_more_24dp)
            expanded = false
        } else {
            chipGroup.visibility = View.VISIBLE
            btnCollapse.setIconResource(R.drawable.ic_expand_less_24dp)
            expanded = true
        }
    }

    private fun setChipState(character: Character, condition: Condition, chip: Chip) {
        chip.isChecked = character.conditions.contains(condition)
        if (chip.isChecked) {
            chip.setChipBackgroundColorResource(R.color.colorSecondary)
        } else {
            chip.setChipBackgroundColorResource(R.color.colorPrimary)
        }
    }

    private fun setChipListener(condition: Condition, chip: Chip) {
//        chip.setOnClickListener {
//            val updatedConditions = if (chip.isChecked) {
//                character.conditions.plus(condition)
//            } else {
//                character.conditions.minus(condition)
//            }
//            val updatedCharacter = character.copy(conditions = updatedConditions)
//            GlobalScope.launch { characterRepository.update(updatedCharacter) }
//        }
    }
}
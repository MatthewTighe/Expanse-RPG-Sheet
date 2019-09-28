package tighe.matthew.expanserpgsheet.attributes

import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.getWatcher
import tighe.matthew.expanserpgsheet.model.character.AttributeType
import tighe.matthew.expanserpgsheet.model.character.Attributes
import tighe.matthew.expanserpgsheet.setTextBeforeWatching

class AttributesView(
    private val layout: ConstraintLayout,
    private val current: Attributes = UNFILLED_ATTRIBUTES,
    private val viewModel: AttributesViewModel
) {

    companion object {
        const val UNFILLED_ATTRIBUTE = Int.MIN_VALUE
        val UNFILLED_ATTRIBUTES = Attributes(
            UNFILLED_ATTRIBUTE,
            UNFILLED_ATTRIBUTE,
            UNFILLED_ATTRIBUTE,
            UNFILLED_ATTRIBUTE,
            UNFILLED_ATTRIBUTE,
            UNFILLED_ATTRIBUTE,
            UNFILLED_ATTRIBUTE,
            UNFILLED_ATTRIBUTE,
            UNFILLED_ATTRIBUTE
        )
    }

    init {
        setupFields()
    }

    private fun setupFields() {
        for (attribute in current) {
            val view = getView(attribute.type)
            val action = getAction(attribute.type)
            val watcher = view.getWatcher(action)
            if (attribute.value == UNFILLED_ATTRIBUTE) {
                view.addTextChangedListener(watcher)
            } else {
                view.setTextBeforeWatching(watcher, attribute.value.toString())
            }
        }
    }

    private fun getView(type: AttributeType): TextInputEditText {
        val id = when (type) {
            AttributeType.ACCURACY -> R.id.input_accuracy
            AttributeType.COMMUNICATION -> R.id.input_communication
            AttributeType.CONSTITUTION -> R.id.input_constitution
            AttributeType.DEXTERITY -> R.id.input_dexterity
            AttributeType.FIGHTING -> R.id.input_fighting
            AttributeType.INTELLIGENCE -> R.id.input_intelligence
            AttributeType.PERCEPTION -> R.id.input_perception
            AttributeType.STRENGTH -> R.id.input_strength
            AttributeType.WILLPOWER -> R.id.input_willpower
        }
        return layout.findViewById(id)!!
    }

    private fun getAction(type: AttributeType): (String) -> Unit {
        return { input -> viewModel.submitAction(AttributesAction.AttributeInput(type, input)) }
    }

}
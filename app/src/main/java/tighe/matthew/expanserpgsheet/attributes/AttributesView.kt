package tighe.matthew.expanserpgsheet.attributes

import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.getWatcher
import tighe.matthew.expanserpgsheet.model.character.AttributeType
import tighe.matthew.expanserpgsheet.model.character.Attributes
import tighe.matthew.expanserpgsheet.setTextBeforeWatching

class AttributesView(
    private val layout: ConstraintLayout,
    private val viewModel: AttributesViewModel,
    initialAttributes: Attributes = Attributes.UNFILLED_ATTRIBUTES,
    initialErrors: List<AttributeError> = listOf()
) {

    init {
        setupFields(initialAttributes)
        displayErrors(initialErrors)
    }

    fun setAttributes(attributes: Attributes) {
        setupFields(attributes)
    }

    fun setErrors(errors: List<AttributeError>) {
        displayErrors(errors)
    }

    private fun setupFields(attributes: Attributes) {
        for (attribute in attributes) {
            val view = getTextView(attribute.type)
            val action = getAction(attribute.type)
            val watcher = view.getWatcher(action)
            if (attribute.value == Attributes.UNFILLED_ATTRIBUTE) {
                view.addTextChangedListener(watcher)
            } else {
                view.setTextBeforeWatching(watcher, attribute.value.toString())
            }
        }
    }

    private fun displayErrors(errors: List<AttributeError>) {
        for (error in errors) {
            val view = getTextLayout(error.type)
            error.handleDisplay(view)
        }
    }

    private fun getTextView(type: AttributeType): TextInputEditText {
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

    private fun getTextLayout(type: AttributeType): TextInputLayout {
        val id = when (type) {
            AttributeType.ACCURACY -> R.id.layout_input_accuracy
            AttributeType.COMMUNICATION -> R.id.layout_input_communication
            AttributeType.CONSTITUTION -> R.id.layout_input_constitution
            AttributeType.DEXTERITY -> R.id.layout_input_dexterity
            AttributeType.FIGHTING -> R.id.layout_input_fighting
            AttributeType.INTELLIGENCE -> R.id.layout_input_intelligence
            AttributeType.PERCEPTION -> R.id.layout_input_perception
            AttributeType.STRENGTH -> R.id.layout_input_strength
            AttributeType.WILLPOWER -> R.id.layout_input_willpower
        }
        return layout.findViewById(id)!!
    }

    private fun getAction(type: AttributeType): (String) -> Unit {
        return { input -> viewModel.submitAction(AttributesAction.AttributeInput(type, input)) }
    }
}
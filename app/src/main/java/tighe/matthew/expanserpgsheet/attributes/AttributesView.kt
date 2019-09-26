package tighe.matthew.expanserpgsheet.attributes

import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.getWatcher
import tighe.matthew.expanserpgsheet.model.character.Attributes
import tighe.matthew.expanserpgsheet.setTextBeforeWatching

class AttributesView(
    private val layout: ConstraintLayout,
    private val current: Attributes = Attributes(),
    private val viewModel: AttributesViewModel
) {

    init {
        setupAccuracyField()
        setupCommunicationField()
        setupConstitutionField()
        setupDexterityField()
        setupFightingField()
        setupIntelligenceField()
        setupPerceptionField()
        setupStrengthField()
        setupWillpowerField()
    }

    private fun setupAccuracyField() {
        val accuracyInput = layout.findViewById<TextInputEditText>(R.id.input_accuracy)
        val watcher = accuracyInput.getWatcher {
            viewModel.submitAction(AttributesAction.AccuracyInput(it))
        }

        accuracyInput.setTextBeforeWatching(watcher, current.accuracy.toString())
    }

    private fun setupCommunicationField() {
        val communicationInput = layout.findViewById<TextInputEditText>(R.id.input_communication)
        val watcher = communicationInput.getWatcher {
            viewModel.submitAction(AttributesAction.CommunicationInput(it))
        }

        communicationInput.setTextBeforeWatching(watcher, current.communication.toString())
    }

    private fun setupConstitutionField() {
        val constitutionInput = layout.findViewById<TextInputEditText>(R.id.input_constitution)
        val watcher = constitutionInput.getWatcher {
            viewModel.submitAction(AttributesAction.ConstitutionInput(it))
        }

        constitutionInput.setTextBeforeWatching(watcher, current.constitution.toString())
    }

    private fun setupDexterityField() {
        val dexterityInput = layout.findViewById<TextInputEditText>(R.id.input_dexterity)
        val watcher = dexterityInput.getWatcher {
            viewModel.submitAction(AttributesAction.DexterityInput(it))
        }

        dexterityInput.setTextBeforeWatching(watcher, current.dexterity.toString())
    }

    private fun setupFightingField() {
        val fightingInput = layout.findViewById<TextInputEditText>(R.id.input_fighting)
        val watcher = fightingInput.getWatcher {
            viewModel.submitAction(AttributesAction.FightingInput(it))
        }

        fightingInput.setTextBeforeWatching(watcher, current.fighting.toString())
    }

    private fun setupIntelligenceField() {
        val intelligenceInput = layout.findViewById<TextInputEditText>(R.id.input_intelligence)
        val watcher = intelligenceInput.getWatcher {
            viewModel.submitAction(AttributesAction.IntelligenceInput(it))
        }

        intelligenceInput.setTextBeforeWatching(watcher, current.intelligence.toString())
    }

    private fun setupPerceptionField() {
        val perceptionInput = layout.findViewById<TextInputEditText>(R.id.input_perception)
        val watcher = perceptionInput.getWatcher {
            viewModel.submitAction(AttributesAction.PerceptionInput(it))
        }

        perceptionInput.setTextBeforeWatching(watcher, current.perception.toString())
    }

    private fun setupStrengthField() {
        val strengthInput = layout.findViewById<TextInputEditText>(R.id.input_strength)
        val watcher = strengthInput.getWatcher {
            viewModel.submitAction(AttributesAction.StrengthInput(it))
        }

        strengthInput.setTextBeforeWatching(watcher, current.strength.toString())
    }

    private fun setupWillpowerField() {
        val willpowerInput = layout.findViewById<TextInputEditText>(R.id.input_willpower)
        val watcher = willpowerInput.getWatcher {
            viewModel.submitAction(AttributesAction.WillpowerInput(it))
        }

        willpowerInput.setTextBeforeWatching(watcher, current.willpower.toString())
    }
}
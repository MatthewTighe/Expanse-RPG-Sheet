package tighe.matthew.expanserpgsheet.characterDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.UserInputTextWatcher
import tighe.matthew.expanserpgsheet.armor.ArmorDropdown
import tighe.matthew.expanserpgsheet.attributes.AttributeInput
import tighe.matthew.expanserpgsheet.attributes.AttributesView
import tighe.matthew.expanserpgsheet.condition.ConditionView
import tighe.matthew.expanserpgsheet.model.character.Armor
import tighe.matthew.expanserpgsheet.model.condition.Condition
import tighe.matthew.expanserpgsheet.setTextBeforeWatching

class CharacterDetailsFragment : Fragment() {

    private val characterId: Long by lazy { arguments!!.getLong("characterId") }

    private val viewModel: CharacterDetailsViewModel by viewModel { parametersOf(characterId) }

    private val maxFortuneEditText by lazy {
        activity?.findViewById<TextInputEditText>(R.id.details_input_max_fortune)
    }
    private val maxFortuneWatcher by lazy {
        UserInputTextWatcher(maxFortuneEditText!!) { text ->
            viewModel.submitAction(CharacterDetailsAction.MaxFortuneChanged(text.toInt()))
        }
    }

    private val currentFortuneEditText by lazy {
        activity?.findViewById<TextInputEditText>(R.id.details_input_current_fortune)
    }
    private val currentFortuneWatcher by lazy {
        UserInputTextWatcher(currentFortuneEditText!!) { text ->
            viewModel.submitAction(CharacterDetailsAction.CurrentFortuneChanged(text.toInt()))
        }
    }

    private val attributeInputHandler: (AttributeInput) -> Unit = {
        viewModel.submitAction(CharacterDetailsAction.AttributeChanged(it))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_character_details, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.observeViewState().observe(this, Observer { it?.let { viewState ->
            handleTextViews(viewState)
            handleFortuneViews(viewState)
            handleConditionView(viewState)
            handleAttributesView(viewState)
            handleArmorView(viewState)
        } })

        setupFortuneListeners()
    }

    private fun handleTextViews(viewState: CharacterDetailsViewState) {
        val textName = activity?.findViewById<TextView>(R.id.details_text_character_name)
        val textDefense = activity?.findViewById<TextView>(R.id.details_text_defense)
        val textToughness = activity?.findViewById<TextView>(R.id.details_text_toughness)
        textName?.text = viewState.character.name
        textDefense?.text = getString(R.string.defense_interpolated, viewState.character.defense)
        textToughness?.text = getString(R.string.toughness_interpolated, viewState.character.toughness)
    }

    private fun handleFortuneViews(viewState: CharacterDetailsViewState) {
        val maxFortuneInput = activity?.findViewById<TextInputEditText>(R.id.details_input_max_fortune)
        val currentFortuneInput = activity?.findViewById<TextInputEditText>(R.id.details_input_current_fortune)

        maxFortuneInput?.setTextBeforeWatching(maxFortuneWatcher, viewState.character.maxFortune.toString())
        currentFortuneInput?.setTextBeforeWatching(currentFortuneWatcher, viewState.character.currentFortune.toString())
    }

    private fun setupFortuneListeners() {
        val maxFortuneInput = activity?.findViewById<TextInputEditText>(R.id.details_input_max_fortune)
        val currentFortuneInput = activity?.findViewById<TextInputEditText>(R.id.details_input_current_fortune)

        maxFortuneInput?.addTextChangedListener(maxFortuneWatcher)

        currentFortuneInput?.addTextChangedListener(currentFortuneWatcher)
    }

    private fun handleArmorView(viewState: CharacterDetailsViewState) {
        val armorLayout = activity?.findViewById<TextInputLayout>(R.id.details_layout_armor_dropdown)!!
        val listener: (Armor) -> Unit = { armor ->
            viewModel.submitAction(CharacterDetailsAction.ArmorChanged(armor))
        }

        ArmorDropdown(armorLayout, activity!!, listener, viewState.character.armor)
    }

    private fun handleAttributesView(viewState: CharacterDetailsViewState) {
        val attributesLayout = activity?.findViewById<ConstraintLayout>(R.id.layout_details_attributes)!!
        AttributesView(
            attributesLayout,
            attributeInputHandler,
            viewState.character.attributes,
            viewState.attributeErrors
        )
    }

    private fun handleConditionView(viewState: CharacterDetailsViewState) {
        val conditionLayout = activity?.findViewById<LinearLayout>(R.id.details_layout_conditions)!!
        val character = viewState.character
        val conditions = viewState.character.conditions
        val onConditionChecked = { condition: Condition ->
            viewModel.submitAction(CharacterDetailsAction.ConditionChecked(condition, character))
        }
        val onConditionUnchecked = { condition: Condition ->
            viewModel.submitAction(CharacterDetailsAction.ConditionUnchecked(condition, character))
        }

        ConditionView(
            conditionLayout,
            conditions,
            onConditionChecked,
            onConditionUnchecked
        )
    }
}
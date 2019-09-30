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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.UserInputTextWatcher
import tighe.matthew.expanserpgsheet.attributes.AttributesView
import tighe.matthew.expanserpgsheet.attributes.AttributesViewModel
import tighe.matthew.expanserpgsheet.controller.ConditionView
import tighe.matthew.expanserpgsheet.model.condition.Condition
import tighe.matthew.expanserpgsheet.setTextBeforeWatching

class CharacterDetailsFragment : Fragment() {

    private val characterId: Long by lazy { arguments!!.getLong("characterId") }

    private val baseViewModel: CharacterDetailsViewModel by viewModel { parametersOf(characterId) }
    private val attributesViewModel: AttributesViewModel by viewModel()

    private val maxFortuneEditText by lazy {
        activity?.findViewById<TextInputEditText>(R.id.details_input_max_fortune)
    }
    private val maxFortuneWatcher by lazy {
        UserInputTextWatcher(maxFortuneEditText!!) { text ->
            baseViewModel.submitAction(CharacterDetailsAction.ChangeMaxFortune(text.toInt()))
        }
    }

    private val currentFortuneEditText by lazy {
        activity?.findViewById<TextInputEditText>(R.id.details_input_current_fortune)
    }
    private val currentFortuneWatcher by lazy {
        UserInputTextWatcher(currentFortuneEditText!!) { text ->
            baseViewModel.submitAction(CharacterDetailsAction.ChangeCurrentFortune(text.toInt()))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_character_details, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val attributesLayout = activity?.findViewById<ConstraintLayout>(R.id.layout_details_attributes)!!
        val attributesView = AttributesView(attributesLayout, attributesViewModel)
        val textName = activity?.findViewById<TextView>(R.id.details_text_character_name)
        baseViewModel.observeViewState().observe(this, Observer { it?.let { viewState ->
            textName?.text = viewState.character.name
            handleFortuneViews(viewState)
            setupConditionView(viewState)
            attributesView.setAttributes(viewState.character.attributes)
        } })

        attributesViewModel.observeViewState().observe(this, Observer { it?.let { viewState ->
            baseViewModel.submitAction(CharacterDetailsAction.UpdateAttributes(viewState.attributes))
            attributesView.setErrors(viewState.errors)
        } })


        setupFortuneListeners()
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

    private fun setupConditionView(viewState: CharacterDetailsViewState) {
        val conditionLayout = activity?.findViewById<LinearLayout>(R.id.details_layout_conditions)!!
        val character = viewState.character
        val conditions = viewState.character.conditions
        val onConditionChecked = { condition: Condition ->
            baseViewModel.submitAction(CharacterDetailsAction.ConditionChecked(condition, character))
        }
        val onConditionUnchecked = { condition: Condition ->
            baseViewModel.submitAction(CharacterDetailsAction.ConditionUnchecked(condition, character))
        }

        ConditionView(conditionLayout, conditions, onConditionChecked, onConditionUnchecked)
    }
}
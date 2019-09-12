package tighe.matthew.expanserpgsheet.characterDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.AfterTextWatcher
import tighe.matthew.expanserpgsheet.setTextWithoutWatcher

class CharacterDetailsFragment : Fragment() {

    private val characterId: Long by lazy { arguments!!.getLong("characterId") }

    private val viewModel: CharacterDetailsViewModel by viewModel { parametersOf(characterId) }

    private val maxFortuneWatcher = AfterTextWatcher { text ->
        viewModel.submitAction(CharacterDetailsAction.ChangeMaxFortune(text.toInt()))
    }

    private val currentFortuneWatcher = AfterTextWatcher { text ->
        viewModel.submitAction(CharacterDetailsAction.ChangeCurrentFortune(text.toInt()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_character_details, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val textName = activity?.findViewById<TextView>(R.id.details_text_character_name)
        viewModel.observeViewState().observe(this, Observer { it?.let { viewState ->
            textName?.text = viewState.character.name
            handleFortuneViews(viewState)
        } })

        setupFortuneListeners()
    }

    private fun handleFortuneViews(viewState: CharacterDetailsViewState) {
        val maxFortuneInput = activity?.findViewById<TextInputEditText>(R.id.details_input_max_fortune)
        val currentFortuneInput = activity?.findViewById<TextInputEditText>(R.id.details_input_current_fortune)

        maxFortuneInput?.setTextWithoutWatcher(maxFortuneWatcher, viewState.character.maxFortune.toString())
        currentFortuneInput?.setTextWithoutWatcher(currentFortuneWatcher, viewState.character.currentFortune.toString())
    }

    private fun setupFortuneListeners() {
        val maxFortuneInput = activity?.findViewById<TextInputEditText>(R.id.details_input_max_fortune)
        val currentFortuneInput = activity?.findViewById<TextInputEditText>(R.id.details_input_current_fortune)

        maxFortuneInput?.addTextChangedListener(maxFortuneWatcher)

        currentFortuneInput?.addTextChangedListener(currentFortuneWatcher)
    }
}
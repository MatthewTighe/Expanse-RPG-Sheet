package tighe.matthew.expanserpgsheet.characterDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.model.character.Character

class CharacterDetailsFragment : Fragment() {

    private val characterId: Long by lazy { arguments!!.getLong("characterId") }

    private val viewModel: CharacterDetailsViewModel by viewModel { parametersOf(characterId) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_character_details, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val textName = activity?.findViewById<TextView>(R.id.details_text_character_name)
        viewModel.observeViewState().observe(this, Observer { it?.let { viewState ->
            textName?.text = viewState.character.name
            handleFortune(viewState)
        } })
    }

    private fun handleFortune(viewState: CharacterDetailsViewState) {
        val maxFortuneInput = activity?.findViewById<TextInputEditText>(R.id.details_input_max_fortune)
        val currentFortune = activity?.findViewById<TextInputEditText>(R.id.details_input_current_fortune)

        maxFortuneInput?.setText(viewState.character.maxFortune.toString())
        currentFortune?.setText(viewState.character.currentFortune.toString())
    }
}
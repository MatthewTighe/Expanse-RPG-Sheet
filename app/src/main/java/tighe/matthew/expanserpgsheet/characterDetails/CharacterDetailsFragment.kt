package tighe.matthew.expanserpgsheet.characterDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
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

        val labelFortune = activity?.findViewById<TextView>(R.id.label_fortune_adjustment)
        labelFortune?.text = getString(R.string.hint_max_fortune)

        val textName = activity?.findViewById<TextView>(R.id.text_character_name_details)
        val textFortune = activity?.findViewById<TextView>(R.id.edit_text_fortune)
        viewModel.observeViewState().observe(this, Observer { it?.let { viewState ->
            textName?.text = viewState.character.name
            textFortune?.text = viewState.character.maxFortune.toString()
        } })

        val incBtn = activity?.findViewById<TextView>(R.id.btn_inc_fortune)!!
        incBtn.setOnClickListener {
            viewModel.submitAction(CharacterDetailsAction.IncrementFortune)
        }

        val decBtn = activity?.findViewById<TextView>(R.id.btn_dec_fortune)!!
        decBtn.setOnClickListener {
            viewModel.submitAction(CharacterDetailsAction.DecrementFortune)
        }
    }
}
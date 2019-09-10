package tighe.matthew.expanserpgsheet.characterDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import org.koin.android.viewmodel.ext.android.viewModel
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.model.character.Character

class CharacterDetailsFragment : Fragment() {
    private val viewModel: CharacterDetailsViewModel by viewModel()
    private val character: Character by lazy {
        val args: CharacterDetailsFragmentArgs by navArgs()
        args.character!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_character_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val textName = activity?.findViewById<TextView>(R.id.text_character_name_details)
        textName?.text = character.name

        val labelFortune = activity?.findViewById<TextView>(R.id.label_fortune_adjustment)
        labelFortune?.text = getString(R.string.hint_max_fortune)

        val textFortune = activity?.findViewById<TextView>(R.id.edit_text_fortune)
        viewModel.observeViewState().observe(this, Observer { it?.let { viewState ->
            textFortune?.text = viewState.maxFortune.toString()
        } })

        val incBtn = activity?.findViewById<TextView>(R.id.btn_inc_fortune)!!
        incBtn.setOnClickListener {
            viewModel.submitAction(CharacterDetailsAction.IncrementFortune(1))
        }

        val decBtn = activity?.findViewById<TextView>(R.id.btn_dec_fortune)!!
        decBtn.setOnClickListener {
            viewModel.submitAction(CharacterDetailsAction.DecrementFortune(1))
        }

        viewModel.submitAction(CharacterDetailsAction.CharacterReceived(character))
    }
}
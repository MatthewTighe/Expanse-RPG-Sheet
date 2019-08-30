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

        val textFortune = activity?.findViewById<TextView>(R.id.text_fortune)
        viewModel.observeViewState().observe(this, Observer { it?.let { viewState ->
            textFortune?.text = viewState.fortune.toString()
        } })

        val inc1 = activity?.findViewById<TextView>(R.id.text_increment_one)!!
        inc1.setOnClickListener { viewModel.submitAction(CharacterDetailsAction.IncrementFortune(1)) }

        val inc5 = activity?.findViewById<TextView>(R.id.text_increment_five)!!
        inc5.setOnClickListener { viewModel.submitAction(CharacterDetailsAction.IncrementFortune(5)) }

        val dec1 = activity?.findViewById<TextView>(R.id.text_decrement_one)!!
        dec1.setOnClickListener { viewModel.submitAction(CharacterDetailsAction.DecrementFortune(1)) }

        val dec5 = activity?.findViewById<TextView>(R.id.text_decrement_five)!!
        dec5.setOnClickListener { viewModel.submitAction(CharacterDetailsAction.DecrementFortune(5)) }

        viewModel.submitAction(CharacterDetailsAction.CharacterReceived(character))
    }
}
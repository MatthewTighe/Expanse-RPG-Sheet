package tighe.matthew.expanserpgsheet.characterCreation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.onTextFinished

class CharacterCreationFragment : Fragment() {
    private val viewModel = ViewModelProvider.NewInstanceFactory().create(CharacterCreationViewModel::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_charaction_creation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val nameInput = activity?.findViewById<TextInputEditText>(R.id.input_name)!!
        nameInput.onTextFinished { name ->
            viewModel.submitAction(CharacterCreationAction.NameInput(name))
        }

        val maxFortuneInput = activity?.findViewById<TextInputEditText>(R.id.input_max_fortune)!!
        maxFortuneInput.onTextFinished { maxFortuneInput ->
            viewModel.submitAction(CharacterCreationAction.MaxFortuneInput(maxFortuneInput.toInt()))
        }

        val saveBtn = activity?.findViewById<FloatingActionButton>(R.id.btn_save)!!
        saveBtn.setOnClickListener { viewModel.submitAction(CharacterCreationAction.Save) }
    }
}

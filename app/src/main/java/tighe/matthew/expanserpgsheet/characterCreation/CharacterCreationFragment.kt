package tighe.matthew.expanserpgsheet.characterCreation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.viewmodel.ext.android.viewModel
import tighe.matthew.expanserpgsheet.*
import tighe.matthew.expanserpgsheet.armor.ArmorDropdown
import tighe.matthew.expanserpgsheet.attributes.AttributeInput
import tighe.matthew.expanserpgsheet.attributes.AttributesView
import tighe.matthew.expanserpgsheet.model.character.Armor

class CharacterCreationFragment : Fragment() {
    private val viewModel: CharacterCreationViewModel by viewModel()

    private val onAttributeInput: (AttributeInput) -> Unit = {
        viewModel.submitAction(CharacterCreationAction.AttributeChanged(it))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_character_creation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.observeEvent().observe(this, Observer { it?.let { event ->
            return@let when (event) {
                is Event.Navigate -> { navTo(event) }
                is Event.Snackbar -> { activity?.shortSnack(event.message) }
            }
        } })

        viewModel.observeViewState().observe(this, Observer { it?.let { viewState ->
            handleViewState(viewState)
        } })

        val nameInput = activity?.findViewById<TextInputEditText>(R.id.input_name)!!
        nameInput.onTextFinished { name ->
            viewModel.submitAction(CharacterCreationAction.NameChanged(name))
        }

        val maxFortuneInput = activity?.findViewById<TextInputEditText>(R.id.input_max_fortune)!!
        maxFortuneInput.onTextFinished { maxFortune ->
            viewModel.submitAction(CharacterCreationAction.MaxFortuneChanged(maxFortune))
        }

        val saveBtn = activity?.findViewById<FloatingActionButton>(R.id.btn_save)!!
        saveBtn.setOnClickListener { viewModel.submitAction(CharacterCreationAction.Save) }
    }

    private fun handleViewState(viewState: CharacterCreationViewState) {
        val nameView = activity?.findViewById<TextInputLayout>(R.id.layout_input_name)
        viewState.nameError.handleDisplay(nameView)

        val attributeLayout = activity?.findViewById<ConstraintLayout>(R.id.layout_creation_attributes)!!
        AttributesView(
            attributeLayout,
            onAttributeInput,
            errors = viewState.attributeErrors
        )

        val armorLayout = activity?.findViewById<TextInputLayout>(R.id.creation_layout_armor_dropdown)!!
        val armorSelectionListener: (Armor) -> Unit = { armor ->
            Toast.makeText(activity, armor.toString(), Toast.LENGTH_LONG).show()
        }
        ArmorDropdown(armorLayout, activity!!, armorSelectionListener)
    }
}

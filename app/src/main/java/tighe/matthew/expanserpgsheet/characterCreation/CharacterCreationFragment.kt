package tighe.matthew.expanserpgsheet.characterCreation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.viewmodel.ext.android.viewModel
import tighe.matthew.expanserpgsheet.*
import tighe.matthew.expanserpgsheet.attributes.AttributesView
import tighe.matthew.expanserpgsheet.attributes.AttributesViewModel
import tighe.matthew.expanserpgsheet.attributes.AttributesViewState

class CharacterCreationFragment : Fragment() {
    private val baseViewModel: CharacterCreationViewModel by viewModel()
    private val attributesViewModel: AttributesViewModel by viewModel()

    private val attributesLayout by lazy {
        activity?.findViewById<ConstraintLayout>(R.id.layout_creation_attributes)!!
    }
    private val attributesView by lazy { AttributesView(attributesLayout, attributesViewModel) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_character_creation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        baseViewModel.observeEvent().observe(this, Observer { it?.let { event ->
            return@let when (event) {
                is Event.Navigate -> { navTo(event) }
                is Event.Snackbar -> { activity?.shortSnack(event.message) }
            }
        } })

        baseViewModel.observeViewState().observe(this, Observer { it?.let { viewState ->
            handleBaseViewState(viewState)
        } })

        attributesViewModel.observeViewState().observe(this, Observer { it?.let { viewState ->
            handleAttributesViewState(viewState)
        } })

        val nameInput = activity?.findViewById<TextInputEditText>(R.id.input_name)!!
        nameInput.onTextFinished { name ->
            baseViewModel.submitAction(CharacterCreationAction.NameInput(name))
        }

        val maxFortuneInput = activity?.findViewById<TextInputEditText>(R.id.input_max_fortune)!!
        maxFortuneInput.onTextFinished { maxFortune ->
            baseViewModel.submitAction(CharacterCreationAction.MaxFortuneInput(maxFortune))
        }

        val saveBtn = activity?.findViewById<FloatingActionButton>(R.id.btn_save)!!
        saveBtn.setOnClickListener { baseViewModel.submitAction(CharacterCreationAction.Save) }
    }

    private fun handleBaseViewState(viewState: CharacterCreationViewState) {
        val nameView = activity?.findViewById<TextInputLayout>(R.id.layout_input_name)
        viewState.nameError.handleDisplay(nameView)
        attributesView.setErrors(viewState.attributeErrors)
    }

    private fun handleAttributesViewState(viewState: AttributesViewState) {
        baseViewModel.submitAction(CharacterCreationAction.UpdateAttributes(viewState.attributes))
        attributesView.setAttributes(viewState.attributes)
        attributesView.setErrors(viewState.errors)
    }
}

package tighe.matthew.expanserpgsheet.encounter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import tighe.matthew.expanserpgsheet.R

class EncounterFragment : Fragment() {
    private val viewModel: EncounterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_encounter, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val test = activity?.findViewById<TextView>(R.id.text_test)
        viewModel.observeViewState().observe(this, Observer { it?.let { viewState ->
            test?.text =  viewState.encounter.characters.first().character.name
        } })
    }
}
package tighe.matthew.expanserpgsheet.encounter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.model.encounter.EncounterCharacter

class EncounterFragment : Fragment(), EncounterAdapter.AdapterListeners {
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

        val adapter = EncounterAdapter(this)
        val recyclerView = activity?.findViewById<RecyclerView>(R.id.list_encounter_characters)!!
        val touchHelpercCallback = EncounterAdapterTouchHelper(adapter)
        val touchHelper = ItemTouchHelper(touchHelpercCallback)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        touchHelper.attachToRecyclerView(recyclerView)
        viewModel.observeViewState().observe(this, Observer { it?.let { viewState ->
            adapter.updateCharacters(viewState.encounter.characters)
        } })
    }

    override fun onDecClick(character: EncounterCharacter) {
        viewModel.submitAction(EncounterAction.DecrementFortune(character))
    }

    override fun onIncClick(character: EncounterCharacter) {
        viewModel.submitAction(EncounterAction.IncrementFortune(character))
    }

    override fun onItemMoved(
        movedCharacter: EncounterCharacter,
        fromPosition: Int,
        displacedCharacter: EncounterCharacter,
        toPosition: Int
    ) {
        val action = EncounterAction.CharacterMoved(
            movedCharacter,
            fromPosition,
            displacedCharacter,
            toPosition
        )
        viewModel.submitAction(action)
    }

    override fun onItemDismissed(character: EncounterCharacter, position: Int) {
        val action = EncounterAction.CharacterRemoved(character, position)
        viewModel.submitAction(action)
    }
}
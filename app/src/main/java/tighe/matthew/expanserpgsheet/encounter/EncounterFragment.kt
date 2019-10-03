package tighe.matthew.expanserpgsheet.encounter

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.condition.Condition
import tighe.matthew.expanserpgsheet.model.encounter.EncounterCharacter

class EncounterFragment : Fragment(), EncounterAdapter.AdapterListeners {
    private val viewModel: EncounterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_encounter, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.observeViewState().observe(this, Observer { it?.let { viewState ->
            handleEncounterCharacterList(viewState)
        } })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_encounter, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_clear_all -> viewModel.submitAction(EncounterAction.ClearAll)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDecClick(character: EncounterCharacter) {
        viewModel.submitAction(EncounterAction.DecrementFortune(character))
    }

    override fun onIncClick(character: EncounterCharacter) {
        viewModel.submitAction(EncounterAction.IncrementFortune(character))
    }

    override fun onFortuneChanged(updatedFortune: String, character: EncounterCharacter) {
        viewModel.submitAction(EncounterAction.SetFortune(updatedFortune, character))
    }

    override fun onConditionChecked(condition: Condition, character: Character) {
        viewModel.submitAction(EncounterAction.ConditionChecked(condition, character))
    }

    override fun onConditionUnchecked(condition: Condition, character: Character) {
        viewModel.submitAction(EncounterAction.ConditionUnchecked(condition, character))
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

    private fun handleEncounterCharacterList(viewState: EncounterViewState) {
        val recyclerView = activity?.findViewById<RecyclerView>(R.id.list_encounter_characters)!!
        val adapter = EncounterAdapter(this)
        recyclerView.adapter = adapter

        val touchHelperCallback = EncounterAdapterTouchHelper(adapter)
        val touchHelper = ItemTouchHelper(touchHelperCallback)
        touchHelper.attachToRecyclerView(recyclerView)

        adapter.updateCharacters(viewState.encounterCharacters)
    }
}
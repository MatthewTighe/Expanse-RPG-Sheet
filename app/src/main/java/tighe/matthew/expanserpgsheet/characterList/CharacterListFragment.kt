package tighe.matthew.expanserpgsheet.characterList

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.viewmodel.ext.android.viewModel
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.navTo
import tighe.matthew.expanserpgsheet.shortSnack

class CharacterListFragment :
    Fragment(),
    CharacterListAdapter.ClickListeners {
    private val viewModel: CharacterListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_character_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.observeEvent().observe(this, Observer { it?.let { event ->
            return@let when (event) {
                is Event.Navigate -> { navTo(event) }
                is Event.Snackbar -> { activity?.shortSnack(event.message) }
            }
        } })

        val adapter = CharacterListAdapter(this)
        val recyclerView = activity?.findViewById<RecyclerView>(R.id.list_characters)!!
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        viewModel.observeViewState().observe(this, Observer { it?.let { viewState ->
            adapter.updateCharacters(viewState.characterList)
            handleInitiativeDialog(viewState)
        } })

        val createBtn = activity?.findViewById<FloatingActionButton>(R.id.btn_create_new)!!
        createBtn.setOnClickListener {
            viewModel.submitAction(CharacterListAction.Add)
        }

        viewModel.submitAction(CharacterListAction.Refresh)
    }

    override fun onClick(character: Character) {
        viewModel.submitAction(CharacterListAction.CharacterClicked(character))
    }

    override fun onOptionsClick(character: Character, anchor: View) {
        val popup = PopupMenu(activity!!, anchor)
        popup.menuInflater.inflate(R.menu.menu_character_options, popup.menu)
        popup.show()
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_add_to_encounter -> {
                    viewModel.submitAction(CharacterListAction.AddToEncounterClicked(character))
                }
                R.id.item_delete -> viewModel.submitAction(CharacterListAction.Delete(character))
            }
            true
        }
    }

    private fun handleInitiativeDialog(viewState: CharacterListViewState) {
        if (!viewState.initiativeDialogShouldBeDisplayed) return
        val editText = EditText(activity!!)
        editText.inputType = InputType.TYPE_CLASS_NUMBER
        editText.id = R.id.initiative_entry_edit_text
        AlertDialog.Builder(activity)
            .setView(editText)
            .setMessage(R.string.entry_initiative)
            .setPositiveButton(R.string.add) { dialog, _ ->
                val initiative = editText.text.toString().toInt()
                val action = CharacterListAction.InitiativeEntered(
                    initiative,
                    viewState.characterBeingManipulated
                )
                viewModel.submitAction(action)
                dialog.dismiss()
            }
            .setNeutralButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create().show()
    }
}
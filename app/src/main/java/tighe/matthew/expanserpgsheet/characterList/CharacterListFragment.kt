package tighe.matthew.expanserpgsheet.characterList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.viewmodel.ext.android.viewModel
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.navTo

class CharacterListFragment : Fragment() {
    private val viewModel: CharacterListViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_character_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.observeEvent().observe(this, Observer { it?.let { event ->
            return@let when (event) {
                is Event.Navigate -> { navTo(event) }
            }
        }})

        val adapter = CharacterListAdapter()
        val recyclerView = activity?.findViewById<RecyclerView>(R.id.list_characters)!!
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        viewModel.observeViewState().observe(this, Observer { it?.let { viewState ->
            adapter.updateCharacters(viewState.characterList)
        }})

        val createBtn = activity?.findViewById<FloatingActionButton>(R.id.btn_create_new)!!
        createBtn.setOnClickListener {
            viewModel.submitAction(CharacterListAction.Add)
        }

        viewModel.submitAction(CharacterListAction.Refresh)
    }
}
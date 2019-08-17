package tighe.matthew.expanserpgsheet.characterList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tighe.matthew.expanserpgsheet.Event
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.navTo

class CharacterListFragment : Fragment() {
    private val viewModel = ViewModelProvider.NewInstanceFactory().create(CharacterListViewModel::class.java)

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

        val createBtn = activity?.findViewById<FloatingActionButton>(R.id.btn_create_new)!!
        createBtn.setOnClickListener { viewModel.submitAction(CharacterListAction.Add) }
    }
}
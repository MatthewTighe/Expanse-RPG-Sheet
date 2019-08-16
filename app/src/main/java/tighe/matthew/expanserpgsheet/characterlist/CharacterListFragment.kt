package tighe.matthew.expanserpgsheet.characterlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tighe.matthew.expanserpgsheet.R

class CharacterListFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_character_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val createBtn = activity?.findViewById<FloatingActionButton>(R.id.btn_create_new)!!
        createBtn.setOnClickListener {  }
    }
}
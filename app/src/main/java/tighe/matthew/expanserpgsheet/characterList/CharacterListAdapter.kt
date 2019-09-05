package tighe.matthew.expanserpgsheet.characterList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.model.character.Character

class CharacterListAdapter(private val listeners: ClickListeners) : RecyclerView.Adapter<CharacterListAdapter.ViewHolder>() {

    interface ClickListeners {
        fun onClick(character: Character)
        fun onOptionsClick(character: Character, anchor: View)
    }

    private val characters = mutableListOf<Character>()

    class ViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        var name = row.findViewById<TextView>(R.id.text_character_name)
        var maxFortune = row.findViewById<TextView>(R.id.text_max_fortune)
        var options = row.findViewById<ImageView>(R.id.btn_options)
    }

    fun updateCharacters(newCharacters: List<Character>) {
        characters.clear()
        characters.addAll(newCharacters)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_character, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characters[position]
        holder.name.text = character.name
        holder.maxFortune.text = character.maxFortune.toString()

        holder.itemView.setOnClickListener { listeners.onClick(character) }
        holder.options.setOnClickListener { listeners.onOptionsClick(character, holder.options) }
    }

    override fun getItemCount(): Int {
        return characters.size
    }
}
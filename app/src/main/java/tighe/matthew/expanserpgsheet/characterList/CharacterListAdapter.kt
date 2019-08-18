package tighe.matthew.expanserpgsheet.characterList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.model.CharacterModel

class CharacterListAdapter(private val listeners: ClickListeners) : RecyclerView.Adapter<CharacterListAdapter.ViewHolder>() {
    interface ClickListeners {
        fun onClick(character: CharacterModel)
    }

    private val characters = mutableListOf<CharacterModel>()

    class ViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        var name = row.findViewById<TextView>(R.id.text_character_name)
        var maxFortune = row.findViewById<TextView>(R.id.text_max_fortune)

    }

    fun updateCharacters(newCharacters: List<CharacterModel>) {
        for (character in newCharacters) {
            if (characters.contains(character)) continue
            characters.add(character)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_character, parent, false))
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characters[position]
        holder.name.text = character.name
        holder.maxFortune.text = character.maxFortune.toString()
        holder.itemView.setOnClickListener { listeners.onClick(character) }
    }
}
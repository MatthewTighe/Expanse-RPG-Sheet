package tighe.matthew.expanserpgsheet.encounter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.model.encounter.EncounterCharacter

class EncounterAdapter(private val listeners: ClickListeners) :
    RecyclerView.Adapter<EncounterAdapter.ViewHolder>() {

    interface ClickListeners {
        fun onDecClick(character: EncounterCharacter)
        fun onIncClick(character: EncounterCharacter)
    }

    class ViewHolder(row: View): RecyclerView.ViewHolder(row) {
        var name = row.findViewById<TextView>(R.id.text_encounter_character_name)
        var initiative = row.findViewById<TextView>(R.id.text_encounter_initiative)
        var maxFortune = row.findViewById<TextView>(R.id.text_encounter_max_fortune)
        var currentFortune = row.findViewById<TextView>(R.id.edit_text_fortune)

        var decBtn = row.findViewById<MaterialButton>(R.id.btn_dec_fortune)
        var incBtn = row.findViewById<MaterialButton>(R.id.btn_inc_fortune)
        var statusBtn = row.findViewById<MaterialButton>(R.id.btn_status_collapsible)
        var expanded = true

        var collapsibleStatusLayout = row.findViewById<LinearLayout>(R.id.layout_collapsible_status)
    }

    private val characters = mutableListOf<EncounterCharacter>()

    fun updateCharacters(newCharacters: List<EncounterCharacter>) {
        characters.clear()
        characters.addAll(newCharacters)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_encounter_character, parent, false))
    }

    override fun onBindViewHolder(holder: EncounterAdapter.ViewHolder, position: Int) {
        val character = characters[position]
        holder.name.text = character.character.name
        holder.initiative.text = character.detail.initiative.toString()
        holder.maxFortune.text = character.character.maxFortune.toString()
        holder.currentFortune.text = character.character.maxFortune.toString() // TODO current

        holder.decBtn.setOnClickListener { listeners.onDecClick(character) }
        holder.incBtn.setOnClickListener { listeners.onIncClick(character) }

        holder.statusBtn.setOnClickListener {
            if (holder.expanded) {
                holder.collapsibleStatusLayout.visibility = View.GONE
                holder.statusBtn.setIconResource(R.drawable.ic_expand_more_24dp)
                holder.expanded = false
            } else {
                holder.collapsibleStatusLayout.visibility = View.VISIBLE
                holder.statusBtn.setIconResource(R.drawable.ic_expand_less_24dp)
                holder.expanded = true
            }
        }
    }

    override fun getItemCount(): Int {
        return characters.size
    }
}
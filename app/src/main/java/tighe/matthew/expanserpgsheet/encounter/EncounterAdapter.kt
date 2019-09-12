package tighe.matthew.expanserpgsheet.encounter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.model.encounter.EncounterCharacter

class EncounterAdapter(private val listeners: AdapterListeners) :
    RecyclerView.Adapter<EncounterAdapter.ViewHolder>(), EncounterAdapterTouchHelper.HelperAdapter {

    interface AdapterListeners {
        fun onDecClick(character: EncounterCharacter)
        fun onIncClick(character: EncounterCharacter)

        fun onItemMoved(
            movedCharacter: EncounterCharacter,
            fromPosition: Int,
            displacedCharacter: EncounterCharacter,
            toPosition: Int
        )
        fun onItemDismissed(character: EncounterCharacter, position: Int)
    }

    class ViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        var name = row.findViewById<TextView>(R.id.text_encounter_character_name)
        var initiative = row.findViewById<TextView>(R.id.text_encounter_initiative)
        var maxFortune = row.findViewById<TextView>(R.id.text_encounter_max_fortune)
        var currentFortune = row.findViewById<TextView>(R.id.edit_text_fortune)
        var fortuneLabel = row.findViewById<TextView>(R.id.label_fortune_adjustment)

        var decBtn = row.findViewById<MaterialButton>(R.id.btn_dec_fortune)
        var incBtn = row.findViewById<MaterialButton>(R.id.btn_inc_fortune)
        var conditionsBtn = row.findViewById<MaterialButton>(R.id.btn_conditions_collapsible)
        var expanded = true

        var collapsibleConditions = row.findViewById<ChipGroup>(R.id.chip_group_conditions)
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characters[position]
        holder.name.text = character.character.name
        holder.initiative.text = character.detail.initiative.toString()
        holder.maxFortune.text = character.character.maxFortune.toString()
        holder.currentFortune.text = character.character.currentFortune.toString()
        holder.fortuneLabel.text =
            holder.fortuneLabel.resources.getString(R.string.hint_current_fortune)

        holder.decBtn.setOnClickListener { listeners.onDecClick(character) }
        holder.incBtn.setOnClickListener { listeners.onIncClick(character) }

        holder.conditionsBtn.setOnClickListener {
            if (holder.expanded) {
                holder.collapsibleConditions.visibility = View.GONE
                holder.conditionsBtn.setIconResource(R.drawable.ic_expand_more_24dp)
                holder.expanded = false
            } else {
                holder.collapsibleConditions.visibility = View.VISIBLE
                holder.conditionsBtn.setIconResource(R.drawable.ic_expand_less_24dp)
                holder.expanded = true
            }
        }
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        val movedCharacter = characters[fromPosition]
        val displacedCharacter = characters[toPosition]
        listeners.onItemMoved(movedCharacter, fromPosition, displacedCharacter, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        val character = characters.removeAt(position)
        listeners.onItemDismissed(character, position)
    }
}
package tighe.matthew.expanserpgsheet.encounter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import org.koin.core.KoinComponent
import org.koin.core.get
import tighe.matthew.expanserpgsheet.UserInputTextWatcher
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.condition.ConditionViewController
import tighe.matthew.expanserpgsheet.model.encounter.EncounterCharacter
import tighe.matthew.expanserpgsheet.setTextBeforeWatching
import org.koin.core.parameter.parametersOf

class EncounterAdapter(private val listeners: AdapterListeners) :
    RecyclerView.Adapter<EncounterAdapter.ViewHolder>(), EncounterAdapterTouchHelper.HelperAdapter {

    interface AdapterListeners {
        fun onDecClick(character: EncounterCharacter)
        fun onIncClick(character: EncounterCharacter)

        fun onFortuneChanged(updatedFortune: String, character: EncounterCharacter)

        fun onItemMoved(
            movedCharacter: EncounterCharacter,
            fromPosition: Int,
            displacedCharacter: EncounterCharacter,
            toPosition: Int
        )
        fun onItemDismissed(character: EncounterCharacter, position: Int)
    }

    class ViewHolder(row: View) : RecyclerView.ViewHolder(row), KoinComponent {
        var name = row.findViewById<TextView>(R.id.text_encounter_character_name)
        var initiative = row.findViewById<TextView>(R.id.text_encounter_initiative)
        var maxFortune = row.findViewById<TextView>(R.id.text_encounter_max_fortune)
        var currentFortune = row.findViewById<EditText>(R.id.edit_text_fortune)
        var fortuneLabel = row.findViewById<TextView>(R.id.label_fortune_adjustment)

        var decBtn = row.findViewById<MaterialButton>(R.id.btn_dec_fortune)
        var incBtn = row.findViewById<MaterialButton>(R.id.btn_inc_fortune)
        var conditionLayout = row.findViewById<LinearLayout>(R.id.layout_encounter_conditions)
    }

    private val characters = mutableListOf<EncounterCharacter>()

    fun updateCharacters(newCharacters: List<EncounterCharacter>) {
        characters.clear()
        characters.addAll(newCharacters)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_encounter_character, parent, false)
            )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characters[position]
        holder.name.text = character.character.name
        holder.initiative.text = character.detail.initiative.toString()
        holder.maxFortune.text = character.character.maxFortune.toString()
        holder.fortuneLabel.text =
            holder.fortuneLabel.resources.getString(R.string.hint_current_fortune)

        holder.decBtn.setOnClickListener { listeners.onDecClick(character) }
        holder.incBtn.setOnClickListener { listeners.onIncClick(character) }

        val userInputWatcher = UserInputTextWatcher(holder.currentFortune) { text ->
            listeners.onFortuneChanged(text, character)
        }

        val currentFortune = character.character.currentFortune.toString()
        holder.currentFortune.setTextBeforeWatching(userInputWatcher, currentFortune)

        holder.get<ConditionViewController> {
            parametersOf(holder.conditionLayout, character.character)
        }
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val movedCharacter = characters[fromPosition]
        val displacedCharacter = characters[toPosition]
        listeners.onItemMoved(movedCharacter, fromPosition, displacedCharacter, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        val character = characters.removeAt(position)
        listeners.onItemDismissed(character, position)
    }
}
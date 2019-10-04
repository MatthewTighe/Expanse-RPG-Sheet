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
import org.w3c.dom.Text
import tighe.matthew.expanserpgsheet.UserInputTextWatcher
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.condition.ConditionView
import tighe.matthew.expanserpgsheet.model.encounter.EncounterCharacter
import tighe.matthew.expanserpgsheet.setTextBeforeWatching
import tighe.matthew.expanserpgsheet.model.character.Character
import tighe.matthew.expanserpgsheet.model.condition.Condition

class EncounterAdapter(private val listeners: AdapterListeners) :
    RecyclerView.Adapter<EncounterAdapter.ViewHolder>(), EncounterAdapterTouchHelper.HelperAdapter {

    interface AdapterListeners {
        fun onDecClick(character: EncounterCharacter)
        fun onIncClick(character: EncounterCharacter)

        fun onFortuneChanged(updatedFortune: String, character: EncounterCharacter)

        fun onConditionChecked(condition: Condition, character: Character)
        fun onConditionUnchecked(condition: Condition, character: Character)

        fun onItemMoved(
            movedCharacter: EncounterCharacter,
            fromPosition: Int,
            displacedCharacter: EncounterCharacter,
            toPosition: Int
        )
        fun onItemDismissed(character: EncounterCharacter, position: Int)
    }

    class ViewHolder(row: View) : RecyclerView.ViewHolder(row), KoinComponent {
        var name = row.findViewById<TextView>(R.id.encounter_text_character_name)!!
        var initiative = row.findViewById<TextView>(R.id.encounter_text_initiative)!!
        var defense = row.findViewById<TextView>(R.id.encounter_text_defense)
        var toughness = row.findViewById<TextView>(R.id.encounter_text_toughness)
        var maxFortune = row.findViewById<TextView>(R.id.encounter_text_max_fortune)!!
        var currentFortune = row.findViewById<EditText>(R.id.edit_text_fortune)!!
        var fortuneLabel = row.findViewById<TextView>(R.id.label_fortune_adjustment)!!

        var accuracyText = row.findViewById<TextView>(R.id.text_accuracy_small)!!
        var communicationText = row.findViewById<TextView>(R.id.text_communication_small)!!
        var constitutionText = row.findViewById<TextView>(R.id.text_constitution_small)!!
        var dexterityText = row.findViewById<TextView>(R.id.text_dexterity_small)!!
        var fightingText = row.findViewById<TextView>(R.id.text_fighting_small)!!
        var intelligenceText = row.findViewById<TextView>(R.id.text_intelligence_small)!!
        var perceptionText = row.findViewById<TextView>(R.id.text_perception_small)!!
        var strengthText = row.findViewById<TextView>(R.id.text_strength_small)!!
        var willpowerText = row.findViewById<TextView>(R.id.text_willpower_small)!!

        var decBtn = row.findViewById<MaterialButton>(R.id.btn_dec_fortune)!!
        var incBtn = row.findViewById<MaterialButton>(R.id.btn_inc_fortune)!!
        var conditionLayout = row.findViewById<LinearLayout>(R.id.encounter_layout_conditions)!!
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
        holder.initiative.text = holder.itemView.resources.getString(
            R.string.initiative_interpolated, character.detail.initiative
        )
        holder.defense.text = holder.itemView.resources.getString(
            R.string.defense_interpolated, character.character.getDefense()
        )
        holder.toughness.text = holder.itemView.resources.getString(
            R.string.toughness_interpolated, character.character.getToughness()
        )
        holder.maxFortune.text = holder.itemView.resources.getString(
            R.string.max_fortune_interpolated, character.character.maxFortune
        )
        holder.fortuneLabel.text =
            holder.fortuneLabel.resources.getString(R.string.hint_current_fortune)

        setupAttributeFields(holder, character.character)

        holder.decBtn.setOnClickListener { listeners.onDecClick(character) }
        holder.incBtn.setOnClickListener { listeners.onIncClick(character) }

        val userInputWatcher = UserInputTextWatcher(holder.currentFortune) { text ->
            listeners.onFortuneChanged(text, character)
        }

        val currentFortune = character.character.currentFortune.toString()
        holder.currentFortune.setTextBeforeWatching(userInputWatcher, currentFortune)

        val onConditionChecked = { condition: Condition ->
            listeners.onConditionChecked(condition, character.character)
        }
        val onConditionUnchecked = { condition: Condition ->
            listeners.onConditionUnchecked(condition, character.character)
        }
        ConditionView(
            holder.conditionLayout,
            character.character.conditions,
            onConditionChecked,
            onConditionUnchecked
        )
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

    private fun setupAttributeFields(holder: ViewHolder, character: Character) {
        val resources = holder.itemView.resources
        holder.accuracyText.text = resources.getString(
            R.string.accuracy_abbreviated_interpolated, character.attributes.accuracy
        )
        holder.communicationText.text = resources.getString(
            R.string.communication_abbreviated_interpolated, character.attributes.communication
        )
        holder.constitutionText.text = resources.getString(
            R.string.constitution_abbreviated_interpolated, character.attributes.constitution
        )
        holder.dexterityText.text = resources.getString(
            R.string.dexterity_abbreviated_interpolated, character.attributes.dexterity
        )
        holder.fightingText.text = resources.getString(
            R.string.fighting_abbreviated_interpolated, character.attributes.fighting
        )
        holder.intelligenceText.text = resources.getString(
            R.string.intelligence_abbreviated_interpolated, character.attributes.intelligence
        )
        holder.perceptionText.text = resources.getString(
            R.string.perception_abbreviated_interpolated, character.attributes.perception
        )
        holder.strengthText.text = resources.getString(
            R.string.strength_abbreviated_interpolated, character.attributes.strength
        )
        holder.willpowerText.text = resources.getString(
            R.string.willpower_abbreviated_interpolated, character.attributes.willpower
        )
    }
}
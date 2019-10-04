package tighe.matthew.expanserpgsheet.armor

import android.content.Context
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.layout_dropdown.view.*
import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.model.character.Armor

class ArmorDropdown(
    layout: TextInputLayout,
    context: Context,
    listener: (Armor) -> Unit,
    current: Armor = Armor.None
) {

    private val armorResourceMap = listOf(
        Armor.None to R.string.none,
        Armor.Padding to R.string.padding,
        Armor.Light to R.string.light,
        Armor.Medium to R.string.medium,
        Armor.Heavy to R.string.heavy,
        Armor.Power to R.string.power
    )

    private val resources = context.resources
    private val textView = layout.findViewById<AutoCompleteTextView>(R.id.dropdown_text)
    private val armorChoices = armorResourceMap.map { (_, res) ->
        resources.getString(res)
    }
    private val adapter = ArrayAdapter(context, R.layout.layout_dropdown_item, armorChoices)

    init {
        layout.hint = resources.getString(R.string.armor)
        textView.inputType = InputType.TYPE_NULL
        textView.isEnabled = false
        textView.text.replace(0, textView.text.length, getTextFromArmor(current))
        textView.setAdapter(adapter)
        textView.setOnItemClickListener { _, view, _, _ ->
            val armor = getArmorFromView(view)
            listener(armor)
        }
    }

    private fun getTextFromArmor(armorToFind: Armor): String {
        return resources.getString(armorResourceMap.find { (armor, _) ->
            armor == armorToFind
        }!!.second)
    }

    private fun getArmorFromView(view: View): Armor {
        view as TextView
        return armorResourceMap.find { (_, resource) ->
            view.text == resources.getString(resource)
        }!!.first
    }
}
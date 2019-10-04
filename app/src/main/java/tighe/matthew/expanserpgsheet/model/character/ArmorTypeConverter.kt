package tighe.matthew.expanserpgsheet.model.character

import androidx.room.TypeConverter
import java.lang.RuntimeException

class ArmorTypeConverter {
    private val noneString = "none"
    private val paddingString = "padding"
    private val lightString = "light"
    private val mediumString = "medium"
    private val heavyString = "heavy"
    private val powerString = "power"

    @TypeConverter
    fun toArmor(asString: String): Armor {
        return when (asString) {
            noneString -> Armor.None
            paddingString -> Armor.Padding
            lightString -> Armor.Light
            mediumString -> Armor.Medium
            heavyString -> Armor.Heavy
            powerString -> Armor.Power
            else -> throw RuntimeException("Armor could not be converted from string")
        }
    }

    @TypeConverter
    fun armorToString(armor: Armor): String {
        return when (armor) {
            Armor.None -> noneString
            Armor.Padding -> paddingString
            Armor.Light -> lightString
            Armor.Medium -> mediumString
            Armor.Heavy -> heavyString
            Armor.Power -> powerString
        }
    }
}
package tighe.matthew.expanserpgsheet.model.condition

import androidx.room.TypeConverter

class ConditionTypeConverter {
    @TypeConverter
    fun toCondition(asString: String): Condition {
        return when (asString) {
            "injured" -> Condition.Injured
            "wounded" -> Condition.Wounded
            "taken_out" -> Condition.TakenOut
            else -> throw RuntimeException("Condition could not be converted from string")
        }
    }

    @TypeConverter
    fun conditionToString(condition: Condition): String {
        return when (condition) {
            is Condition.Injured -> "injured"
            is Condition.Wounded -> "wounded"
            is Condition.TakenOut -> "taken_out"
        }
    }
}
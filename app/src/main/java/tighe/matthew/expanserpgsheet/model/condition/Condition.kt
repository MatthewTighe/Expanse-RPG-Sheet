package tighe.matthew.expanserpgsheet.model.condition

sealed class Condition {
    object Injured : Condition()
    object Wounded : Condition()
    object TakenOut : Condition()
}
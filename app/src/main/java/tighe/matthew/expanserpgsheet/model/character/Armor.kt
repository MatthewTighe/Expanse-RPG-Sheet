package tighe.matthew.expanserpgsheet.model.character

sealed class Armor {

    abstract val bonus: Int
    abstract val penalty: Int

    object None : Armor() {
        override val bonus: Int
            get() = 0
        override val penalty: Int
            get() = 0
    }

    object Padding : Armor() {
        override val bonus: Int
            get() = 1
        override val penalty: Int
            get() = 0
    }

    object Light : Armor() {
        override val bonus: Int
            get() = 2
        override val penalty: Int
            get() = 1
    }

    object Medium : Armor() {
        override val bonus: Int
            get() = 4
        override val penalty: Int
            get() = 2
    }

    object Heavy : Armor() {
        override val bonus: Int
            get() = 6
        override val penalty: Int
            get() = 3
    }

    object Power : Armor() {
        override val bonus: Int
            get() = 12
        override val penalty: Int
            get() = 0
    }
}
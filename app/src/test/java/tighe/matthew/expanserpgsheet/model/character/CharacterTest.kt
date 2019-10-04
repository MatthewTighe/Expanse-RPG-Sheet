package tighe.matthew.expanserpgsheet.model.character

import org.junit.Assert.*
import org.junit.Test

class CharacterTest {

    @Test
    fun `Defense is computed with dexterity`() {
        val dex = 5
        val attributes = Attributes.UNFILLED_ATTRIBUTES.copy(dexterity = dex)
        val character = Character(attributes = attributes)

        val result = character.defense

        assertEquals(10 + dex, result)
    }

    @Test
    fun `Toughness is computed with constitution and armor bonus`() {
        val con = 5
        val armor = Armor.Medium
        val attributes = Attributes.UNFILLED_ATTRIBUTES.copy(constitution = con)
        val character = Character(attributes = attributes, armor = armor)

        val result = character.toughness + armor.bonus

        assertEquals(con, result)
    }
}
package tighe.matthew.expanserpgsheet.characterCreation

import tighe.matthew.expanserpgsheet.R
import tighe.matthew.expanserpgsheet.TextInputFieldError
import tighe.matthew.expanserpgsheet.ViewState

internal data class CharacterCreationViewState(
    val nameError: NameError = NameError(),
    val accuracyError: AccuracyError = AccuracyError(),
    val communicationError: CommunicationError = CommunicationError(),
    val constitutionError: ConstitutionError = ConstitutionError(),
    val dexterityError: DexterityError = DexterityError(),
    val fightingError: FightingError = FightingError(),
    val intelligenceError: IntelligenceError = IntelligenceError(),
    val perceptionError: PerceptionError = PerceptionError(),
    val strengthError: StrengthError = StrengthError(),
    val willpowerError: WillpowerError = WillpowerError()
) : ViewState

internal data class NameError(
    override val errorEnabled: Boolean = false,
    override val fieldName: Int = R.string.name
) : TextInputFieldError

internal data class AccuracyError(
    override val errorEnabled: Boolean = false,
    override val fieldName: Int = R.string.accuracy
) : TextInputFieldError

internal data class CommunicationError(
    override val errorEnabled: Boolean = false,
    override val fieldName: Int = R.string.communication
) : TextInputFieldError

internal data class ConstitutionError(
    override val errorEnabled: Boolean = false,
    override val fieldName: Int = R.string.constitution
) : TextInputFieldError

internal data class DexterityError(
    override val errorEnabled: Boolean = false,
    override val fieldName: Int = R.string.dexterity
) : TextInputFieldError

internal data class FightingError(
    override val errorEnabled: Boolean = false,
    override val fieldName: Int = R.string.fighting
) : TextInputFieldError

internal data class IntelligenceError(
    override val errorEnabled: Boolean = false,
    override val fieldName: Int = R.string.intelligence
) : TextInputFieldError

internal data class PerceptionError(
    override val errorEnabled: Boolean = false,
    override val fieldName: Int = R.string.perception
) : TextInputFieldError

internal data class StrengthError(
    override val errorEnabled: Boolean = false,
    override val fieldName: Int = R.string.strength
) : TextInputFieldError

internal data class WillpowerError(
    override val errorEnabled: Boolean = false,
    override val fieldName: Int = R.string.willpower
) : TextInputFieldError
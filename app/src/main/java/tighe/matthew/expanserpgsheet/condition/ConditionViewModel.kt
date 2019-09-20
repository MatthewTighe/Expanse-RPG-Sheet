package tighe.matthew.expanserpgsheet.condition

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tighe.matthew.expanserpgsheet.FlowViewModel
import tighe.matthew.expanserpgsheet.model.character.CharacterRepository

class ConditionViewModel(private val characterRepository: CharacterRepository) :
    ViewModel(), FlowViewModel<ConditionViewState, ConditionAction> {

    override fun observeViewState(): Flow<ConditionViewState> {
        return flow {
        }
    }
    override fun submitAction(action: ConditionAction) {
    }
}
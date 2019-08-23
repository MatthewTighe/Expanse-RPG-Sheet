package tighe.matthew.expanserpgsheet.characterCreation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import tighe.matthew.expanserpgsheet.*
import tighe.matthew.expanserpgsheet.model.CharacterModel
import tighe.matthew.expanserpgsheet.repository.CharacterRepository

class CharacterCreationViewModelTest : KoinTest {
    @get:Rule val rule = InstantTaskExecutorRule()

    val mockEventObserver = mockk<Observer<Event?>>(relaxUnitFun = true)
    val mockRepo = mockk<CharacterRepository>(relaxUnitFun = true)

    private lateinit var viewModel: CharacterCreationViewModel

    @Before
    fun setup() {
        viewModel = CharacterCreationViewModel(mockRepo)
    }

    @Test
    fun `Save action persists model to repository`() {
        val model = CharacterModel("name", 10)

        viewModel.observeEvent().observeForever(mockEventObserver)
        viewModel.submitAction(CharacterCreationAction.NameInput(model.name))
        viewModel.submitAction(CharacterCreationAction.MaxFortuneInput(model.maxFortune))
        viewModel.submitAction(CharacterCreationAction.Save)

        verify { mockRepo.persist(model) }
        verify { mockEventObserver.onChanged(Event.Navigate(R.id.characterListFragment)) }
    }
}
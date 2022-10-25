package com.msf.itunessearch.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.msf.itunessearch.core.StubFactory
import com.msf.itunessearch.core.TestCoroutineRule
import com.msf.itunessearch.core.getOrAwaitValue
import com.msf.itunessearch.model.TracksResponse
import com.msf.itunessearch.network.ResultWrapper
import com.msf.itunessearch.usecase.ItunesSearchUseCase
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
internal class ItunesSearchViewModelTest {
    private val useCase: ItunesSearchUseCase = mockk(relaxed = true)
    private lateinit var viewModel: ItunesSearchViewModel

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    private val testCoroutineRule = TestCoroutineRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ItunesSearchViewModel(useCase)
    }

    @Test
    fun `GIVEN viewmodel is initialized THEN uiStateLiveData should return state empty`() {
        viewModel.uiStateLiveData.getOrAwaitValue().shouldBeInstanceOf<ItunesUiState.Empty>()
    }

    @Test
    fun `GIVEN a title for activity has value WHEN update title activity is called THEN should livedata be updated`() {
        val title = "title"

        viewModel.setTitleActivity(title)

        viewModel.titleFragmentLiveData.getOrAwaitValue() shouldBe title
    }

    @Test
    fun `GIVEN term and country is valid WHEN fetchMusic is called THEN should livedata be update to LOADED`() {
        every { useCase.invoke(any(), any(), any(), any()) } answers {
            lastArg<(ResultWrapper.Success<TracksResponse>) -> ResultWrapper.Success<TracksResponse>>().invoke(ResultWrapper.Success(StubFactory.tracksResponse))
        }

        testDispatcher.pauseDispatcher()
        viewModel.fetchMusic(anyString(), anyString())

        viewModel.uiStateLiveData.getOrAwaitValue().shouldBeInstanceOf<ItunesUiState.Loading>()
        testDispatcher.resumeDispatcher()

        viewModel.uiStateLiveData.getOrAwaitValue().shouldBeInstanceOf<ItunesUiState.Loaded>()
        val loadedValue = viewModel.uiStateLiveData.getOrAwaitValue() as ItunesUiState.Loaded
        loadedValue.musics shouldNotBe null
        loadedValue.musics.size shouldBe 1
        loadedValue.musics[0].artistName shouldBe "artistName"
    }

    @Test
    fun `GIVEN anyString for parameter WHEN fetchMusic is called AND return an error THEN should livedata be update to ERROR`() {
        every { useCase.invoke(any(), any(), any(), any()) } answers {
            lastArg<(ResultWrapper.GenericError) -> ResultWrapper.GenericError>().invoke(ResultWrapper.GenericError(400, "error"))
        }

        viewModel.fetchMusic(anyString(), anyString())

        viewModel.uiStateLiveData.getOrAwaitValue().shouldBeInstanceOf<ItunesUiState.Error>()
        val errorValue = viewModel.uiStateLiveData.getOrAwaitValue() as ItunesUiState.Error
        errorValue shouldNotBe null
        errorValue.message shouldBe "error"
    }

    @Test
    fun `GIVEN anyString for parameter WHEN fetchMusic is called AND return an NetworkError THEN should livedata be update to EMPTY`() {
        every { useCase.invoke(any(), any(), any(), any()) } answers {
            lastArg<(ResultWrapper.NetworkError) -> ResultWrapper.NetworkError>().invoke(ResultWrapper.NetworkError)
        }

        viewModel.fetchMusic(anyString(), anyString())

        viewModel.uiStateLiveData.getOrAwaitValue().shouldBeInstanceOf<ItunesUiState.Empty>()
    }

    @Test
    fun `GIVEN anyString for parameter WHEN fetchMusic is called AND an error occurs with a message THEN should livedata be update to ERROR`() {
        every { useCase.invoke(any(), any(), any(), any()) } answers {
            thirdArg<(Throwable) -> Throwable>().invoke(Throwable("Message"))
        }

        viewModel.fetchMusic(anyString(), anyString())

        viewModel.fetchMusic(anyString(), anyString())

        viewModel.uiStateLiveData.getOrAwaitValue().shouldBeInstanceOf<ItunesUiState.Error>()
        val errorValue = viewModel.uiStateLiveData.getOrAwaitValue() as ItunesUiState.Error
        errorValue shouldNotBe null
        errorValue.message shouldBe "Message"
    }

    @Test
    fun `GIVEN anyString for parameter WHEN fetchMusic is called AND an error occurs without a message THEN should livedata be update to ERROR`() {
        every { useCase.invoke(any(), any(), any(), any()) } answers {
            thirdArg<(Throwable) -> Throwable>().invoke(Throwable())
        }

        viewModel.fetchMusic(anyString(), anyString())

        viewModel.uiStateLiveData.getOrAwaitValue().shouldBeInstanceOf<ItunesUiState.Error>()
        val errorValue = viewModel.uiStateLiveData.getOrAwaitValue() as ItunesUiState.Error
        errorValue shouldNotBe null
        errorValue.message shouldBe "Something got wrong, please try again"
    }

    @Test
    fun `GIVEN uiStateLiveData is loaded WHEN getMusic is called THEN should return a music from array`() {
        viewModel.setMusics(StubFactory.tracksResponse.musics)

        val music = viewModel.getMusic(0)

        music shouldNotBe null
        music!!.artistName shouldBe "artistName"
    }

    @Test
    fun `GIVEN uiStateLiveData is not loaded WHEN getMusic is called THEN should return null`() {
        val music = viewModel.getMusic(0)

        music shouldBe null
    }
}

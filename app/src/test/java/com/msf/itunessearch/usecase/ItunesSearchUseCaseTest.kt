package com.msf.itunessearch.usecase

import com.msf.itunessearch.core.CoroutinesContextProvider
import com.msf.itunessearch.core.RequestWrapperImpl
import com.msf.itunessearch.core.StubFactory
import com.msf.itunessearch.core.TestCoroutineRule
import com.msf.itunessearch.model.TracksResponse
import com.msf.itunessearch.network.ResultWrapper
import com.msf.itunessearch.repository.ItunesSearchRepository
import io.kotlintest.matchers.types.shouldBeSameInstanceAs
import io.kotlintest.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

@OptIn(ExperimentalCoroutinesApi::class)
internal class ItunesSearchUseCaseTest {

    private val repository: ItunesSearchRepository = mockk(relaxed = true)
    private val requestWrapper = RequestWrapperImpl()
    private val contextProvider: CoroutinesContextProvider = mockk(relaxed = true)

    private lateinit var useCase: ItunesSearchUseCase

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    private val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        useCase = ItunesSearchUseCase(
            repository = repository,
            contextProvider = contextProvider,
            requestWrapper = requestWrapper
        )
    }

    @Test
    fun `GIVEN repository is ok WHEN useCase runs THEN fetchMusics is called`() {
        coEvery { repository.fetchMusics(anyString(), anyString()) } returns ResultWrapper.Success(StubFactory.tracksResponse)
        runBlocking {
            useCase.run(Pair(anyString(), anyString()))

            coVerify(exactly = 1) {
                repository.fetchMusics(anyString(), anyString())
            }
        }
    }

    @Test
    fun `GIVEN repository will response success WHEN useCase runs THEN should return success true`() {
        coEvery { repository.fetchMusics(anyString(), anyString()) } returns ResultWrapper.Success(StubFactory.tracksResponse)
        runBlocking {
            val result = useCase.run(Pair(anyString(), anyString()))

            coVerify(exactly = 1) {
                repository.fetchMusics(anyString(), anyString())
            }

            result.isFailure shouldBe false
            result.isSuccess shouldBe true
            result.success(StubFactory.tracksResponse).success shouldBeSameInstanceAs StubFactory.tracksResponse
        }
    }

    @Test
    fun `GIVEN repository will response success but resultCount is 0 WHEN useCase runs THEN should return success true`() {
        coEvery { repository.fetchMusics(anyString(), anyString()) } returns ResultWrapper.Success(TracksResponse(0, emptyList()))
        runBlocking {
            val result = useCase.run(Pair(anyString(), anyString()))

            coVerify(exactly = 1) {
                repository.fetchMusics(anyString(), anyString())
            }

            result.isFailure shouldBe false
            result.isSuccess shouldBe true
        }
    }

    @Test
    fun `GIVEN repository will response generic error WHEN useCase runs THEN should return success true`() {
        coEvery { repository.fetchMusics(anyString(), anyString()) } returns ResultWrapper.GenericError(404, "error")
        runBlocking {
            val result = useCase.run(Pair(anyString(), anyString()))

            coVerify(exactly = 1) {
                repository.fetchMusics(anyString(), anyString())
            }

            result.isFailure shouldBe false
            result.isSuccess shouldBe true
        }
    }

    @Test
    fun `GIVEN repository will response network error WHEN useCase runs THEN should return success true`() {
        coEvery { repository.fetchMusics(anyString(), anyString()) } returns ResultWrapper.NetworkError
        runBlocking {
            val result = useCase.run(Pair(anyString(), anyString()))

            coVerify(exactly = 1) {
                repository.fetchMusics(anyString(), anyString())
            }

            result.isFailure shouldBe false
            result.isSuccess shouldBe true
        }
    }

}

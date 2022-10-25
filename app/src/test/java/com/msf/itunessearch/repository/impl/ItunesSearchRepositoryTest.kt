package com.msf.itunessearch.repository.impl

import com.msf.itunessearch.core.StubFactory
import com.msf.itunessearch.core.TestCoroutineRule
import com.msf.itunessearch.model.TracksResponse
import com.msf.itunessearch.network.ItunesService
import com.msf.itunessearch.network.ResultWrapper
import com.msf.itunessearch.repository.ItunesSearchRepository
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import retrofit2.HttpException

@OptIn(ExperimentalCoroutinesApi::class)
internal class ItunesSearchRepositoryTest {

    private val itunesService: ItunesService = mockk(relaxed = true)
    private lateinit var itunesSearchRepository: ItunesSearchRepository

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    private val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        itunesSearchRepository = ItunesSearchRepositoryImpl(itunesService)
    }

    @Test
    fun `GIVEN service return something WHEN fetchMusics is called THEN should return ResultWrapper Success with data`() = testCoroutineRule.runBlockingTest {
        coEvery { itunesService.fetchMusics(anyString(), anyString()) } returns StubFactory.tracksResponse

        val resultWrapper = itunesSearchRepository.fetchMusics(anyString(), anyString())

        resultWrapper shouldNotBe null
        resultWrapper.shouldBeInstanceOf<ResultWrapper.Success<TracksResponse>>()
        (resultWrapper as ResultWrapper.Success<TracksResponse>).value shouldNotBe null
        resultWrapper.value.resultCount shouldBe 1
        resultWrapper.value.musics.size shouldBe 1
        resultWrapper.value.musics[0].artistName shouldBe "artistName"
    }

    @Test
    fun `GIVEN service will throw an httpexception WHEN fetchMusics is called THEN should return ResultWrapper Generic Error`() = testCoroutineRule.runBlockingTest {
        val httpException = mockk<HttpException>(relaxed = true)
        coEvery { itunesService.fetchMusics(anyString(), anyString()) } throws httpException

        val resultWrapper = itunesSearchRepository.fetchMusics(anyString(), anyString())

        resultWrapper shouldNotBe null
        resultWrapper.shouldBeInstanceOf<ResultWrapper.GenericError>()
        (resultWrapper as ResultWrapper.GenericError) shouldNotBe null
        resultWrapper.code shouldBe httpException.code()
        resultWrapper.error shouldBe httpException.message
    }

    @Test
    fun `GIVEN service will throw an exception WHEN fetchMusics is called THEN should return ResultWrapper Generic Error`() = testCoroutineRule.runBlockingTest {
        coEvery { itunesService.fetchMusics(anyString(), anyString()) } throws Exception()

        val resultWrapper = itunesSearchRepository.fetchMusics(anyString(), anyString())

        resultWrapper shouldNotBe null
        resultWrapper.shouldBeInstanceOf<ResultWrapper.GenericError>()
        (resultWrapper as ResultWrapper.GenericError) shouldNotBe null
        resultWrapper.code shouldBe 999
        resultWrapper.error shouldBe "Something weird happens"
    }
}

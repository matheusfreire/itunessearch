package com.msf.itunessearch.ui

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.view.isVisible
import androidx.fragment.app.testing.launchFragmentInContainer
import com.msf.itunessearch.R
import com.msf.itunessearch.usecase.ItunesSearchUseCase
import com.msf.itunessearch.viewmodel.ItunesSearchViewModel
import io.kotlintest.shouldBe
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.mockito.ArgumentMatchers.anyString
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
internal class MusicListFragmentTest {

    private val useCase: ItunesSearchUseCase = mockk(relaxed = true)
    private lateinit var viewModel: ItunesSearchViewModel

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = ItunesSearchViewModel(useCase)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `should show empty view`() {
        val scenario = launchFragmentInContainer(themeResId = R.style.Theme_Itunessearch) {
            MusicListFragment()
        }
        scenario.onFragment {
            it.binding.itemList.isVisible shouldBe false
            it.binding.progressBar.isVisible shouldBe false
            it.binding.msgLayout.msgEmptyLayout.isVisible shouldBe true
            it.binding.msgLayout.imageEmptyLayout.isVisible shouldBe true
            it.binding.msgLayout.msgEmptyLayout.text shouldBe "Find your musics,\ntype the name of it"
        }
    }

    @Test
    fun `show progress when type input`() {
        val scenario = launchFragmentInContainer(themeResId = R.style.Theme_Itunessearch) {
            MusicListFragment()
        }
        scenario.onFragment {
            it.binding.txtInputEdit.setText(anyString())
            it.binding.progressBar.isVisible shouldBe true
            it.binding.msgLayout.msgEmptyLayout.isVisible shouldBe false
            it.binding.msgLayout.imageEmptyLayout.isVisible shouldBe false
        }
    }

//    @Test
//    fun `show loaded state when return from viewmodel`() {
//        every { useCase.invoke(any(), any(), any(), any()) } answers {
//            lastArg<(ResultWrapper.Success<TracksResponse>) -> ResultWrapper.Success<TracksResponse>>().invoke(ResultWrapper.Success(StubFactory.tracksResponse))
//        }
//        val scenario = launchFragmentInContainer(themeResId = R.style.Theme_Itunessearch) {
//            MusicListFragment()
//        }
//        scenario.onFragment {
//            it.binding.txtInputEdit.setText("term")
//            it.binding.itemList.isVisible shouldBe true
//            it.binding.itemList.adapter?.itemCount shouldNotBe null
//            it.binding.itemList.adapter?.itemCount shouldBe 1
//        }
//    }
}

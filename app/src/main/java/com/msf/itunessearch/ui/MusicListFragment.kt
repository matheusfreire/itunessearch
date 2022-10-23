package com.msf.itunessearch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.msf.itunessearch.R
import com.msf.itunessearch.databinding.FragmentItemListBinding
import com.msf.itunessearch.extensions.textInputAsFlow
import com.msf.itunessearch.model.Music
import com.msf.itunessearch.viewmodel.ItunesSearchViewModel
import com.msf.itunessearch.viewmodel.ItunesUiState
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MusicListFragment : Fragment() {

    private val itunesSearchViewModel by sharedViewModel<ItunesSearchViewModel>()
    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        itunesSearchViewModel.uiStateLiveData.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is ItunesUiState.Loading -> showLoadingView(uiState.loading)
                is ItunesUiState.Loaded -> setupRecyclerView(uiState.musics.toMutableList())
                is ItunesUiState.Empty -> showMessageLayout(
                    getString(R.string.empty_search),
                    android.R.drawable.ic_media_play
                )
                is ItunesUiState.Error -> showMessageLayout(uiState.message, R.drawable.ic_error)
            }
        }
        binding.txtInputEdit?.textInputAsFlow()
            ?.onEach {
                itunesSearchViewModel.fetchMusic(
                    it.toString(),
                    requireContext().resources.configuration.locales[0].country
                )
            }
            ?.debounce(750)
            ?.launchIn(lifecycleScope)
    }

    private fun showLoadingView(showLoading: Boolean) {
        binding.progressBar?.isVisible = showLoading
        binding.msgLayout?.msgEmptyLayout?.isVisible = false
        binding.msgLayout?.imageEmptyLayout?.isVisible = false
    }

    private fun showMessageLayout(message: String, @DrawableRes img: Int) {
        binding.itemList.isVisible = false
        showEmptyLayout(message, img)
        binding.msgLayout?.msgEmptyLayout?.isVisible = true
        binding.msgLayout?.imageEmptyLayout?.isVisible = true
    }

    private fun showEmptyLayout(msg: String, @DrawableRes drawable: Int) {
        binding.msgLayout?.let {
            it.msgEmptyLayout.text = msg
            it.imageEmptyLayout.setImageResource(drawable)
        }
    }

    private fun setupRecyclerView(musics: MutableList<Music>) {
        val recyclerView: RecyclerView = binding.itemList

        val itemDetailFragmentContainer: View? = binding.root.findViewById(R.id.item_detail_nav_container)
        recyclerView.adapter = MusicAdapter(
            musics,
            itemDetailFragmentContainer
        )
        showLoadingView(false)
        binding.itemList.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.msf.itunessearch.ui

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.msf.itunessearch.databinding.FragmentItemDetailBinding
import com.msf.itunessearch.model.Music
import com.msf.itunessearch.viewmodel.ItunesSearchViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MusicDetailFragment : Fragment() {

    private val itunesSearchViewModel by sharedViewModel<ItunesSearchViewModel>()
    private var item: Music? = null

    private var toolbarLayout: CollapsingToolbarLayout? = null

    private var _binding: FragmentItemDetailBinding? = null

    private val binding get() = _binding!!

    private val dragListener = View.OnDragListener { v, event ->
        if (event.action == DragEvent.ACTION_DROP) {
            val clipDataItem: ClipData.Item = event.clipData.getItemAt(0)
            val dragData = clipDataItem.text
            item = itunesSearchViewModel.getMusic(id)
            updateContent()
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                item = itunesSearchViewModel.getMusic(it.getInt(ARG_ITEM_ID))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        toolbarLayout = binding.toolbarLayout

        updateContent()
        rootView.setOnDragListener(dragListener)

        return rootView
    }

    private fun updateContent() {
        toolbarLayout?.title = item?.trackName
    }

    companion object {
        const val ARG_ITEM_ID = "music_position"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

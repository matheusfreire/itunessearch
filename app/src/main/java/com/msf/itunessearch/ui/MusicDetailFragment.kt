package com.msf.itunessearch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.msf.itunessearch.R
import com.msf.itunessearch.databinding.FragmentItemDetailBinding
import com.msf.itunessearch.extensions.isExplicit
import com.msf.itunessearch.extensions.toFormatDate
import com.msf.itunessearch.model.Music
import com.msf.itunessearch.viewmodel.ItunesSearchViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MusicDetailFragment : Fragment() {

    private val itunesSearchViewModel by sharedViewModel<ItunesSearchViewModel>()
    private lateinit var item: Music

    private var _binding: FragmentItemDetailBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                itunesSearchViewModel.getMusic(it.getInt(ARG_ITEM_ID))?.let { music ->
                    item = music
                }
            }
        }
        itunesSearchViewModel.setTitleActivity(item.trackName)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            musicTitle?.text = item.trackName
            musicDetailArtist?.text = item.artistName
            musicDetailAlbumText?.text = item.collectionName
            musicDetailAlbum?.let {
                Glide.with(requireContext()).load(item.artworkUrl100).placeholder(R.drawable.ic_music)
                    .into(it)
            }
            musicDetailGenre?.text = item.primaryGenreName
            musicDetailReleaseDate?.text = item.releaseDate.toFormatDate()
            musicExplicit?.isChecked = item.trackExplicitness.isExplicit()
        }
    }

    companion object {
        const val ARG_ITEM_ID = "music_position"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        itunesSearchViewModel.setTitleActivity(getString(R.string.app_name))
        _binding = null
    }
}

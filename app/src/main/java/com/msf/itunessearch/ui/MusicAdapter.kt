package com.msf.itunessearch.ui

import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.msf.itunessearch.R
import com.msf.itunessearch.databinding.ItemListContentBinding
import com.msf.itunessearch.model.Music

class MusicAdapter(
    private val values: List<Music>,
    private val itemDetailFragmentContainer: View?
) : RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    private fun getItemByPosition(position: Int): Music = values[position]

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = getItemByPosition(position)
        with(holder) {
            musicTrack.text = music.trackName
            musicCollectionName.text = music.collectionName
            musicArtist.text = music.artistName
            Glide.with(holder.itemView.context).load(music.artworkUrl100).placeholder(R.drawable.ic_music)
                .into(musicAlbum)
        }

        with(holder.itemView) {
            tag = music
            setOnClickListener { itemView ->
                val bundle = Bundle()
                bundle.putInt(
                    MusicDetailFragment.ARG_ITEM_ID,
                    position
                )
                if (itemDetailFragmentContainer != null) {
                    itemDetailFragmentContainer.findNavController()
                        .navigate(R.id.fragment_item_detail, bundle)
                } else {
                    itemView.findNavController().navigate(R.id.show_item_detail, bundle)
                }
            }
            setOnContextClickListener { v ->
                val item = v.tag as Music
                Toast.makeText(
                    v.context,
                    "Context click of item " + item.trackId.toString(),
                    Toast.LENGTH_LONG
                ).show()
                true
            }

            setOnLongClickListener { v ->
                // Setting the item id as the clip data so that the drop target is able to
                // identify the id of the content
                val clipItem = ClipData.Item(music.trackId.toString())
                val dragData = ClipData(
                    v.tag as? CharSequence,
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                    clipItem
                )

                v.startDragAndDrop(
                    dragData,
                    View.DragShadowBuilder(v),
                    null,
                    0
                )
            }
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(binding: ItemListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val musicTrack: TextView = binding.musicTrack
        val musicCollectionName: TextView = binding.musicCollectionName
        val musicAlbum: AppCompatImageView = binding.musicAlbum
        val musicArtist: TextView = binding.musicArtist
    }
}

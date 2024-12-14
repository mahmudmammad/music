package com.example.musicalquiz.ui.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication7.R

class PlaylistAdapter(val playlists: MutableList<String>) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int = playlists.size

    fun addPlaylist(playlistName: String) {
        playlists.add(playlistName)
        notifyItemInserted(playlists.size - 1)
    }

    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val playlistNameTextView: TextView =
            itemView.findViewById(R.id.playlistNameTextView)

        fun bind(name: String) {
            playlistNameTextView.text = name
        }
    }
}


package com.example.myapplication7.ui.search

import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication7.R
import com.example.myapplication7.data.model.Data

class TrackAdapter(
    private val tracks: MutableList<Data>, // Tracks list
    private val onTrackClick: (Data) -> Unit, // Callback for playing track
    private val onDeleteClick: (Data) -> Unit,
    private val isPlaylist: Boolean,// Callback for deleting track
    private val onLongClick: ((Data) -> Unit)? = null // Optional callback for long press
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    private var mediaPlayer: MediaPlayer? = null

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackTitle: TextView = itemView.findViewById(R.id.trackTitle)
        private val albumCover: ImageView = itemView.findViewById(R.id.albumCover)
        private val playButton: ImageButton = itemView.findViewById(R.id.playButton)
        private val stopButton: ImageButton = itemView.findViewById(R.id.stopButton)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)

        fun bind(track: Data) {
            trackTitle.text = track.title

            // Load album cover using Glide
            Glide.with(itemView.context)
                .load(track.album.cover)
                .placeholder(R.drawable.sharp_artist_24)
                .error(R.drawable.baseline_adjust_24)
                .into(albumCover)

            // Set button visibility based on the flag
            deleteButton.visibility = if (isPlaylist) View.VISIBLE else View.GONE

            // Set up play button click
            playButton.setOnClickListener {
                playTrack(track)
                onTrackClick(track)
            }

            // Set up stop button click
            stopButton.setOnClickListener {
                stopTrack()
            }

            // Set up delete button click
            deleteButton.setOnClickListener {
                onDeleteClick(track)
            }

            // Set up optional long press listener
            onLongClick?.let { longClick ->
                itemView.setOnLongClickListener {
                    longClick(track)
                    true
                }
            } ?: itemView.setOnLongClickListener(null)
        }

        private fun playTrack(track: Data) {
            stopTrack()

            mediaPlayer = MediaPlayer().apply {
                setDataSource(track.preview)
                prepare()
                start()
            }

            mediaPlayer?.setOnCompletionListener {
                it.release()
                mediaPlayer = null
            }
        }

        private fun stopTrack() {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int = tracks.size

    // Remove track and notify adapter
    fun removeTrack(track: Data) {
        val position = tracks.indexOf(track)
        if (position != -1) {
            tracks.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}

package com.example.myapplication7.ui.search

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication7.R
import com.example.myapplication7.data.model.Data

class TrackAdapter(private val tracks: List<Data>) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.trackTitle.text = track.title

        // Load album cover using Glide
        Glide.with(holder.itemView.context)
            .load(track.album.cover_medium) // Use the appropriate image size from the API response
            .placeholder(R.drawable.twotone_album_24) // Optional: Add a placeholder image
            .error(R.drawable.sharp_artist_24) // Optional: Add an error image
            .into(holder.albumCover)

        holder.playButton.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null

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

        holder.stopButton.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    override fun getItemCount(): Int = tracks.size

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackTitle: TextView = itemView.findViewById(R.id.trackTitle)
        val albumCover: ImageView = itemView.findViewById(R.id.albumCover)
        val playButton: Button = itemView.findViewById(R.id.playButton)
        val stopButton: Button = itemView.findViewById(R.id.stopButton)
    }
}

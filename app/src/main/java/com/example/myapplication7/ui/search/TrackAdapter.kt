package com.example.myapplication7.ui.search



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication7.R
import com.example.myapplication7.data.model.Data

class TrackAdapter(private var tracks: List<Data>) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackTitle: TextView = itemView.findViewById(R.id.trackTitle)
        private val trackArtist: TextView = itemView.findViewById(R.id.trackArtist)
        private val albumCover: ImageView = itemView.findViewById(R.id.albumCover)

        fun bind(track: Data) {
            trackTitle.text = track.title
            trackArtist.text = track.artist.name
            Glide.with(itemView.context).load(track.album.cover).into(albumCover)
        }
    }
}


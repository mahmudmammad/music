package com.example.myapplication7.ui.playlist

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication7.R
import com.example.myapplication7.data.model.Data
import com.example.myapplication7.ui.search.TrackAdapter


class PlaylistDetailFragment : Fragment(R.layout.fragment_playlist_detail) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrackAdapter
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var playlistViewModel: PlaylistViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewTracks)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize ViewModel
        playlistViewModel = ViewModelProvider(requireActivity()).get(PlaylistViewModel::class.java)

        val playlistId = arguments?.getInt("playlistId") ?: return
        val playlist = playlistViewModel.getPlaylistById(playlistId)

        if (playlist != null) {
            adapter = TrackAdapter(
                tracks = playlist.tracks,
                onTrackClick = { track ->
                    // Handle track playback or highlight in UI
                    Toast.makeText(requireContext(), "Playing ${track.title}", Toast.LENGTH_SHORT).show()
                },
                onDeleteClick = { track ->
                    // Handle track deletion
                    playlist.tracks.remove(track)
                    adapter.notifyDataSetChanged() // Ensure UI reflects the change
                    playlistViewModel.updatePlaylist(playlist) // Update ViewModel with changes
                    Toast.makeText(requireContext(), "${track.title} removed from playlist.", Toast.LENGTH_SHORT).show()
                },
                onLongClick = { track ->
                    // Optional: Handle long press (e.g., showing more options)
                    Toast.makeText(requireContext(), "Long press on ${track.title}", Toast.LENGTH_SHORT).show()
                },
                isPlaylist = true // Set true because this is for a playlist
            )
            recyclerView.adapter = adapter
        }


        fun playTrack(track: Data) {
        mediaPlayer?.stop()
        mediaPlayer?.release()

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

     fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}}

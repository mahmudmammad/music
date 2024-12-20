package com.example.myapplication7.ui.search

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication7.R
import com.example.myapplication7.data.model.Data
import com.example.myapplication7.di.RetrofitClient
import com.example.myapplication7.di.DeezerResponse
import com.example.myapplication7.ui.playlist.PlaylistViewModel

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment(R.layout.fragment_music) {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var playlistViewModel: PlaylistViewModel
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrackAdapter
    private var mediaPlayer: MediaPlayer? = null
    private val tracks = mutableListOf<Data>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModels
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        playlistViewModel = ViewModelProvider(requireActivity()).get(PlaylistViewModel::class.java)

        // Initialize views
        searchView = view.findViewById(R.id.searchView)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter
        adapter = TrackAdapter(
            tracks = tracks,
            onTrackClick = { track ->

                Toast.makeText(requireContext(), "Playing ${track.title}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { track ->
                tracks.remove(track)
                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "${track.title} removed from search results.", Toast.LENGTH_SHORT).show()
            },
            onLongClick = { track ->
                addToPlaylist(track) // Handle long press
            },
            isPlaylist = false // Pass 'true' for playlist, 'false' for search
        )
        recyclerView.adapter = adapter

        // Observe ViewModel data
        observeViewModel()

        // Set up SearchView listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        searchViewModel.fetchTracks(it) // Trigger ViewModel's fetchTracks
                    } else {
                        Toast.makeText(requireContext(), "Please enter a search term", Toast.LENGTH_SHORT).show()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false // We don't need live search updates here
            }
        })
    }

    private fun observeViewModel() {
        // Observe tracks
        searchViewModel.tracks.observe(viewLifecycleOwner) { fetchedTracks ->
            tracks.clear()
            tracks.addAll(fetchedTracks)
            adapter.notifyDataSetChanged()
        }

        // Observe loading state
        searchViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // Show loading indicator
            } else {
                // Hide loading indicator
            }
        }

        // Observe error state
        searchViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun playTrack(track: Data) {
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

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun addToPlaylist(track: Data) {
        val playlists = playlistViewModel.playlists.value ?: mutableListOf()
        if (playlists.isEmpty()) {
            Toast.makeText(requireContext(), "No playlists available. Create one first.", Toast.LENGTH_SHORT).show()
            return
        }

        val playlistNames = playlists.map { it.name }.toTypedArray()
        AlertDialog.Builder(requireContext())
            .setTitle("Select a Playlist")
            .setItems(playlistNames) { _, which ->
                val selectedPlaylist = playlists[which]
                selectedPlaylist.tracks.add(track)
                playlistViewModel.updatePlaylist(selectedPlaylist)
                Toast.makeText(requireContext(), "${track.title} added to ${selectedPlaylist.name}.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}


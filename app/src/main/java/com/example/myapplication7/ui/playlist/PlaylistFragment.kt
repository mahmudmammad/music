package com.example.myapplication7.ui.playlist

import PlaylistAdapter
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication7.MainActivity
import com.example.myapplication7.R
import com.example.myapplication7.data.model.Playlist
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PlaylistFragment : Fragment(R.layout.fragment_playlist) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlaylistAdapter
    private lateinit var playlistViewModel: PlaylistViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        playlistViewModel = ViewModelProvider(requireActivity()).get(PlaylistViewModel::class.java)

        recyclerView = view.findViewById(R.id.recyclerViewPlaylists)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe playlists and update adapter
        playlistViewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            adapter = PlaylistAdapter(playlists) { playlist ->
                (activity as MainActivity).navigateToPlaylistDetail(playlist.id)
            }
            recyclerView.adapter = adapter
        }

        val createPlaylistButton: FloatingActionButton = view.findViewById(R.id.createPlaylistButton)
        createPlaylistButton.setOnClickListener {
            showCreatePlaylistDialog()
        }
    }

    private fun showCreatePlaylistDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Create New Playlist")

        val input = EditText(requireContext())
        input.hint = "Enter playlist name"
        builder.setView(input)

        builder.setPositiveButton("Create") { _, _ ->
            val playlistName = input.text.toString()
            if (playlistName.isNotBlank()) {
                val newPlaylist = Playlist(
                    id = System.currentTimeMillis().toInt(),
                    name = playlistName,
                    tracks = mutableListOf()
                )
                playlistViewModel.addPlaylist(newPlaylist)
                Toast.makeText(requireContext(), "Playlist '$playlistName' created!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Playlist name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}

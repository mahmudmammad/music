package com.example.musicalquiz.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication7.R
import com.example.myapplication7.ui.playlist.PlaylistViewModel

import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PlaylistFragment : Fragment() {

    private lateinit var adapter: PlaylistAdapter
    private lateinit var recyclerViewPlaylists: RecyclerView
    private lateinit var createPlaylistButton: View

    // Shared ViewModel scoped to the activity
    private val playlistViewModel: PlaylistViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_playlist, container, false)

        recyclerViewPlaylists = rootView.findViewById(R.id.recyclerViewPlaylists)
        createPlaylistButton = rootView.findViewById(R.id.createPlaylistButton)

        adapter = PlaylistAdapter(mutableListOf())
        recyclerViewPlaylists.adapter = adapter

        // Observe ViewModel data
        playlistViewModel.playlists.observe(viewLifecycleOwner, Observer { playlists ->
            adapter.notifyDataSetChanged()
            adapter.playlists.clear()
            adapter.playlists.addAll(playlists)
        })

        createPlaylistButton.setOnClickListener {
            showAddPlaylistDialog()
        }

        return rootView
    }

    private fun showAddPlaylistDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_playlist, null)

        val playlistNameEditText = dialogView.findViewById<EditText>(R.id.playlistNameEditText)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Playlist")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val playlistName = playlistNameEditText.text.toString()
                if (playlistName.isNotBlank()) {
                    playlistViewModel.addPlaylist(playlistName)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

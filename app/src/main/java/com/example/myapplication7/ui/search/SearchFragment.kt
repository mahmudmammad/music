package com.example.myapplication7.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.example.myapplication7.R
import com.example.myapplication7.di.RetrofitClient
import com.example.myapplication7.di.DeezerResponse
import com.example.myapplication7.ui.search.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment(R.layout.fragment_music) {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = view.findViewById(R.id.searchView)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // SearchView listener to listen for text changes
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        fetchTracks(it)
                    } else {
                        Toast.makeText(requireContext(), "Please enter a search term", Toast.LENGTH_SHORT).show()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false // We don't need to handle text changes for live search here
            }
        })
    }

    private fun fetchTracks(query: String) {
        Log.d("SearchFragment", "Fetching data for: $query")

        // Fetch tracks from Deezer API
        RetrofitClient.apiService.searchTracks(query).enqueue(object : Callback<DeezerResponse> {
            override fun onResponse(call: Call<DeezerResponse>, response: Response<DeezerResponse>) {
                if (response.isSuccessful) {
                    val tracks = response.body()?.data ?: emptyList()

                    // Set the adapter to the RecyclerView
                    Log.d("SearchFragment", "Received ${tracks.size} tracks")
                    val adapter = TrackAdapter(tracks)
                    recyclerView.adapter = adapter
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch tracks", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DeezerResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

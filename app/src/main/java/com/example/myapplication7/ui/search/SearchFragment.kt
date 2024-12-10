package com.example.myapplication7.ui.playlist



import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication7.R
import com.example.myapplication7.di.RetrofitClient
import com.example.myapplication7.di.DeezerResponse
import com.example.myapplication7.ui.search.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment(R.layout.fragment_music) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Fetch tracks from Deezer API
        Log.d("SearchFragment", "Fetching data from Deezer")

        fetchTracks("eminem") // Example search term
    }

    private fun fetchTracks(query: String) {
        RetrofitClient.apiService.searchTracks(query).enqueue(object : Callback<DeezerResponse> {
            override fun onResponse(call: Call<DeezerResponse>, response: Response<DeezerResponse>) {
                if (response.isSuccessful) {
                    val tracks = response.body()?.data ?: emptyList()

                    // Set the adapter only after the data is fetched
                    Log.d("SearchFragment", "Received ${tracks.size} tracks")
                    val adapter = TrackAdapter(tracks)
                    view?.findViewById<RecyclerView>(R.id.recyclerView)?.adapter = adapter
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



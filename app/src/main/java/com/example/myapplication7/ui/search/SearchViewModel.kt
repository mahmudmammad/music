package com.example.myapplication7.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication7.data.model.Data
import com.example.myapplication7.di.RetrofitClient
import com.example.myapplication7.di.DeezerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    private val _tracks = MutableLiveData<List<Data>>() // Holds fetched tracks
    val tracks: LiveData<List<Data>> get() = _tracks

    private val _loading = MutableLiveData<Boolean>() // Loading state
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>() // Error message
    val error: LiveData<String?> get() = _error

    // Fetch tracks from Deezer API
    fun fetchTracks(query: String) {
        _loading.value = true
        _error.value = null

        RetrofitClient.apiService.searchTracks(query).enqueue(object : Callback<DeezerResponse> {
            override fun onResponse(call: Call<DeezerResponse>, response: Response<DeezerResponse>) {
                _loading.value = false
                if (response.isSuccessful) {
                    _tracks.value = response.body()?.data ?: emptyList()
                } else {
                    _error.value = "Failed to fetch tracks. Try again later."
                }
            }

            override fun onFailure(call: Call<DeezerResponse>, t: Throwable) {
                _loading.value = false
                _error.value = "Error: ${t.message}"
            }
        })
    }
}

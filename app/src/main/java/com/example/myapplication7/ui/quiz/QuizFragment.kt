package com.example.myapplication7.ui.quiz

import QuizViewModel
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication7.R
import com.example.myapplication7.data.model.Quiz
import com.example.myapplication7.ui.playlist.PlaylistViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class QuizFragment : Fragment(R.layout.fragment_quiz) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: QuizAdapter
    private lateinit var quizViewModel: QuizViewModel
    private lateinit var playlistViewModel: PlaylistViewModel // To fetch playlists

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewQuizzes)
        val createQuizButton: FloatingActionButton = view.findViewById(R.id.createQuizButton)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize ViewModels
        quizViewModel = ViewModelProvider(requireActivity()).get(QuizViewModel::class.java)
        playlistViewModel = ViewModelProvider(requireActivity()).get(PlaylistViewModel::class.java)

        // Observe quizzes and update UI
        quizViewModel.quizzes.observe(viewLifecycleOwner) { quizzes ->
            adapter = QuizAdapter(quizzes) { quiz ->
                showQuizDetails(quiz.id)
            }
            recyclerView.adapter = adapter
        }

        // Create a new quiz
        createQuizButton.setOnClickListener {
            createNewQuiz()
        }
    }

    private fun showQuizDetails(quizId: Int) {
        // Navigate to QuizDetailFragment
        val fragment = QuizDetailFragment().apply {
            arguments = Bundle().apply {
                putInt("quizId", quizId)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun createNewQuiz() {
        val playlists = playlistViewModel.playlists.value ?: return
        if (playlists.isEmpty()) {
            Toast.makeText(requireContext(), "No playlists available. Create one first.", Toast.LENGTH_SHORT).show()
            return
        }

        val playlistNames = playlists.map { it.name }.toTypedArray()
        AlertDialog.Builder(requireContext())
            .setTitle("Select Playlist for Quiz")
            .setItems(playlistNames) { _, which ->
                val selectedPlaylist = playlists[which]
                val quizName = "Quiz for ${selectedPlaylist.name}"
                val newQuiz = Quiz(
                    id = System.currentTimeMillis().toInt(),
                    name = quizName,
                    playlist = selectedPlaylist,
                    isRandom = true
                )
                quizViewModel.addQuiz(newQuiz)
                Toast.makeText(requireContext(), "$quizName created.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

package com.example.myapplication7.ui.quiz

import QuizViewModel
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication7.R
import com.example.myapplication7.data.model.Quiz
import com.example.myapplication7.databinding.FragmentQuizDetailBinding

class QuizDetailFragment : Fragment(R.layout.fragment_quiz_detail) {

    private var _binding: FragmentQuizDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var quizViewModel: QuizViewModel
    private var quiz: Quiz? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = com.example.myapplication7.databinding.FragmentQuizDetailBinding.bind(view)

        quizViewModel = ViewModelProvider(requireActivity()).get(QuizViewModel::class.java)

        val quizId = arguments?.getInt("quizId") ?: return
        quiz = quizViewModel.getQuizById(quizId)

        quiz?.let { quiz ->
            binding.quizName2.text = quiz.name
            binding.playlistName.text = "Associated Playlist: ${quiz.playlist.name}"
            binding.startQuizButton.setOnClickListener {
                startQuiz(quiz)
            }

            binding.backButton.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        } ?: run {
            Toast.makeText(requireContext(), "Quiz not found", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }

    private fun startQuiz(quiz: Quiz) {
        val fragment = QuizPlayFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList("tracks", ArrayList(quiz.playlist.tracks))
                // Note: questionMode is no longer used since we have questionType in each Data object
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

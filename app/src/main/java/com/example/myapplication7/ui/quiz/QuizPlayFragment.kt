package com.example.myapplication7.ui.quiz

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.myapplication7.R
import com.example.myapplication7.data.model.Data

class QuizPlayFragment : Fragment(R.layout.fragment_quiz_play) {

    private var mediaPlayer: MediaPlayer? = null
    private var currentQuestionIndex = 0
    private var tracks: List<Data> = emptyList()
    private var correctAnswers = 0
    private var timer: CountDownTimer? = null

    // Views

    private lateinit var playPreviewButton: Button
    private lateinit var questionText: TextView
    private lateinit var multipleChoiceGroup: RadioGroup
    private lateinit var openEndedAnswer: EditText
    private lateinit var nextQuestionButton: Button
    private lateinit var timerTextView: TextView

    // Question modes and time limits
    private var questionModes: List<String> = listOf("multiple_choice", "fill_in_the_blank", "multiple_choice")
    private var timeLimits: List<Long> = listOf(30000L, 45000L, 30000L)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views

        playPreviewButton = view.findViewById(R.id.playPreviewButton)
        questionText = view.findViewById(R.id.questionText)
        multipleChoiceGroup = view.findViewById(R.id.multipleChoiceGroup)
        openEndedAnswer = view.findViewById(R.id.openEndedAnswer)
        nextQuestionButton = view.findViewById(R.id.nextQuestionButton)
        timerTextView = view.findViewById(R.id.timerTextView)

        // Retrieve tracks from arguments
        tracks = arguments?.getParcelableArrayList("tracks") ?: emptyList()

        if (tracks.isEmpty()) {
            Toast.makeText(requireContext(), "No tracks available for the quiz.", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
            return
        }

        // Handle shorter questionModes and timeLimits lists
        if (questionModes.size < tracks.size) {
            questionModes += List(tracks.size - questionModes.size) { "multiple_choice" }
        }
        if (timeLimits.size < tracks.size) {
            timeLimits += List(tracks.size - timeLimits.size) { 30000L }
        }

        // Set up the first question
        loadQuestion()

        // Handle "Next" button
        nextQuestionButton.setOnClickListener {
            timer?.cancel()

            val track = tracks[currentQuestionIndex]
            val correctSongName = track.title
            val currentMode = questionModes[currentQuestionIndex]

            when (currentMode) {
                "multiple_choice" -> {
                    val selectedOptionId = multipleChoiceGroup.checkedRadioButtonId
                    if (selectedOptionId == -1) {
                        Toast.makeText(requireContext(), "Please select an answer before proceeding", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    val selectedRadioButton = multipleChoiceGroup.findViewById<RadioButton>(selectedOptionId)
                    val selectedAnswer = selectedRadioButton.text.toString()
                    if (selectedAnswer.equals(correctSongName, ignoreCase = true)) {
                        correctAnswers++
                    }
                }

                "fill_in_the_blank" -> {
                    val userAnswer = openEndedAnswer.text?.toString()?.trim().orEmpty()
                    if (userAnswer.equals(correctSongName, ignoreCase = true)) {
                        correctAnswers++
                    }
                }
            }

            goToNextQuestion()
        }
    }

    private fun loadQuestion() {
        val track = tracks[currentQuestionIndex]
        val currentMode = questionModes[currentQuestionIndex]

        playPreviewButton.setOnClickListener {
            playTrackPreview(track.preview)
        }

        // Start the timer for this question
        startTimer(timeLimits[currentQuestionIndex])

        when (currentMode) {
            "multiple_choice" -> {
                questionText.text = "What is the name of this song?"
                multipleChoiceGroup.visibility = View.VISIBLE
                openEndedAnswer.visibility = View.GONE
                loadMultipleChoiceOptions(track)
            }

            "fill_in_the_blank" -> {
                questionText.text = "Fill in the missing letters to guess the song title:"
                multipleChoiceGroup.visibility = View.GONE
                openEndedAnswer.visibility = View.VISIBLE
                openEndedAnswer.text.clear()

                val correctTitle = track.title
                val blankedTitle = createBlanks(correctTitle)
                openEndedAnswer.hint = blankedTitle
            }

            else -> {
                questionText.text = "What is the name of this song?"
                multipleChoiceGroup.visibility = View.VISIBLE
                openEndedAnswer.visibility = View.GONE
                loadMultipleChoiceOptions(track)
            }
        }
    }

    private fun loadMultipleChoiceOptions(correctTrack: Data) {
        multipleChoiceGroup.removeAllViews()

        val options = generateOptions(correctTrack)
        options.forEach { option ->
            val radioButton = RadioButton(requireContext()).apply {
                text = option.title
            }
            multipleChoiceGroup.addView(radioButton)
        }
    }

    private fun generateOptions(correctTrack: Data): List<Data> {
        val shuffledTracks = tracks.shuffled()
        val options = mutableListOf(correctTrack)
        options.addAll(shuffledTracks.filter { it != correctTrack }.take(3))
        return options.shuffled()
    }

    private fun createBlanks(title: String): String {
        val chars = title.toCharArray()
        for (i in chars.indices step 2) {
            if (chars[i].isLetter()) {
                chars[i] = '_'
            }
        }
        return String(chars)
    }

    private fun playTrackPreview(previewUrl: String) {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        mediaPlayer = MediaPlayer().apply {
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener {
                start()
            }
        }
    }

    private fun startTimer(timeLimit: Long) {
        timer?.cancel()
        timer = object : CountDownTimer(timeLimit, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                timerTextView.text = "Time left: $secondsLeft s"
            }

            override fun onFinish() {
                timerTextView.text = "Time's up!"
                goToNextQuestion()
            }
        }.start()
    }

    private fun goToNextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex >= tracks.size) {
            displayScore()
        } else {
            loadQuestion()
        }
    }

    private fun displayScore() {
        val message = "Quiz Completed! You got $correctAnswers out of ${tracks.size} questions correct."
        AlertDialog.Builder(requireContext())
            .setTitle("Quiz Results")
            .setMessage(message)
            .setPositiveButton("OK") { _, _ ->
                parentFragmentManager.popBackStack()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        timer?.cancel()
    }
}

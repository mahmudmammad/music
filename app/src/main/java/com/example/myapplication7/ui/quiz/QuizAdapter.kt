package com.example.myapplication7.ui.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication7.R
import com.example.myapplication7.data.model.Quiz

class QuizAdapter(
    private val quizzes: List<Quiz>,
    private val onClick: (Quiz) -> Unit
) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quiz, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizzes[position]
        holder.bind(quiz)
    }

    override fun getItemCount() = quizzes.size

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val quizName: TextView = itemView.findViewById(R.id.quizName)

        fun bind(quiz: Quiz) {
            quizName.text = quiz.name
            itemView.setOnClickListener { onClick(quiz) }
        }
    }
}

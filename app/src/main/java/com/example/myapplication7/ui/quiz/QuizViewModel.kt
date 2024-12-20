import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication7.data.model.Quiz

class QuizViewModel : ViewModel() {
    private val _quizzes = MutableLiveData<MutableList<Quiz>>(mutableListOf())
    val quizzes: LiveData<MutableList<Quiz>> get() = _quizzes

    fun addQuiz(quiz: Quiz) {
        val currentQuizzes = _quizzes.value ?: mutableListOf()
        currentQuizzes.add(quiz)
        _quizzes.value = currentQuizzes
    }

    fun getQuizById(id: Int): Quiz? {
        return _quizzes.value?.find { it.id == id }
    }
}

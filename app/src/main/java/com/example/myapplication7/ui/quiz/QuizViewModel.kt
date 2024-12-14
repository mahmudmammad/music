import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication7.data.model.Album
import com.example.myapplication7.data.model.Artist
import com.example.myapplication7.data.model.Data
import com.example.myapplication7.data.model.Quiz

class QuizViewModel : ViewModel() {
    private val _quizzes = MutableLiveData<List<Quiz>>()
    val quizzes: LiveData<List<Quiz>> = _quizzes

    private val _currentQuiz = MutableLiveData<Quiz?>()
    val currentQuiz: LiveData<Quiz?> = _currentQuiz

    init {
        // Sample track and quiz data (you can replace these with actual data from the Deezer API)
        val album1 = Album(
            cover = "cover1.jpg",
            cover_big = "cover_big1.jpg",
            cover_medium = "cover_medium1.jpg",
            cover_small = "cover_small1.jpg",
            cover_xl = "cover_xl1.jpg",
            id = 1,
            md5_image = "md5_image1",
            title = "Album 1",
            tracklist = "tracklist1",
            type = "album"
        )
        val artist1 = Artist(
            id = 1,
            link = "link1",
            name = "Artist 1",
            picture = "picture1.jpg",
            picture_big = "picture_big1.jpg",
            picture_medium = "picture_medium1.jpg",
            picture_small = "picture_small1.jpg",
            picture_xl = "picture_xl1.jpg",
            tracklist = "tracklist1",
            type = "artist"
        )

        val track1 = Data(
            album = album1,
            artist = artist1,
            duration = 200,
            explicit_content_cover = 1,
            explicit_content_lyrics = 1,
            explicit_lyrics = true,
            id = 1,
            link = "https://link1.com",
            md5_image = "md5_image1",
            preview = "https://example.com/track1preview.mp3",
            rank = 1,
            readable = true,
            title = "Song 1",
            title_short = "Song 1",
            title_version = "Version 1",
            type = "track"
        )

        val album2 = Album(
            cover = "cover2.jpg",
            cover_big = "cover_big2.jpg",
            cover_medium = "cover_medium2.jpg",
            cover_small = "cover_small2.jpg",
            cover_xl = "cover_xl2.jpg",
            id = 2,
            md5_image = "md5_image2",
            title = "Album 2",
            tracklist = "tracklist2",
            type = "album"
        )
        val artist2 = Artist(
            id = 2,
            link = "link2",
            name = "Artist 2",
            picture = "picture2.jpg",
            picture_big = "picture_big2.jpg",
            picture_medium = "picture_medium2.jpg",
            picture_small = "picture_small2.jpg",
            picture_xl = "picture_xl2.jpg",
            tracklist = "tracklist2",
            type = "artist"
        )

        val track2 = Data(
            album = album2,
            artist = artist2,
            duration = 250,
            explicit_content_cover = 0,
            explicit_content_lyrics = 0,
            explicit_lyrics = false,
            id = 2,
            link = "https://link2.com",
            md5_image = "md5_image2",
            preview = "https://example.com/track2preview.mp3",
            rank = 2,
            readable = true,
            title = "Song 2",
            title_short = "Song 2",
            title_version = "Version 2",
            type = "track"
        )

        _quizzes.value = listOf(
            Quiz("Guess the Song 1", track1, "Song 1"),
            Quiz("Guess the Song 2", track2, "Song 2")
        )
    }

    fun startQuiz(quiz: Quiz) {
        _currentQuiz.value = quiz
    }

    fun checkAnswer(userAnswer: String): Boolean {
        return _currentQuiz.value?.correctAnswer?.equals(userAnswer, true) == true
    }
}

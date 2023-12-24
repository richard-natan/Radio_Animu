package br.com.richardnatan.radioanimu.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.media3.exoplayer.ExoPlayer
import br.com.richardnatan.radioanimu.R
import br.com.richardnatan.radioanimu.model.Music
import br.com.richardnatan.radioanimu.presenter.HomePresenter


class HomeFragment : Fragment() {
    lateinit var exoPlayer: ExoPlayer

    private lateinit var presenter: HomePresenter
    private lateinit var progressBar: ProgressBar
    private lateinit var progressBarHorizontal: ProgressBar
    private lateinit var buttonPlay: ImageView
    lateinit var imageMusic: ImageView
    private lateinit var textMusicName: TextView
    private lateinit var textMusicAuthor: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = HomePresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonPlay = view.findViewById(R.id.button_play)
        imageMusic = view.findViewById(R.id.image_music)
        textMusicName = view.findViewById(R.id.text_music_name)
        textMusicName.setSingleLine()
        textMusicName.isSelected = true
        textMusicAuthor = view.findViewById(R.id.text_music_author)
        progressBar = view.findViewById(R.id.progress_bar)
        progressBarHorizontal = view.findViewById(R.id.progress_bar_horizontal)


        presenter.startRadio()
        presenter.getCurrentMusic()

        buttonPlay.setOnClickListener {
            presenter.switchRadioPlaying()
        }
    }

    fun playStreamMusic(response: ExoPlayer) {
        exoPlayer = response
        exoPlayer.play()
    }

    fun showError(response: String) {
        Toast.makeText(requireContext(), response, Toast.LENGTH_LONG).show()
    }

    fun clearTexts() {
        textMusicName.text = ""
        textMusicAuthor.text = ""
    }

    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    fun changeButtonPlayDrawable(stateResponse: Boolean) {
        buttonPlay.isSelected = stateResponse
    }

    fun updateMusic(response: Music) {
        textMusicName.text = response.name
        textMusicAuthor.text = response.author
    }

    fun updateProgressBar(response: Int) {
        progressBarHorizontal.max = 100
        progressBarHorizontal.progress = response
    }

}
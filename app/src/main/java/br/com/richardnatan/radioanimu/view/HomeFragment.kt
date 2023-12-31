package br.com.richardnatan.radioanimu.view

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.media3.exoplayer.ExoPlayer
import androidx.palette.graphics.Palette
import br.com.richardnatan.radioanimu.R
import br.com.richardnatan.radioanimu.model.AnimuDjResponse
import br.com.richardnatan.radioanimu.model.Music
import br.com.richardnatan.radioanimu.presenter.HomePresenter
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import org.w3c.dom.Text


class HomeFragment : Fragment() {
    lateinit var exoPlayer: ExoPlayer

    private lateinit var presenter: HomePresenter

    private lateinit var mainContainer: LinearLayout
    lateinit var musicDetailsContainer: ConstraintLayout
    lateinit var musicContainer: ConstraintLayout
    lateinit var djContainer: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var progressBarHorizontal: ProgressBar
    private lateinit var buttonPlay: ImageView
    lateinit var imageMusic: ImageView
    private lateinit var textListeners: TextView
    lateinit var imageDj: ImageView
    private lateinit var textDjAnnouncer: TextView
    private lateinit var imageMusicContainer: MaterialCardView
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

        mainContainer = view.findViewById(R.id.main_container)
        musicDetailsContainer = view.findViewById(R.id.music_details_container)
        musicContainer = view.findViewById(R.id.music_container)
        djContainer = view.findViewById(R.id.dj_container)
        imageDj = view.findViewById(R.id.image_dj)
        textDjAnnouncer = view.findViewById(R.id.text_dj_announcer)
        imageMusicContainer = view.findViewById(R.id.image_music_container)
        buttonPlay = view.findViewById(R.id.button_play)
        imageMusic = view.findViewById(R.id.image_music)
        textListeners = view.findViewById(R.id.text_listeners)
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

    fun updateListeners(response: Int) {
        textListeners.text = "Ouvintes: $response"
    }

    fun updateMusicImage(response: Bitmap) {
        imageMusic.setImageBitmap(response)
    }

    fun updateDj(response: AnimuDjResponse) {
        textDjAnnouncer.text = response.announcer
    }

    fun updateUiColors(response: Palette) {
        mainContainer.setBackgroundColor(response.lightVibrantSwatch?.rgb ?: R.color.green)
        imageMusicContainer.strokeColor = ColorUtils.setAlphaComponent(response.darkVibrantSwatch?.rgb ?: response.darkMutedSwatch?.rgb ?: resources.getColor(R.color.purple), 255 )
        musicContainer.setBackgroundColor(response.darkVibrantSwatch?.rgb ?: R.color.purple)
        textListeners.setBackgroundColor(response.darkVibrantSwatch?.rgb ?: R.color.purple)
        buttonPlay.setColorFilter(response.darkVibrantSwatch?.rgb ?: R.color.green)
    }

    fun updateProgressBar(response: Int) {
        progressBarHorizontal.max = 100
        progressBarHorizontal.progress = response
    }

}
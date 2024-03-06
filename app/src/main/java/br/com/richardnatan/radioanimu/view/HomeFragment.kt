package br.com.richardnatan.radioanimu.view

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.media3.exoplayer.ExoPlayer
import androidx.palette.graphics.Palette
import br.com.richardnatan.radioanimu.R
import br.com.richardnatan.radioanimu.databinding.FragmentHomeBinding
import br.com.richardnatan.radioanimu.model.AnimuDjResponse
import br.com.richardnatan.radioanimu.model.Music
import br.com.richardnatan.radioanimu.presenter.HomePresenter


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var exoPlayer: ExoPlayer
    private lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = HomePresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textMusicName.setSingleLine()
        binding.textMusicName.isSelected = true

        presenter.startRadio()
        presenter.getCurrentMusic()

        binding.buttonPlay.setOnClickListener {
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
        binding.textMusicName.text = ""
        binding.textMusicAuthor.text = ""
    }

    fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    fun changeButtonPlayDrawable(stateResponse: Boolean) {
        binding.buttonPlay.isSelected = stateResponse
    }

    fun updateMusic(response: Music) {
        binding.textMusicName.text = response.name
        binding.textMusicAuthor.text = response.author
    }

    fun updateListeners(response: Int) {
        binding.textListeners.text = "Ouvintes: $response"
    }

    fun updateMusicImage(response: Bitmap) {
        binding.imageMusic.setImageBitmap(response)
    }

    fun updateMusicImage(response: Int) {
        binding.imageMusic.setImageResource(response)
    }

    fun updateDj(response: AnimuDjResponse) {
        binding.textDjAnnouncer.text = response.announcer
    }

    fun updateDjImageBitmap(response: Bitmap) {
        binding.imageDj.setImageBitmap(response)
    }

    fun updateDjImage(response: Int) {
        binding.imageDj.setImageResource(response)
    }

    fun toggleDjVisibility(visible: Boolean) {
        if (visible) {
            binding.musicDetailsContainer.visibility = View.INVISIBLE
            binding.djContainer.visibility = View.VISIBLE
        } else {
            binding.musicDetailsContainer.visibility = View.VISIBLE
            binding.djContainer.visibility = View.INVISIBLE
        }
    }

    fun updateUiColors(response: Palette) {
        binding.mainContainer.setBackgroundColor(response.lightVibrantSwatch?.rgb ?: R.color.green)
        binding.imageMusicContainer.strokeColor = ColorUtils.setAlphaComponent(
            response.darkVibrantSwatch?.rgb ?: response.darkMutedSwatch?.rgb
            ?: resources.getColor(R.color.purple), 255
        )
        binding.musicContainer.setBackgroundColor(response.darkVibrantSwatch?.rgb ?: R.color.purple)
        binding.textListeners.setBackgroundColor(response.darkVibrantSwatch?.rgb ?: R.color.purple)
        binding.buttonPlay.setColorFilter(response.darkVibrantSwatch?.rgb ?: R.color.green)
    }

    fun updateProgressBar(response: Int) {
        binding.progressBarHorizontal.max = 100
        binding.progressBarHorizontal.progress = response
    }

}
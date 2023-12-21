package br.com.richardnatan.radioanimu.presenter

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import br.com.richardnatan.radioanimu.data.MusicCallback
import br.com.richardnatan.radioanimu.data.MusicDataSource
import br.com.richardnatan.radioanimu.data.UnixCallback
import br.com.richardnatan.radioanimu.data.UnixDataSource
import br.com.richardnatan.radioanimu.model.ApiResponse
import br.com.richardnatan.radioanimu.model.Music
import br.com.richardnatan.radioanimu.view.HomeFragment
import java.util.Timer
import kotlin.concurrent.schedule

class HomePresenter(
    private val view: HomeFragment,
    private val dataSource: MusicDataSource = MusicDataSource(),
    private val unixDataSource: UnixDataSource = UnixDataSource()
) : MusicCallback, UnixCallback {

    private var exoPlayer: ExoPlayer = ExoPlayer.Builder(view.requireContext()).build()
    private lateinit var currentMusic: Music

    fun startRadio() {
        view.showProgressBar()
        Log.i("radioState", "radioState: PLAYING")

        view.changeButtonPlayDrawable(true)
        val audioURL = "https://cast.animu.com.br:9079/stream"
        exoPlayer.setMediaItem(MediaItem.fromUri(audioURL))
        exoPlayer.prepare()

        onRadioIsReady(exoPlayer)
        onComplete()
    }

    fun switchRadioPlaying() {
        if (view.exoPlayer.isPlaying) {
            Log.i("radioState", "radioState: PAUSED")

            view.changeButtonPlayDrawable(false)
            view.exoPlayer.pause()
            return
        }

        startRadio()
    }

    // PLAYER
    private fun onRadioIsReady(response: ExoPlayer) {
        view.playStreamMusic(response)
    }

    fun getCurrentMusic() {
        view.showProgressBar()
        dataSource.getCurrentMusic(this)
    }

    private fun retryGetCurrentMusic() {
        dataSource.getCurrentMusic(this)
    }

    // Music DataSource
    override fun onSuccess(response: ApiResponse) {
        val music = Music(
            response.music.name,
            response.music.author,
            response.music.imageUrl,
            response.music.timeStart,
            response.music.duration
        )

        currentMusic = music
        unixDataSource.getCurrentUnix(this)
        view.updateMusic(music)
    }

    private fun calculateMusicTime(timeStart: Long, duration: Long, currentTime: Long) {
        val musicEndTime = timeStart + duration
        val currentTimeInMillis = currentTime * 1000


        val timer = Timer()

        if (musicEndTime - currentTimeInMillis <= 0) {
            retryGetCurrentMusic()
            return
        }

        Log.i("TAG", "calculateMusicTime: INICIOU O TIMER")
        timer.schedule(musicEndTime - currentTimeInMillis) {
            Log.i("TAG", "calculateMusicTime: EXECUTOU O TIMER")
            Handler(Looper.getMainLooper()).post {
                view.clearTexts()
                getCurrentMusic()
            }
        }
    }

    // UNIX DATASOURCE
    override fun onSuccess(response: Long) {
        calculateMusicTime(currentMusic.timeStart!!, currentMusic.duration!!, response)
    }


    override fun onError(response: String) {
        view.showError(response)
    }

    override fun onComplete() {
        view.hideProgressBar()
    }





}
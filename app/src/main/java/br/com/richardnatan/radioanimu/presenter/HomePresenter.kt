package br.com.richardnatan.radioanimu.presenter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.palette.graphics.Palette
import br.com.richardnatan.radioanimu.R
import br.com.richardnatan.radioanimu.data.AnimuDJCallback
import br.com.richardnatan.radioanimu.data.AnimuDjDataSource
import br.com.richardnatan.radioanimu.data.MusicCallback
import br.com.richardnatan.radioanimu.data.MusicDataSource
import br.com.richardnatan.radioanimu.data.UnixCallback
import br.com.richardnatan.radioanimu.data.UnixDataSource
import br.com.richardnatan.radioanimu.model.AnimuApiResponse
import br.com.richardnatan.radioanimu.model.AnimuDjResponse
import br.com.richardnatan.radioanimu.model.Music
import br.com.richardnatan.radioanimu.view.HomeFragment
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.util.Timer
import kotlin.concurrent.schedule

class HomePresenter(
    private val view: HomeFragment,
    private val dataSource: MusicDataSource = MusicDataSource(),
    private val animuDjDataSource: AnimuDjDataSource = AnimuDjDataSource(),
    private val unixDataSource: UnixDataSource = UnixDataSource()
) : MusicCallback, UnixCallback, AnimuDJCallback {

    private var exoPlayer: ExoPlayer = ExoPlayer.Builder(view.requireContext()).build()
    private lateinit var currentMusic: Music
    private var currentMusicTimerIsRunning: Boolean = false
    private var progressBarTimerIsRunning: Boolean = false
    private var containerLoopIsRunning: Boolean = false
    private var lastCurrentTime: Long = 0

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

        getCurrentMusic()
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

    private fun getCurrentDj() {
        animuDjDataSource.getCurrentDj(this)
    }

    private fun getCurrentMusicImage(url: String?) {
        val target = object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                if (bitmap == null) {
                    view.updateMusicImage(R.drawable.animu_other_logo)
                    Log.i("getCurrentMusicImage", "onBitmapLoaded: IMAGE NOT FOUND")
                } else {
                    Log.i("getCurrentMusicImage", "onBitmapLoaded: IMAGE FOUND")
                    view.updateMusicImage(bitmap)
                    getMusicImagePalette(bitmap)
                }
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                retryGetCurrentMusic()
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

            }

        }
        Picasso.get().load(url).into(target)
    }

    private fun getMusicImagePalette(image: Bitmap) {
        Log.i("getMusicImagePalette", "onBitmapLoaded: GETTING PALETTE")
        val palette = Palette.from(image).generate()

        view.updateUiColors(palette)
    }

    private fun getCurrentUnix() {
        unixDataSource.getCurrentUnix(this)
    }

    private fun retryGetCurrentMusic() {
        dataSource.getCurrentMusic(this)
    }

    // Music DataSource
    override fun onSuccess(response: AnimuApiResponse) {
        val music = Music(
            response.music.name,
            response.music.author,
            response.music.artworks,
            response.music.timeStart,
            response.music.duration
        )
        currentMusic = music

        getCurrentMusicImage(music.artworks?.medium)
        getCurrentUnix()
        getCurrentDj()

        view.updateListeners(response.listeners)
        view.updateMusic(music)
    }

    private fun calculateMusicTime(timeStart: Long, duration: Long, currentTime: Long) {
        if (!currentMusicTimerIsRunning) {
            val musicEndTime = timeStart + duration
            val currentTimeInMillis = currentTime * 1000
            val timeEnd = musicEndTime - currentTimeInMillis
            lastCurrentTime = currentTimeInMillis

            if (timeEnd <= 0) {
                retryGetCurrentMusic()
                return
            }

            val timer = Timer()
            Log.i("calculateMusicTime", "START TIMER")
            currentMusicTimerIsRunning = true
            timer.schedule(timeEnd) {
                Log.i("calculateMusicTime", "TIMER DONE")
                currentMusicTimerIsRunning = false
                Handler(Looper.getMainLooper()).post {
                    view.clearTexts()
                    getCurrentMusic()
                }
            }
        }
    }

    private fun progressBarPercent(currentTime: Long) {
        progressBarTimerIsRunning = true
        val musicElapsedTime = currentTime - (currentMusic.timeStart ?: 0)
        val percent = (musicElapsedTime.toFloat() / (currentMusic.duration ?: 0)) * 100

        val timer = Timer()
        timer.schedule(2000) {
            Log.i("progressBarPercent", "progressBarPercent: PROGRESSBAR UPDATE")
            Handler(Looper.getMainLooper()).post {
                view.updateProgressBar(percent.toInt())
                lastCurrentTime += 2000
                progressBarPercent(lastCurrentTime)
            }
        }


    }

    // DJ DATASOURCE
    override fun onSuccess(response: AnimuDjResponse) {
        view.updateDj(response)

        if (response.announcer != "Haruka Yuki") {
            val target = object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    view.updateDjImageBitmap(bitmap!!)
                }

                override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                    view.updateDjImage(R.drawable.haru_chan_operator_dj)
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }

            }

            Picasso.get().load(response.image).into(target)
        } else {
            view.updateDjImage(R.drawable.haru_chan_operator_dj)
        }

        if (!containerLoopIsRunning) {
            startContainerLoop(20000, false)
        }
    }

    private fun startContainerLoop(delay: Long, lastVisible: Boolean) {
        containerLoopIsRunning = true

        val timer = Timer()
        timer.schedule(delay) {
            Log.i("progressBarPercent", "progressBarPercent: PROGRESSBAR UPDATE")
            Handler(Looper.getMainLooper()).post {
                if (!lastVisible) {
                    // DJ VISIBLE
                    view.toggleDjVisibility(true)
                    startContainerLoop(5000, true)
                } else {
                    // MUSIC DETAIL VISIBLE
                    view.toggleDjVisibility(false)
                    startContainerLoop(20000, false)
                }
            }
        }
    }

    // UNIX DATASOURCE
    override fun onSuccess(response: Long) {
        calculateMusicTime(currentMusic.timeStart!!, currentMusic.duration!!, response)

        if (!progressBarTimerIsRunning) {
            progressBarPercent(lastCurrentTime)
        }
    }


    override fun onError(response: String) {
        view.showError(response)
    }

    override fun onComplete() {
        view.hideProgressBar()
    }


}
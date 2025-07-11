package com.impulse.playlite

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class MainActivity : AppCompatActivity() {
    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private var playbackPosition: Long = 0L
    private var playWhenReady: Boolean = true
    private var currentMediaUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        playerView = findViewById(R.id.player_view)
        currentMediaUri = intent.getStringExtra("video_uri")
        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong("playback_position", 0L)
            playWhenReady = savedInstanceState.getBoolean("play_when_ready", true)
            currentMediaUri = savedInstanceState.getString("current_media_uri", currentMediaUri)
        }
        initializePlayer()
        hideSystemUI()
    }

    private fun initializePlayer() {
        if (player == null) {
            player = ExoPlayer.Builder(this).build()
            playerView.player = player
            val mediaItem = if (currentMediaUri != null) {
                MediaItem.fromUri(Uri.parse(currentMediaUri))
            } else {
                MediaItem.fromUri("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4")
            }
            player?.setMediaItem(mediaItem)
            player?.prepare()
            player?.seekTo(playbackPosition)
            player?.playWhenReady = playWhenReady
        } else {
            playerView.player = player
            player?.seekTo(playbackPosition)
            player?.playWhenReady = playWhenReady
        }
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        )
        actionBar?.hide()
        supportActionBar?.hide()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save playback position and play state
        player?.let {
            outState.putLong("playback_position", it.currentPosition)
            outState.putBoolean("play_when_ready", it.playWhenReady)
        }
        outState.putString("current_media_uri", currentMediaUri)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Restore playback position and play state
        playbackPosition = savedInstanceState.getLong("playback_position", 0L)
        playWhenReady = savedInstanceState.getBoolean("play_when_ready", true)
        currentMediaUri = savedInstanceState.getString("current_media_uri", currentMediaUri)
    }

    override fun onPause() {
        // Save playback position and play state before pausing
        player?.let {
            playbackPosition = it.currentPosition
            playWhenReady = it.playWhenReady
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        initializePlayer()
        hideSystemUI()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }
}
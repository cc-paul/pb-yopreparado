package com.ajv.yopreparado

import android.annotation.SuppressLint
import android.net.Uri
import android.net.Uri.parse
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajv.yopreparado.helper.sharedHelper
import com.ajv.yopreparado.recycleview.doAdapter
import com.ajv.yopreparado.recycleview.doItem
import com.ajv.yopreparado.recycleview.eventMenuItem
import com.ajv.yopreparado.recycleview.notificationAdapter
import com.ajv.yopreparado.recycleview.notificationItem
import com.ajv.yopreparado.services.restApiService
import com.ajv.yopreparado.services.utils
import com.bumptech.glide.Glide
import com.google.firebase.installations.Utils

class VideoPlayerMain : AppCompatActivity() {
    private lateinit var tvVideoTitle: TextView
    private lateinit var imgCancel: ImageView
    private lateinit var videoView: VideoView
    private lateinit var imgNoVideo: ImageView

    val utils = utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_video_player_main)

        tvVideoTitle = findViewById(R.id.tvVideoTitle)
        imgCancel = findViewById(R.id.imgCancel)
        videoView = findViewById(R.id.videoView)
        imgNoVideo = findViewById(R.id.imgNoVideo)

        tvVideoTitle.text = sharedHelper.getString("selected_title")

        utils.showProgress(this)
        playVideo(sharedHelper.getString("selected_video"))

        imgCancel.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun playVideo(url: String) {
        if (url == "" || url.contains("no-video")) {
            imgNoVideo.visibility = View.VISIBLE
            videoView.visibility = View.GONE
            return
        }

        val mediaController = MediaController(this)
        val uri: Uri = parse(url)

        videoView.setMediaController(mediaController)
        videoView.setVideoURI(uri)


        videoView.setOnPreparedListener {
            videoView.requestFocus()
            videoView.start()
            utils.closeProgress()
        }

        videoView.setOnErrorListener { _, _, _ ->
            utils.closeProgress()
            imgNoVideo.visibility = View.VISIBLE
            videoView.visibility = View.GONE
            true
        }
    }


}
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
import com.ajv.yopreparado.recycleview.doAdapter
import com.ajv.yopreparado.recycleview.doItem
import com.ajv.yopreparado.recycleview.eventMenuItem
import com.ajv.yopreparado.recycleview.notificationAdapter
import com.ajv.yopreparado.recycleview.notificationItem
import com.ajv.yopreparado.services.restApiService
import com.ajv.yopreparado.services.utils
import com.bumptech.glide.Glide
import com.google.firebase.installations.Utils

class VideoPlayer : AppCompatActivity() {
    private lateinit var videoView: VideoView
    private lateinit var imgNoVideo: ImageView
    private lateinit var tvEventName: TextView
    private lateinit var imgCancel: ImageView
    private lateinit var tvDescription: TextView
    private lateinit var tvOrigin: TextView
    private lateinit var crdDescription: CardView
    private lateinit var crdOrigin: CardView
    private lateinit var crdDescription_item: CardView
    private lateinit var crdOrigin_item: CardView
    private lateinit var imgEventLogo: ImageView
    private lateinit var imgOrigin: ImageView
    private lateinit var imgDescription: ImageView
    private lateinit var rvDo: RecyclerView
    private lateinit var crdDo: CardView
    private lateinit var imgDo: ImageView
    private lateinit var rvDoMar: RecyclerView
    private lateinit var crdDoMar: CardView
    private lateinit var imgDoMar: ImageView

    val utils = utils()
    val playerApiService = restApiService()

    var isDescriptionHidden: Boolean = true
    var isOriginHidden: Boolean = true
    var isDosHidden: Boolean = true
    var isDosMarHidden: Boolean = true

    private var doAdapter: doAdapter? = null
    private var doAdapterMar: doAdapter? = null
    private val doList  = ArrayList<doItem>()
    private val doListMar  = ArrayList<doItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_video_player)

        videoView = findViewById(R.id.videoView)
        imgNoVideo = findViewById(R.id.imgNoVideo)
        tvEventName = findViewById(R.id.tvEventName)
        imgCancel = findViewById(R.id.imgCancel)
        tvDescription = findViewById(R.id.tvDescription)
        tvOrigin = findViewById(R.id.tvOrigin)
        imgEventLogo = findViewById(R.id.imgEventLogo)
        crdDescription = findViewById(R.id.crdDescription)
        crdOrigin_item = findViewById(R.id.crdOrigin_item)
        crdDescription_item = findViewById(R.id.crdDescription_item)
        crdOrigin = findViewById(R.id.crdOrigin)
        imgOrigin = findViewById(R.id.imgOrigin)
        imgDescription = findViewById(R.id.imgDescription)
        rvDo = findViewById(R.id.rvDo)
        crdDo = findViewById(R.id.crdDo)
        imgDo = findViewById(R.id.imgDo)
        rvDoMar = findViewById(R.id.rvDoMar)
        crdDoMar = findViewById(R.id.crdDoMar)
        imgDoMar = findViewById(R.id.imgDoMar)

        val eventID = intent.getIntExtra("eventID",0)

        utils.showProgress(this)

        crdOrigin_item.visibility = View.GONE
        crdDescription_item.visibility = View.GONE
        rvDo.visibility = View.GONE
        rvDoMar.visibility = View.GONE

        playerApiService.getVideo(eventID) { response ->
            utils.closeProgress()

            if (response!!.success) {
                val data = response.data

                if (data.rows_returned != 0) {
                    val data = data.video

                    data.forEach { menu ->
                        playVideo(menu.videolink)
                        tvEventName.text = menu.event
                        tvDescription.text = menu.description
                        tvOrigin.text = menu.origin

                        Glide.with(this)
                            .load(menu.imagelink)
                            .into(imgEventLogo)

                        val rowDo = menu.`do`

                        rowDo.forEach{ currentDo ->
                            if (currentDo.category == "General") {
                                doList.add(
                                    doItem(
                                        currentDo.isDo,
                                        currentDo.details
                                    )
                                )
                            } else {
                                doListMar.add(
                                    doItem(
                                        currentDo.isDo,
                                        currentDo.details
                                    )
                                )
                            }
                        }
                    }

                    doAdapter = doAdapter(doList)
                    rvDo.layoutManager = LinearLayoutManager(this)
                    rvDo.adapter = doAdapter

                    doAdapterMar = doAdapter(doListMar)
                    rvDoMar.layoutManager = LinearLayoutManager(this)
                    rvDoMar.adapter = doAdapterMar
                }
            } else {
                utils.closeProgress()
            }
        }

        imgCancel.setOnClickListener {
            super.onBackPressed()
        }

        crdDescription.setOnClickListener {
            isDescriptionHidden = !isDescriptionHidden
            crdDescription_item.visibility = if (isDescriptionHidden) View.GONE else View.VISIBLE

            Glide.with(this)
                .load(if (isDescriptionHidden) R.drawable.arrow_down else R.drawable.arrow_up)
                .into(imgDescription)
        }

        crdOrigin.setOnClickListener {
            isOriginHidden = !isOriginHidden
            crdOrigin_item.visibility = if (isOriginHidden) View.GONE else View.VISIBLE

            Glide.with(this)
                .load(if (isOriginHidden) R.drawable.arrow_down else R.drawable.arrow_up)
                .into(imgOrigin)
        }

        crdDo.setOnClickListener {
            isDosHidden = !isDosHidden
            rvDo.visibility = if (isDosHidden) View.GONE else View.VISIBLE

            Glide.with(this)
                .load(if (isDosHidden) R.drawable.arrow_down else R.drawable.arrow_up)
                .into(imgDo)
        }

        crdDoMar.setOnClickListener {
            isDosMarHidden = !isDosMarHidden
            rvDoMar.visibility = if (isDosMarHidden) View.GONE else View.VISIBLE

            Glide.with(this)
                .load(if (isDosMarHidden) R.drawable.arrow_down else R.drawable.arrow_up)
                .into(imgDoMar)
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
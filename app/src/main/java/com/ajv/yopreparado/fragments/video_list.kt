package com.ajv.yopreparado.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajv.yopreparado.ProfileActitivity
import com.ajv.yopreparado.R
import com.ajv.yopreparado.VideoPlayerMain
import com.ajv.yopreparado.helper.sharedHelper
import com.ajv.yopreparado.recycleview.eventAdapter
import com.ajv.yopreparado.recycleview.eventAdapterVideo
import com.ajv.yopreparado.recycleview.eventItem
import com.ajv.yopreparado.recycleview.eventMenuAdapter
import com.ajv.yopreparado.recycleview.eventMenuItem
import com.ajv.yopreparado.recycleview.videoListAdapter
import com.ajv.yopreparado.recycleview.videoListItem
import com.ajv.yopreparado.services.restApiService
import com.ajv.yopreparado.services.utils

class video_list(private val profileContext : ProfileActitivity) : Fragment() , ProfileActitivity.IOnBackPressed {
    private lateinit var videoListView: View
    private lateinit var rvEventMenu: RecyclerView
    private lateinit var rvVideoList: RecyclerView
    private lateinit var imgBack: ImageView
    public lateinit var etSearchVideo: EditText
    private lateinit var imgRefresh: ImageView
    private lateinit var tvRecordMessage: TextView
    private lateinit var crdSearch: CardView
    private lateinit var frAudioVideo: FrameLayout

    private val mapAPIService = restApiService()
    private val utils = utils()
    private val menuList  = ArrayList<eventItem>()
    private val videoList = ArrayList<videoListItem>()

    private var eventAdapterVideo: eventAdapterVideo? = null
    private var videoListAdapter: videoListAdapter? = null

    var eventID: Int = 0
    var eventName: String = ""
    var isTyping: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        videoListView = inflater.inflate(R.layout.fragment_video_list, container, false)
        rvEventMenu = videoListView.findViewById(R.id.rvEventMenu)
        rvVideoList = videoListView.findViewById(R.id.rvVideoList)
        imgBack = videoListView.findViewById(R.id.imgBack)
        etSearchVideo = videoListView.findViewById(R.id.etSearchVideo)
        imgRefresh = videoListView.findViewById(R.id.imgRefresh)
        tvRecordMessage = videoListView.findViewById(R.id.tvRecordMessage)
        crdSearch = videoListView.findViewById(R.id.crdSearch)
        frAudioVideo = videoListView.findViewById(R.id.frAudioVideo)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                imgBack.performClick()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        imgBack.setOnClickListener {
            profileContext.goBack()
        }

        loadMenu()
        loadVideos()

        imgRefresh.setOnClickListener {
            etSearchVideo.setText("")
            eventID = 0
            eventName = ""
            loadMenu()
            loadVideos()
        }

        crdSearch.setOnClickListener {
            if (etSearchVideo.text.isEmpty()) {
                utils.showSnackMessage(frAudioVideo,"Nothing to search")
            } else {
                eventID = 0
                eventName = ""
                loadVideos()
            }
        }

        etSearchVideo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
//                eventID = 0
//                eventName = s.toString()
//                loadMenu()
//                loadVideos()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isTyping = true
            }
        })

        return videoListView
    }

    fun loadVideos() {
        if (!utils.hasInternet(requireContext())) {
            utils.showToastMessage(requireContext(), "Please check your internet connection")
            return
        }

        mapAPIService.getVideoList(eventID) { response ->
            if (response!!.success) {
                val data = response.data
                videoList.clear()
                var videoCounter = 0

                if (data.rows_returned != 0) {
                    val data = data.videos

                    data.forEach { video ->
                        if (eventName == video.event || video.title.lowercase().contains(etSearchVideo.text.toString().lowercase())) {
                            videoList.add(
                                videoListItem(
                                    video.videolink,
                                    video.imagelink,
                                    video.title,
                                    video.views.toString(),
                                    video.id
                                )
                            )

                            videoCounter++
                        }
                    }
                }

                var underMessage: String = " under " + if (etSearchVideo.text.isEmpty()) eventName else etSearchVideo.text.toString()
                Log.e("Under Message",etSearchVideo.text.toString() + "" + eventName)

                if (etSearchVideo.text.toString().isEmpty() && eventName.isEmpty()) {
                    underMessage = ""
                }

                tvRecordMessage.text = if (videoCounter == 0) "" else videoCounter.toString() + " " + response.messages[0] + underMessage
            }

            videoListAdapter = videoListAdapter(this,videoList)
            rvVideoList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvVideoList.adapter = videoListAdapter
        }
    }


    fun loadMenu() {
        if (!utils.hasInternet(requireContext())) {
            utils.showToastMessage(requireContext(), "Please check your internet connection")
            return
        }

        mapAPIService.getAllMenu { response ->
            if (response!!.success) {
                val data = response.data

                if (data.rows_returned != 0) {
                    menuList.clear()
                    val data = data.event

                    data.forEach { menu ->
                        menuList.add(
                            eventItem(
                                menu.eventID,
                                menu.link,
                                menu.event,
                                false
                            )
                        )
                    }
                }
            }

            eventAdapterVideo = eventAdapterVideo(this, menuList)
            rvEventMenu.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvEventMenu.adapter = eventAdapterVideo
        }
    }

    fun loadVideo(videoLink: String,title: String,videoID: Int) {
        sharedHelper.putString("selected_title","Now Playing: $title")
        sharedHelper.putString("selected_video",videoLink)

        utils.showProgress(this.profileContext)

        mapAPIService.updateView(videoID) { response ->
            if (response!!.success) {
                activity?.let{
                    val intent = Intent (it, VideoPlayerMain::class.java)
                    it.startActivity(intent)
                }
            } else {
                utils.showSnackMessage(frAudioVideo,response.messages[0])
            }

            utils.closeProgress()
        }
    }

    override fun onBackPressed(): Boolean {
        return true
    }
}
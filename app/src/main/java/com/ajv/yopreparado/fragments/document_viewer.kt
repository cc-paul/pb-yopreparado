package com.ajv.yopreparado.fragments

import android.annotation.SuppressLint
import android.graphics.Picture
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import com.ajv.yopreparado.ProfileActitivity
import com.ajv.yopreparado.R
import com.ajv.yopreparado.helper.sharedHelper

class document_viewer(private val profileContext : ProfileActitivity) : Fragment(),
    ProfileActitivity.IOnBackPressed {

    private lateinit var documentView : View
    private lateinit var imgBack : ImageView
    private lateinit var wbDocument : WebView

    val BASE_URL : String = "https://apps.project4teen.online/yopreparado/pages/documents?docu="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        documentView = inflater.inflate(R.layout.fragment_document_viewer, container, false)

        imgBack = documentView.findViewById(R.id.imgBack)
        wbDocument = documentView.findViewById(R.id.wbDocument)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                imgBack.performClick()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        imgBack.setOnClickListener {
            profileContext.goBack()
        }

        wbDocument.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url!!)
                return true
            }
        }

        wbDocument.apply {
            settings.setLoadWithOverviewMode(true)
            settings.useWideViewPort = true
            settings.setSupportZoom(false)
            settings.javaScriptEnabled = true
            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    if (newProgress == 100) {
                        view?.setInitialScale(0)
                    }
                }
            }

            loadUrl(BASE_URL + sharedHelper.getString("docu"))
        }

        return documentView
    }

    override fun onBackPressed(): Boolean {
        return true
    }
}
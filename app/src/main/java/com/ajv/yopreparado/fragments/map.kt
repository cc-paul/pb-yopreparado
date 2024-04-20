package com.ajv.yopreparado.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajv.yopreparado.MainActivity
import com.ajv.yopreparado.ProfileActitivity
import com.ajv.yopreparado.R
import com.ajv.yopreparado.helper.sharedHelper
import com.ajv.yopreparado.recycleview.eventAdapter
import com.ajv.yopreparado.recycleview.eventItem
import com.ajv.yopreparado.services.restApiService
import com.ajv.yopreparado.services.utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception


class map : Fragment(), OnMapReadyCallback {
    private lateinit var rlMain: RelativeLayout
    private lateinit var mapView: View
    private lateinit var rvEventMenu: RecyclerView
    private lateinit var crdGPS: LinearLayout
    private lateinit var mapViewCavite: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var meMap: GoogleMap
    private lateinit var mainActivity: MainActivity
    private lateinit var etSearchMunicipality : EditText
    private lateinit var crdCall : LinearLayout
    private lateinit var tvInitial : TextView
    private lateinit var imgProfile : ImageView
    private lateinit var crdProfile : CardView
    private lateinit var crdHide : LinearLayout
    private lateinit var lblShowHide : TextView
    private lateinit var imgWeatherIcon : ImageView
    private lateinit var tvTemp : TextView
    private lateinit var tvWeather : TextView
    private lateinit var crdWeather : LinearLayout
    private lateinit var crdWeatherApp : CardView
    private lateinit var crdNotification : LinearLayout
    private lateinit var imgMenu : ImageView

    private val markers: MutableMap<Marker, Map<String, Any>> = HashMap()
    private val eventList = ArrayList<eventItem>()
    private var eventAdapter: eventAdapter? = null
    private val mapAPIService = restApiService()
    private val utils = utils()
    val eventMarkers = ArrayList<Marker>()
    val circleList: MutableList<Circle> = mutableListOf()
    var circle: Circle? = null
    var hideRadius : Boolean = false

    var mainActivityReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                val lat = intent.getDoubleExtra("lat",0.0)
                val lng = intent.getDoubleExtra("lng",0.0)
                val iconUrl = "https://apps.project4teen.online/yopreparado/dist/img/body.png"

                moveCamera(lat,lng)
                removeMarkerByTitle("myself")
                createMapMarker(iconUrl,lat,lng,"myself","","","","","","")
            }
        }
    }

    var municipalityDialogReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                val lat = intent.getStringExtra("lat")!!.toDouble()
                val lng = intent.getStringExtra("lng")!!.toDouble()
                val municipality = intent.getStringExtra("municipality")


                etSearchMunicipality.setText(municipality)
                moveCamera(lat,lng)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mapView = inflater.inflate(R.layout.fragment_map, container, false)

        val localBroadcastManager = LocalBroadcastManager.getInstance(requireContext())

        localBroadcastManager.registerReceiver(mainActivityReceiver, IntentFilter("current_location"))
        localBroadcastManager.registerReceiver(municipalityDialogReceiver, IntentFilter("municipalities"))

        rlMain = mapView.findViewById(R.id.rlMain)
        rvEventMenu = mapView.findViewById(R.id.rvEventMenu)
        crdGPS = mapView.findViewById(R.id.crdGPS)
        mapViewCavite = mapView.findViewById(R.id.mapCavite)
        etSearchMunicipality = mapView.findViewById(R.id.etSearchMunicipality)
        crdCall = mapView.findViewById(R.id.crdCall)
        tvInitial = mapView.findViewById(R.id.tvInitial)
        imgProfile = mapView.findViewById(R.id.imgProfile)
        crdProfile = mapView.findViewById(R.id.crdProfile)
        crdHide = mapView.findViewById(R.id.crdHide)
        lblShowHide = mapView.findViewById(R.id.lblShowHide)
        imgWeatherIcon = mapView.findViewById(R.id.imgWeatherIcon)
        tvTemp = mapView.findViewById(R.id.tvTemp)
        tvWeather = mapView.findViewById(R.id.tvWeather)
        crdWeather = mapView.findViewById(R.id.crdWeather)
        crdWeatherApp = mapView.findViewById(R.id.crdWeatherApp)
        crdNotification = mapView.findViewById(R.id.crdNotification)
        imgMenu = mapView.findViewById(R.id.imgMenu)

        mapViewCavite.onCreate(savedInstanceState)
        mapViewCavite.getMapAsync(this)

        tvInitial.text = sharedHelper.getString("first_name").substring(0,1).uppercase()

        crdGPS.setOnClickListener {
            mainActivity.enablePermissionAngGetLocation()
        }

        etSearchMunicipality.setOnClickListener {
            val fragMan : FragmentManager = requireActivity().supportFragmentManager
            val municipalityListDialog = dialog_municipality()
            municipalityListDialog.show(fragMan,"MunicipalityFragment")
        }

        crdCall.setOnClickListener {
            val fragMan : FragmentManager = requireActivity().supportFragmentManager
            val hotlineListDialog = dialog_hotline()
            hotlineListDialog.show(fragMan,"HotlineFragment")
        }

        crdProfile.setOnClickListener {
            val fragMan : FragmentManager = requireActivity().supportFragmentManager
            val profileDialog = dialog_profile()
            profileDialog.show(fragMan,"SettingsFragment")
        }

        imgMenu.setOnClickListener {
            val fragMan : FragmentManager = requireActivity().supportFragmentManager
            val profileDialog = dialog_profile()
            profileDialog.show(fragMan,"SettingsFragment")
        }

        crdHide.setOnClickListener {
            hideRadius = !hideRadius
            lblShowHide.text = if (!hideRadius) "Show Borders" else "Hide Borders"
            hideCircles()
        }

        crdWeather.setOnClickListener {
            crdWeatherApp.visibility =  View.VISIBLE
            hideWeatherCard()
        }

        crdNotification.setOnClickListener {
            val intent = Intent(activity, ProfileActitivity::class.java)
            intent.putExtra("fragment", "notification")
            startActivity(intent)
        }

        loadWeather()

        return mapView
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        meMap = map
        googleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN

        val location = LatLng(14.4791, 120.8970)
        val cameraPosition = CameraPosition.Builder()
            .target(location)
            .zoom(16f)
            .bearing(45.0f)
            .build()
        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        loadMarkers()
        loadMunicipality()

        if (eventList.size != 0) {
            loadMenu()
        }
    }

    fun hideWeatherCard() {
        GlobalScope.launch {
            delay(2000)
            crdWeatherApp.visibility = View.INVISIBLE
        }
    }

    fun loadWeather() {
        if (!utils.hasInternet(requireContext())) {
            utils.showToastMessage(requireContext(), "Please check your internet connection")
            return
        }

        mapAPIService.getWeather { response ->
            if (response!!.cod == 200) {
                var temp = response.main.temp - 273.15
                var icon = response.weather[0].icon
                var weather = response.weather[0].main

                Glide.with(this)
                    .load("http://openweathermap.org/img/w/${icon}.png")
                    .fitCenter()
                    .into(imgWeatherIcon)

                tvTemp.text = String.format("%.2f", temp).toDouble().toString()
                tvWeather.text = weather
            }
        }
    }

    fun loadMarkers(selectedEventId: Int = 0) {
        if (!utils.hasInternet(requireContext())) {
            utils.showToastMessage(requireContext(), "Please check your internet connection")
            return
        }

        mapAPIService.getAllMarkers { response ->
            if (response!!.success) {
                val data = response.data

                if (data.rows_returned != 0) {
                    val events = data.event

                    if (eventMarkers.size != 0) {
                        for (i in eventMarkers.size - 1 downTo 0) {
                            removeMarkerByTitle(eventMarkers[i].title!!)
                        }
                    }

                    deleteCircles()

                    events.forEach { event ->
                        val eventName: String = event.event
                        val lat: Double = event.lat.toDouble()
                        val lng: Double = event.lng.toDouble()
                        val iconUrl: String = event.link

                        eventList.add(eventItem(event.markerID, iconUrl, eventName))

                        if (selectedEventId == 0 || selectedEventId == event.markerID) {
                            createMapMarker(
                                iconUrl,
                                lat,
                                lng,
                                "event-${event.eventID}",
                                event.event,
                                event.remarks,
                                event.alertLevel,
                                event.passableVehicle,
                                event.dateReported,
                                utils.formatDuration(event.duration)
                            )
                            drawCircleOnMarker(lat,lng,event.radius)
                            hideCircles()
                        }
                    }

                    if (eventList.isNotEmpty() && eventAdapter == null) {
                        loadMenu()
                    }
                }
            }
        }
    }

    fun loadMunicipality() {
        if (!utils.hasInternet(requireContext())) {
            utils.showToastMessage(requireContext(), "Please check your internet connection")
            return
        }

        mapAPIService.getAllMunicipality {response ->
            if (response!!.success) {
                val data = response.data

                if (data.rows_returned != 0) {
                    val municipality = data.municipality

                    municipality.forEach { municipality ->
                        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                        paint.textSize = 25f
                        paint.color = Color.parseColor("#F67E24")
                        paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)

                        val textBounds = Rect()
                        paint.getTextBounds(municipality.barangayName, 0, municipality.barangayName.length, textBounds)

                        val bitmap = Bitmap.createBitmap(textBounds.width(), textBounds.height(), Bitmap.Config.ARGB_8888)
                        val canvas = Canvas(bitmap)
                        canvas.drawText(municipality.barangayName, -textBounds.left.toFloat(), -textBounds.top.toFloat(), paint)

                        val markerOptions = MarkerOptions()
                            .position(LatLng(municipality.lat.toDouble(), municipality.lng.toDouble()))
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))

                        googleMap.addMarker(markerOptions)
                    }
                }
            }
        }
    }

    private fun loadMenu() {
        if (eventList.size == 0) return
        val eventListDistinct: ArrayList<eventItem> = eventList.distinct() as ArrayList<eventItem>
        eventAdapter = eventAdapter(this, eventListDistinct)
        rvEventMenu.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvEventMenu.adapter = eventAdapter
    }

    fun moveCamera(lat: Double, lng: Double) {
        val newLatLng = LatLng(lat, lng)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(newLatLng, 16f)
        googleMap?.animateCamera(cameraUpdate)

        Log.e("Current Lat Long","{$lat}------{$lng}")
    }

    fun createMapMarker(
        iconUrl : String,
        lat : Double,
        lng : Double,
        title : String,
        eventType: String,
        remarks: String,
        alertLevel: String,
        passableVehicle: String,
        dateReported: String,
        duration: String
    ) {
        Glide.with(requireContext())
            .asBitmap()
            .load(iconUrl)
            .apply(RequestOptions().override(100, 100))
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resource)

                    val markerOptions = MarkerOptions()
                        .position(LatLng(lat, lng))
                        .icon(bitmapDescriptor)
                        .title(title)
                    val marker = googleMap?.addMarker(markerOptions)

                    marker?.let {
                        val markerData: MutableMap<String, Any> = HashMap()
                        markerData["eventType"] = eventType
                        markerData["alertLevel"] = alertLevel
                        markerData["passableVehicle"] = passableVehicle
                        markerData["dateReported"] = dateReported
                        markerData["duration"] = duration
                        markerData["remarks"] = remarks

                        markers[it] = markerData
                    }

                    marker?.let { marker ->
                        marker.tag = title

                        googleMap?.setOnMarkerClickListener { clickedMarker ->
                            var clickedMarkerTitle = clickedMarker.tag as? String

                            if (clickedMarkerTitle != title) {
                                clickedMarker.hideInfoWindow()
                            }

                            try {
                                val dataModel = markers[clickedMarker]

                                val message = "" +
                                        "Event: ${dataModel!!["eventType"] }" +
                                        "\n" +
                                        "Alert Level: ${dataModel!!["alertLevel"] }" +
                                        "\n" +
                                        "Passable Vehicle: ${dataModel!!["passableVehicle"] }" +
                                        "\n" +
                                        "Date Reported: ${dataModel!!["dateReported"] }" +
                                        "\n" +
                                        "Duration: ${dataModel!!["duration"] }" +
                                        "\n" +
                                        "\n" +
                                        "Remarks: ${dataModel!!["remarks"] }"

                                utils.showSnackMessage(rlMain,message)

                                println("Marker Tag : ${marker.title}")
                            } catch (e: Exception) {

                            }

                            true
                        }
                    }

                    if (!title.equals("myself")) {
                        eventMarkers.add(marker!!)
                    }
                }
            })
    }

    fun drawCircleOnMarker(lat: Double,lng: Double,radius: Double) {
        val centerLatLng = LatLng(lat, lng)
        val circleOptions = CircleOptions()
            .center(centerLatLng)
            .radius(radius)
            .strokeWidth(2f)
            .strokeColor(Color.RED)
            .fillColor(Color.parseColor("#40FF0000"))

        val circle = googleMap?.addCircle(circleOptions)
        circle?.let { circleList.add(it) }
    }

    fun deleteCircles() {
        for (circle in circleList) {
            circle.remove()
        }
        circleList.clear()
    }

    fun hideCircles() {
        for (circle in circleList) {
            circle.isVisible = hideRadius
        }
    }

    fun removeMarkerByTitle(title: String) {
        if (circle != null) {
            circle?.remove()
        }

        for (i in eventMarkers.size - 1 downTo 0) {
            if (eventMarkers[i].title.equals(title)) {
                println("Removed Marker Title : $title | Array Current Size : ${eventMarkers.size}")

                eventMarkers[i].remove()
                eventMarkers.removeAt(i)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        activity?.window?.statusBarColor = Color.TRANSPARENT

        mainActivity = requireActivity() as MainActivity
    }

    override fun onDetach() {
        super.onDetach()
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
    }

    override fun onResume() {
        val imageLink : String = sharedHelper.getString("image_link")

        if (imageLink.equals("-")) {
            tvInitial.visibility = View.VISIBLE
            imgProfile.visibility = View.GONE
        } else {
            tvInitial.visibility = View.GONE
            imgProfile.visibility = View.VISIBLE

            Glide.with(this)
                .load(Uri.parse(imageLink))
                .into(imgProfile)
        }

        mapViewCavite.onResume()
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapViewCavite.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapViewCavite.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapViewCavite.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapViewCavite.onDestroy()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mainActivityReceiver)
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(municipalityDialogReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapViewCavite.onLowMemory()
    }
}
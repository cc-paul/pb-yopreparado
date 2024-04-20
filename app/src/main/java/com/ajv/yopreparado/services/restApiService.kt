package com.ajv.yopreparado.services

import android.util.Log
import com.ajv.yopreparado.data.FileResponse
import com.ajv.yopreparado.data.emailResponse
import com.ajv.yopreparado.data.faqResponse
import com.ajv.yopreparado.data.hotlineDataResponse
import com.ajv.yopreparado.data.loginData
import com.ajv.yopreparado.data.loginResponse
import com.ajv.yopreparado.data.mapDataResponse
import com.ajv.yopreparado.data.municipalityDataResponse
import com.ajv.yopreparado.data.notificationResponse
import com.ajv.yopreparado.data.passwordData
import com.ajv.yopreparado.data.passwordDataResponse
import com.ajv.yopreparado.data.passwordResetResponse
import com.ajv.yopreparado.data.profileData
import com.ajv.yopreparado.data.profileDataResponse
import com.ajv.yopreparado.data.registrationData
import com.ajv.yopreparado.data.registrationResponse
import com.ajv.yopreparado.data.videoListResponse
import com.ajv.yopreparado.data.videoResponse
import com.ajv.yopreparado.data.viewResponse
import com.ajv.yopreparado.data.weatherDataResponse
import com.ajv.yopreparado.helper.retrofitHelper
import com.ajv.yopreparado.helper.sharedHelper
import com.ajv.yopreparado.interfaces.changePasswordAPI
import com.ajv.yopreparado.interfaces.changeProfileAPI
import com.ajv.yopreparado.interfaces.emailSendAPI
import com.ajv.yopreparado.interfaces.getAllMarkersAPI
import com.ajv.yopreparado.interfaces.getAllMenuAPI
import com.ajv.yopreparado.interfaces.getAllMunicipalityAPI
import com.ajv.yopreparado.interfaces.getAllNotificationAPI
import com.ajv.yopreparado.interfaces.getFAQListAPI
import com.ajv.yopreparado.interfaces.getFilesListAPI
import com.ajv.yopreparado.interfaces.getHotlineAPI
import com.ajv.yopreparado.interfaces.getVideoDetailsAPI
import com.ajv.yopreparado.interfaces.getVideoListAPI
import com.ajv.yopreparado.interfaces.getWeatherAPI
import com.ajv.yopreparado.interfaces.loginAPI
import com.ajv.yopreparado.interfaces.registrationAPI
import com.ajv.yopreparado.interfaces.resetPasswordAPI
import com.ajv.yopreparado.interfaces.updateViewAPI
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class restApiService {
    fun addUser(userData: registrationData, onResult: (registrationResponse?) -> Unit) {
        val retrofit = retrofitHelper.buildService(registrationAPI::class.java)

        retrofit.saveAccount(userData).enqueue(
            object : Callback<registrationResponse> {
                override fun onFailure(call: Call<registrationResponse>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<registrationResponse>, response: Response<registrationResponse>) {
                    var addedUser = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        addedUser = gson.fromJson(response.errorBody()!!.string(), registrationResponse::class.java)
                    }

                    onResult(addedUser)
                }
            }
        )
    }

    fun loginUser(loginData: loginData, onResult: (loginResponse?) -> Unit) {
        val retrofit = retrofitHelper.buildService(loginAPI::class.java)

        retrofit.loginAccount(loginData).enqueue(
            object : Callback<loginResponse> {
                override fun onFailure(call: Call<loginResponse>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(call: Call<loginResponse>, response: Response<loginResponse>) {
                    var loginUser = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        loginUser = gson.fromJson(response.errorBody()!!.string(), loginResponse::class.java)
                    }

                    onResult(loginUser)
                }
            }
        )
    }

    fun resetPassword(email : String,onResult: (passwordResetResponse?) -> Unit) {
        val retrofit = retrofitHelper.buildService(resetPasswordAPI::class.java)

        retrofit.resetPassword(email).enqueue(
            object : Callback<passwordResetResponse> {
                override fun onFailure(call: Call<passwordResetResponse>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(
                    call: Call<passwordResetResponse>,
                    response: Response<passwordResetResponse>
                ) {
                    var resetPassword = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        resetPassword = gson.fromJson(response.errorBody()!!.string(),passwordResetResponse::class.java)
                    }

                    onResult(resetPassword)
                }
            }
        )
    }

    fun sendEmail(rEmail : String,sSubject : String,sBody : String,onResult: (emailResponse?) -> Unit) {
        val retrofit = retrofitHelper.buildService(emailSendAPI::class.java)

        retrofit.sendEmail(rEmail,sSubject,sBody).enqueue(
            object : Callback<emailResponse> {
                override fun onFailure(call: Call<emailResponse>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(
                    call: Call<emailResponse>,
                    response: Response<emailResponse>
                ) {
                    var emailSend = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        emailSend = gson.fromJson(response.errorBody()!!.string(),emailResponse::class.java)
                    }

                    onResult(emailSend)
                }
            }
        )
    }

    fun getAllMarkers(onResult: (mapDataResponse?) -> Unit) {
        val retrofit = retrofitHelper.buildService(getAllMarkersAPI::class.java)

        retrofit.getAllMarkers().enqueue(
            object : Callback<mapDataResponse> {
                override fun onFailure(call: Call<mapDataResponse>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(
                    call: Call<mapDataResponse>,
                    response: Response<mapDataResponse>
                ) {
                    var markers = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        markers = gson.fromJson(response.errorBody()!!.string(),mapDataResponse::class.java)
                    }

                    //Log.e("Markers Response",response.body().toString())
                    onResult(markers)
                }
            }
        )
    }

    fun getAllMenu(onResult: (mapDataResponse?) -> Unit) {
        val retrofit = retrofitHelper.buildService(getAllMenuAPI::class.java)

        retrofit.getAllMenu().enqueue(
            object : Callback<mapDataResponse> {
                override fun onFailure(call: Call<mapDataResponse>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(
                    call: Call<mapDataResponse>,
                    response: Response<mapDataResponse>
                ) {
                    var menu = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        menu = gson.fromJson(response.errorBody()!!.string(),mapDataResponse::class.java)
                    }

                    //Log.e("Markers Response",response.body().toString())
                    onResult(menu)
                }
            }
        )
    }

    fun getAllMunicipality(onResult: (municipalityDataResponse?) -> Unit) {
        val retrofit = retrofitHelper.buildService(getAllMunicipalityAPI::class.java)

        retrofit.getAllMunicipality().enqueue(
            object : Callback<municipalityDataResponse> {
                override fun onFailure(call: Call<municipalityDataResponse>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(
                    call: Call<municipalityDataResponse>,
                    response: Response<municipalityDataResponse>
                ) {
                    var municipality = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        municipality = gson.fromJson(response.errorBody()!!.string(),municipalityDataResponse::class.java)
                    }

                    onResult(municipality)
                }
            }
        )
    }

    fun getAllHotline (onResult: (hotlineDataResponse?) -> Unit) {
        val retrofit = retrofitHelper.buildService(getHotlineAPI::class.java)

        retrofit.getHotline().enqueue(
            object : Callback<hotlineDataResponse> {
                override fun onFailure(call: Call<hotlineDataResponse>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(
                    call: Call<hotlineDataResponse>,
                    response: Response<hotlineDataResponse>
                ) {
                    var hotline = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        hotline = gson.fromJson(response.errorBody()!!.string(),hotlineDataResponse::class.java)
                    }

                    onResult(hotline)
                }
            }
        )
    }

    fun getAllNotification (onResult: (notificationResponse?) -> Unit) {
        val retrofit = retrofitHelper.buildService(getAllNotificationAPI::class.java)

        retrofit.getNotification().enqueue(
            object : Callback<notificationResponse> {
                override fun onFailure(call: Call<notificationResponse>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(
                    call: Call<notificationResponse>,
                    response: Response<notificationResponse>
                ) {
                    var notificaiont = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        notificaiont = gson.fromJson(response.errorBody()!!.string(),notificationResponse::class.java)
                    }

                    onResult(notificaiont)
                }
            }
        )
    }

    fun changePassword (passwordData: passwordData ,onResult: (passwordDataResponse?) -> Unit) {
        val retrofit = retrofitHelper.buildService(changePasswordAPI::class.java)

        retrofit.changePassword(sharedHelper.getString("access_token"),passwordData).enqueue(
            object : Callback<passwordDataResponse> {
                override fun onFailure(call: Call<passwordDataResponse>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(
                    call: Call<passwordDataResponse>,
                    response: Response<passwordDataResponse>
                ) {
                    var changePassword = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        changePassword = gson.fromJson(response.errorBody()!!.string(),passwordDataResponse::class.java)
                    }

                    onResult(changePassword)
                }
            }
        )
    }

    fun changeProfile (profileData: profileData, onResult: (profileDataResponse?) -> Unit) {
        val retrofit = retrofitHelper.buildService(changeProfileAPI::class.java)

        retrofit.changeProfile(sharedHelper.getString("access_token"),profileData).enqueue(
            object : Callback<profileDataResponse> {
                override fun onFailure(call: Call<profileDataResponse>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(
                    call: Call<profileDataResponse>,
                    response: Response<profileDataResponse>
                ) {
                    var profile = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        profile = gson.fromJson(response.errorBody()!!.string(),profileDataResponse::class.java)
                    }

                    onResult(profile)
                }
            }
        )
    }

    fun getVideo(eventID: Int,onResult: (videoResponse?) -> Unit) {
        val retrofit = retrofitHelper.buildService(getVideoDetailsAPI::class.java)

        retrofit.getVideoDetails(sharedHelper.getString("access_token"),eventID).enqueue(
            object : Callback<videoResponse> {
                override fun onFailure(call: Call<videoResponse>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(
                    call: Call<videoResponse>,
                    response: Response<videoResponse>
                ) {
                    var video = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        video = gson.fromJson(response.errorBody()!!.string(),videoResponse::class.java)
                    }

                    onResult(video)
                }
            }
        )
    }

    fun getVideoList(eventID: Int,onResult: (videoListResponse?) -> Unit) {
        val retrofit = retrofitHelper.buildService(getVideoListAPI::class.java)

        retrofit.getVideoList(sharedHelper.getString("access_token"),eventID).enqueue(
            object : Callback<videoListResponse> {
                override fun onFailure(call: Call<videoListResponse>, t: Throwable) {
                    Log.e("Video Response",t.message.toString())
                    onResult(null)
                }
                override fun onResponse(
                    call: Call<videoListResponse>,
                    response: Response<videoListResponse>
                ) {
                    var videoList = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        videoList = gson.fromJson(response.errorBody()!!.string(),videoListResponse::class.java)
                    }

                    onResult(videoList)
                }
            }
        )
    }

    fun updateView(videoID: Int,onResult: (viewResponse?) -> Unit) {
        val retrofit = retrofitHelper.buildService(updateViewAPI::class.java)

        retrofit.updateVideoView(sharedHelper.getString("access_token"),videoID).enqueue(
            object : Callback<viewResponse> {
                override fun onFailure(call: Call<viewResponse>, t: Throwable) {
                    Log.e("Video Response",t.message.toString())
                    onResult(null)
                }
                override fun onResponse(
                    call: Call<viewResponse>,
                    response: Response<viewResponse>
                ) {
                    var video = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        video = gson.fromJson(response.errorBody()!!.string(),viewResponse::class.java)
                    }

                    onResult(video)
                }
            }
        )
    }

    fun getFAQList(onResult: (faqResponse?) -> Unit) {
        val retrofit = retrofitHelper.buildService(getFAQListAPI::class.java)

        retrofit.getFAQList(sharedHelper.getString("access_token")).enqueue(
            object : Callback<faqResponse> {
                override fun onFailure(call: Call<faqResponse>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(
                    call: Call<faqResponse>,
                    response: Response<faqResponse>
                ) {
                    var faqList = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        faqList = gson.fromJson(response.errorBody()!!.string(),faqResponse::class.java)
                    }

                    onResult(faqList)
                }
            }
        )
    }

    fun getWeather(onResult: (weatherDataResponse?) -> Unit) {
        val retrofit = retrofitHelper.buildService(getWeatherAPI::class.java)

        retrofit.getWeatherData().enqueue(
            object : Callback<weatherDataResponse> {
                override fun onFailure(call: Call<weatherDataResponse>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(
                    call: Call<weatherDataResponse>,
                    response: Response<weatherDataResponse>
                ) {
                    var weatherList = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        weatherList = gson.fromJson(response.errorBody()!!.string(),weatherDataResponse::class.java)
                    }

                    onResult(weatherList)
                }
            }
        )
    }

    fun getAllFiles(onResult: (FileResponse?) -> Unit) {
        val retrofit = retrofitHelper.buildService(getFilesListAPI::class.java)

        retrofit.getFileList(sharedHelper.getString("access_token")).enqueue(
            object : Callback<FileResponse> {
                override fun onFailure(call: Call<FileResponse>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(
                    call: Call<FileResponse>,
                    response: Response<FileResponse>
                ) {
                    var files = response.body()

                    if (!response.isSuccessful) {
                        val gson = Gson()
                        files = gson.fromJson(response.errorBody()!!.string(),FileResponse::class.java)
                    }

                    onResult(files)
                }
            }
        )
    }
}
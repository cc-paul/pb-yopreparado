package com.ajv.yopreparado.interfaces


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
import com.ajv.yopreparado.helper.sharedHelper
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface registrationAPI {
     @Headers("Content-Type: application/json")
     @POST("users")
     fun saveAccount(@Body requestBody: registrationData) : Call<registrationResponse>
}

interface changePasswordAPI {
     @Headers("Content-Type: application/json")
     @PATCH("users")
     fun changePassword(@Header("Authorization") token: String = sharedHelper.getString("access_token"),@Body requestBody: passwordData) : Call<passwordDataResponse>
}

interface changeProfileAPI {
     @Headers("Content-Type: application/json")
     @PATCH("users")
     fun changeProfile(@Header("Authorization") token: String = sharedHelper.getString("access_token"),@Body requestBody: profileData) : Call<profileDataResponse>
}

interface loginAPI {
     @Headers("Content-Type: application/json")
     @POST("sessions")
     fun loginAccount(@Body requestBody: loginData) : Call<loginResponse>
}

interface resetPasswordAPI {
     @GET("users/{email}")
     fun resetPassword(@Path("email") email: String ) : Call<passwordResetResponse>
}

interface emailSendAPI {
     @FormUrlEncoded
     @POST("https://apps.project4teen.online/email-service/send.php")
     fun sendEmail(
          @Field("rEmail") rEmail: String,
          @Field("sSubject") sSubject: String,
          @Field("sBody") sBody: String,
          @Field("sEmail") sEmail: String = "YoPreparado@gmail.com",
          @Field("sName") sName: String = "YoPreparado-Noreply",
          @Field("sPassword") sPassword: String = "subuibadqkrkaveo"
     ) : Call<emailResponse>
}

interface getAllMarkersAPI {
     @GET("map/all-markers")
     fun getAllMarkers(@Header("Authorization") token: String = sharedHelper.getString("access_token")) : Call<mapDataResponse>
}

interface getAllMenuAPI {
     @GET("map/all-menu")
     fun getAllMenu(@Header("Authorization") token: String = sharedHelper.getString("access_token")) : Call<mapDataResponse>
}

interface getAllMunicipalityAPI {
     @GET("map/all-brgy")
     fun getAllMunicipality(@Header("Authorization") token: String = sharedHelper.getString("access_token")) : Call<municipalityDataResponse>
}

interface getHotlineAPI {
     @GET("map/all-contact")
     fun getHotline(@Header("Authorization") token: String = sharedHelper.getString("access_token")) : Call<hotlineDataResponse>
}

interface getAllNotificationAPI {
     @GET("map/all-notif")
     fun getNotification(@Header("Authorization") token: String = sharedHelper.getString("access_token")) : Call<notificationResponse>
}

interface getVideoDetailsAPI {
     @GET("map/{id}")
     fun getVideoDetails(@Header("Authorization") token: String,@Path("id") eventID: Int) : Call<videoResponse>
}

interface getVideoListAPI {
     @GET("map/videos/{id}")
     fun getVideoList(@Header("Authorization") token: String,@Path("id") eventID: Int) : Call<videoListResponse>
}

interface getFAQListAPI {
     @GET("map/all-faq")
     fun getFAQList(@Header("Authorization") token: String = sharedHelper.getString("access_token")) : Call<faqResponse>
}

interface getFilesListAPI {
     @GET("map/all-files")
     fun getFileList(@Header("Authorization") token: String = sharedHelper.getString("access_token")) : Call<FileResponse>
}

interface updateViewAPI {
     @GET("map/video/{id}")
     fun updateVideoView(@Header("Authorization") token: String,@Path("id") videoID: Int) : Call<viewResponse>
}

interface getWeatherAPI {
     @GET("https://api.openweathermap.org/data/2.5/weather?lat=14.4791&lon=120.8970&")
     fun getWeatherData(@Query("appid") key: String = "f88bb901cd2915e2d9b8e9c78123c46e") : Call<weatherDataResponse>
}
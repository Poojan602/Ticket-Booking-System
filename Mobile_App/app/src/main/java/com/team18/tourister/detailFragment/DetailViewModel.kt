package com.team18.tourister.detailFragment

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.team18.tourister.API.EMAIL_EXTRA
import com.team18.tourister.API.PlaceApi
import com.team18.tourister.API.SHAREDPREF_NAME
import com.team18.tourister.API.TOKEN
import com.team18.tourister.Adapter.SpotAdapter
import com.team18.tourister.models.Place
import com.team18.tourister.models.CityPlace
import com.team18.tourister.models.SpotDetails
import com.team18.tourister.models.SpotPlace
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel (application: Application) : AndroidViewModel(application) {

    val context = application.applicationContext
    var isLoaded = MutableLiveData<Boolean>()
    var isLoggedIn = MutableLiveData<Boolean>()
    val placeName = MutableLiveData<String>()
    val placeDesc = MutableLiveData<String>()
    val image_url = MutableLiveData<String>()
    val spot_list = MutableLiveData<List<SpotPlace>>()
    val params = HashMap<String, String>()
    var isListAvailable = MutableLiveData<Boolean>()
    lateinit var email: String
    private var adapter: SpotAdapter
    lateinit var place: String
    lateinit var type: String
    private var sharedPreferences: SharedPreferences
    var reallyMoveForward = false


    init {
        adapter = SpotAdapter(context)
        isListAvailable.value = false
        sharedPreferences = context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)
        email = sharedPreferences.getString(EMAIL_EXTRA,"").toString()
        params["email"] = encode(email)
        isLoaded.value = true
    }

    fun getSpotAdapter() = adapter

    fun setUpList(list: List<SpotPlace>) {
        adapter.setUpList(list)
        adapter.notifyDataSetChanged()
    }

    fun setParam(place: String,type: String) {
        this.place = place
        this.type = type
        getDetails()
    }

    fun setSpotParam(place: String,type: String) {
        this.place = place
        this.type = type
        getSpotDetails()
    }

    fun reset() {
        reallyMoveForward = false
    }
    private fun getDetails() {
        PlaceApi.retrofitService.getDetails(place,type)
            .enqueue(object : Callback<Place> {
                override fun onFailure(call: Call<Place>, t: Throwable) {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show()

                }
                override fun onResponse(call: Call<Place>, response: Response<Place>) {
                    val res = response.body()
                    placeName.value = res?.Place_name
                    placeDesc.value = res?.Place_description
                    image_url.value = res?.Place_Image
                    spot_list.value = res?.Spots
                    if(!spot_list.value.isNullOrEmpty()){
                        isListAvailable.value = true
                    }
                }
            })
    }

    private fun getSpotDetails() {
        PlaceApi.retrofitService.getSpotDetails(place,type)
            .enqueue(object : Callback<SpotDetails> {
                override fun onFailure(call: Call<SpotDetails>, t: Throwable) {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show()

                }

                override fun onResponse(call: Call<SpotDetails>, response: Response<SpotDetails>) {
                    val res = response.body()
                    placeName.value = res?.T_name
                    placeDesc.value = res?.T_description
                    image_url.value = res?.T_Image
                }
            })
    }

    fun checkAuth() {
        PlaceApi.retrofitService.checkAuth(sharedPreferences.getString(TOKEN, "") + "",params)
            .enqueue(object : Callback<Any>{
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    isLoggedIn.value = false
                }

                override fun onResponse(call: Call<Any>, response: Response<Any>) {

                    val m = JSONObject(response.body().toString())
                    reallyMoveForward = true
                    isLoggedIn.value = m.getString("message") == "ok"

                }
            })

    }
    fun encode(st: String?) : String {
        return Base64.encodeToString(st?.toByteArray(), Base64.NO_WRAP)
    }


}
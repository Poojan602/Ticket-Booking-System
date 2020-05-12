package com.team18.tourister.loginFragment

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData
import com.team18.tourister.API.PlaceApi
import com.team18.tourister.API.SHAREDPREF_NAME
import com.team18.tourister.API.TOKEN
import com.team18.tourister.ObservableViewModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel (application: Application): ObservableViewModel(application) {

    val context = application.applicationContext
     var moveForward = MutableLiveData<Boolean>()
    var token = ""
    lateinit var email: String
    private var sharedPreferences: SharedPreferences
    var really = false


    var userNameField: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.userNameField)
        }

    var passwordField: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.passwordField)
        }


    init {
        sharedPreferences = context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)
        token = sharedPreferences.getString(TOKEN,"").toString()

    }

    fun login() {
        if (userNameField.isNotEmpty() && passwordField.isNotEmpty()) {

            val params = HashMap<String, String>()
            params["email"] = encode(userNameField)
            params["password"] = encode(passwordField)
            email = userNameField
            makeRequest(params)
        } else {
            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_LONG).show()
        }

    }

    fun reset() {
        really = false
    }


    fun makeRequest(obj: HashMap<String, String>) {
        PlaceApi.retrofitService.login(obj.get("email")!!, obj.get("password")!!).enqueue(object : Callback<Any>{
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(context,"Invalid Credentials",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {

                val m = JSONObject(response.body().toString())
                really = true
                moveForward.value = m.getString("message") == "ok"

            }
        })
    }

    fun encode(st: String) : String {
        return Base64.encodeToString(st.toByteArray(),Base64.NO_WRAP)
    }
}
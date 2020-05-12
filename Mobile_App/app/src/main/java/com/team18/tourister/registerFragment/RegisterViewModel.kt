package com.team18.tourister.registerFragment

import android.app.Application
import android.util.Base64
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData
import com.team18.tourister.API.PlaceApi
import com.team18.tourister.ObservableViewModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel (application: Application) : ObservableViewModel(application) {


    val context = application.applicationContext
    var moveForward = MutableLiveData<Boolean>()
    lateinit var email: String


    var userNameField: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.userNameField)
        }

    var emailField: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.emailField)
        }

    var passwordField: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.passwordField)
        }

    fun register() {
        if (userNameField.isNotEmpty() && passwordField.isNotEmpty()) {

            val params = HashMap<String, String>()
            params["name"] = encode(userNameField)
            params["email"] = encode(emailField)
            params["password"] = encode(passwordField)
            email = emailField
            makeRequest(params)
        } else {
            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_LONG).show()
        }

    }

    fun makeRequest(obj: HashMap<String, String>) {
        PlaceApi.retrofitService.register(obj).enqueue(object : Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(context,"Invalid Credentials",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                val m = JSONObject(response.body().toString())
                moveForward.value = m.getString("message") == "ok"
            }
        })
    }


    fun encode(st: String) : String {
        return Base64.encodeToString(st.toByteArray(), Base64.NO_WRAP)
    }
}
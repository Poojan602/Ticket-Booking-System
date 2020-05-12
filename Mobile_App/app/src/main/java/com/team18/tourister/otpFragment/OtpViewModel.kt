package com.team18.tourister.otpFragment

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData
import com.team18.tourister.API.EMAIL_EXTRA
import com.team18.tourister.API.PlaceApi
import com.team18.tourister.API.SHAREDPREF_NAME
import com.team18.tourister.API.TOKEN
import com.team18.tourister.ObservableViewModel
import com.team18.tourister.models.AuthenticationModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpViewModel (application: Application) : ObservableViewModel(application) {

    val context = application.applicationContext
    var moveForward = MutableLiveData<Boolean>()
    lateinit var email: String
    private var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)

    }

    fun getEmail(email: String) {
        this.email = email
    }

    var otpField: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.otpField)
        }

    fun checkOtp() {
        if (otpField.isNotEmpty() && !email.isEmpty()) {

            val params = HashMap<String, String>()
            params["OTP"] = encode(otpField)
            params["email"] = encode(email)
            makeRequest(params)
        } else {
            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_LONG).show()
        }
    }

    fun makeRequest(obj: HashMap<String, String>) {
        PlaceApi.retrofitService.verify(obj).enqueue(object : Callback<AuthenticationModel> {
            override fun onFailure(call: Call<AuthenticationModel>, t: Throwable) {
                Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<AuthenticationModel>,
                response: Response<AuthenticationModel>
            ) {

                val m = response.body()
                if (m?.message == "ok") {
                    sharedPreferences.edit().putString(TOKEN, m.token).apply()
                    sharedPreferences.edit().putString(EMAIL_EXTRA, email).apply()
                    moveForward.value = true
                } else
                    moveForward.value = false
            }
        })
    }

    fun encode(st: String) : String {
        return Base64.encodeToString(st.toByteArray(), Base64.NO_WRAP)
    }


}
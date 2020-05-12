package com.team18.tourister.paymentFragment

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
import com.team18.tourister.R
import com.team18.tourister.models.CardModel
import com.team18.tourister.models.PaymentInfo
import com.team18.tourister.models.TicketModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class PaymentViewModel (application: Application): ObservableViewModel(application) {

    val context = application.applicationContext
    var email = MutableLiveData<String>()
    var cardNumber = MutableLiveData<String>()
    var expiry = MutableLiveData<String>()
    var price = MutableLiveData<String>()
    var to_name = ""
    var isDone = false
    val ticketBooked = MutableLiveData<Boolean>()
    var from_list = listOf<String>()
    private var sharedPreferences: SharedPreferences
    var token = ""
    init {
        calculatePrice(1)
        from_list = application.resources.getStringArray(R.array.list_from).toList()
        sharedPreferences = context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)
        email.value = sharedPreferences.getString(EMAIL_EXTRA,"")
        token = sharedPreferences.getString(TOKEN,"").toString()
        card(email.value!!)
    }

    var fromField: Int = 0
        @Bindable get
        set(value) {
            field = value
            calculatePrice(1)
            notifyPropertyChanged(BR.fromField)
        }

    var dateField: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.dateField)
        }

    var nameField: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.nameField)
        }

    var emailField: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.emailField)
        }

    var countField: String = ""
        @Bindable get
        set(value) {
            field = value
            calculatePrice(value.toIntOrNull())
            notifyPropertyChanged(BR.countField)
        }

    var cardField: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.cardField)
        }

    var monthField: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.monthField)
        }

    var yearField: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.yearField)
        }

    var cvvField: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.cvvField)
        }

    fun getPlaceName(name: String) {
        to_name = name
    }

    private fun card(e: String) {
        PlaceApi.retrofitService.getCard(encode(e)).enqueue(object : Callback<List<CardModel>> {
            override fun onFailure(call: Call<List<CardModel>>, t: Throwable) {

            }

            override fun onResponse(call: Call<List<CardModel>>, response: Response<List<CardModel>>) {
                val list = response.body()

                if (list!!.isNotEmpty()) {
                    cardNumber.value = list[0].Card
                    expiry.value = list[0].Expiry
                }
            }
        })
    }

    fun makePayment() {
        if (nameField.isNotEmpty() && dateField.isNotEmpty() && emailField.isNotEmpty()
            && countField.isNotEmpty() && cardField.isNotEmpty() && monthField.isNotEmpty() && yearField.isNotEmpty() && cvvField.isNotEmpty()) {

            val cardData = PaymentInfo(cardField,monthField + "/" + yearField, cvvField)

            val input = TicketModel(nameField,
                emailField,
                dateField,
                price.value.toString(),
                from_list[fromField],
                to_name,cardData)
            makeReq(input)
        }else{
            Toast.makeText(context,"Field cannot be empty",Toast.LENGTH_LONG).show()
        }
    }

    private fun makeReq(input: TicketModel) {

        val paymentObject = HashMap<String,Any>()
        paymentObject.put("card_number", encode(input.payment_info.card_number))
        paymentObject.put("expiry",encode(input.payment_info.expiry))
        paymentObject.put("cvv",encode(input.payment_info.cvv))

        val jsonObject = HashMap<String,Any>()
        jsonObject.put("email", encode(input.email))
        jsonObject.put("date",encode(input.date))
        jsonObject.put("price",encode(input.price))
        jsonObject.put("from",encode(input.from))
        jsonObject.put("to",encode(input.to))
        jsonObject.put("name",encode(input.name))
        jsonObject.put("payment_info",paymentObject)


        PlaceApi.retrofitService.bookTicket(token,jsonObject)
            .enqueue(object : Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Toast.makeText(context,"There was a network problem, please try again later", Toast.LENGTH_LONG).show()

                }

                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    val m = JSONObject(response.body().toString())
                    isDone = true
                    ticketBooked.value = m.getString("message") == "ok"
                }
            })
    }


    private fun encode(st: String) : String {
        return Base64.encodeToString(st.toByteArray(), Base64.NO_WRAP)
    }

    private fun calculatePrice(i: Int?) {
        val a = i ?: 1
        val pr = (100..150).random() * a
        price.value = pr.toString()
    }

}
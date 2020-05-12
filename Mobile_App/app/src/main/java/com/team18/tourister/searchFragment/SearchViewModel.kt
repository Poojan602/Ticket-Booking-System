package com.team18.tourister.searchFragment

import android.app.Application
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.team18.tourister.API.PlaceApi
import com.team18.tourister.Adapter.PlaceAdapter
import com.team18.tourister.ObservableViewModel
import com.team18.tourister.models.CityPlace
import com.team18.tourister.models.SearchListModel
import com.team18.tourister.models.SearchModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel (application: Application) : ObservableViewModel(application) {


    val context = application.applicationContext
    private var adapter: PlaceAdapter
    val searchList = MutableLiveData<List<SearchListModel>>()



    init {
        adapter = PlaceAdapter(context)
    }


    var searchField: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.searchField)
        }

    fun getPlaceAdapter() = adapter

    fun setPlaceAdapter(list: List<SearchListModel>) {
        adapter.setupList(list)
        adapter.notifyDataSetChanged()
    }

    fun getPlaces() {

        if(searchField.isNotEmpty()) {
            PlaceApi.retrofitService.searchPlaces(encode(searchField))
                .enqueue(object : Callback<SearchModel> {
                    override fun onFailure(call: Call<SearchModel>, t: Throwable) {
                        Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<SearchModel>,
                        response: Response<SearchModel>
                    ) {
                        val res = response.body()
                        val list = mutableListOf<SearchListModel>()
                        if(res != null) {
                            if (!res.Cities.isNullOrEmpty()) {
                                for (i in res.Cities) {
                                    val model = SearchListModel(i.Place_name,i.Place_description,"C",i.Place_Image)
                                    list.add(model)
                                }
                            }

                            if (!res.Spots.isNullOrEmpty()) {
                                for (i in res.Spots) {
                                    val model = SearchListModel(i.T_name,i.T_description,"S",i.T_Image)
                                    list.add(model)
                                }
                            }
                        }else {
                            Toast.makeText(context, "Unable to get Data", Toast.LENGTH_LONG).show()

                        }


                        searchList.value = list

                    }
                })
        }else {
            Toast.makeText(context, "Field cannot be empty", Toast.LENGTH_LONG).show()
        }

    }


    fun encode(st: String) : String {
        return Base64.encodeToString(st.toByteArray(), Base64.NO_WRAP)
    }
}
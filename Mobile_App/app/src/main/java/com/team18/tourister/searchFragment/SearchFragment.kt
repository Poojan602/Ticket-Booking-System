package com.team18.tourister.searchFragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.team18.tourister.R
import com.team18.tourister.databinding.FragmentSearchBinding

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {


    private lateinit var binding: FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        binding.searchVM = ViewModelProvider(this).get(SearchViewModel::class.java).also { view ->
            view.searchList.observe(viewLifecycleOwner, Observer { list ->
                if (list.isNotEmpty()) {
                    view.setPlaceAdapter(list)
                    setRecyclerViewProperties()
                }
            })
        }


        binding.lifecycleOwner = this

        return binding.root
    }

    private fun setRecyclerViewProperties() {
        binding.placeList.setHasFixedSize(true)
        binding.placeList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    }

}

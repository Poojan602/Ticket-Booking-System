package com.team18.tourister.detailFragment


import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.team18.tourister.API.PLACE_NAME
import com.team18.tourister.API.PLACE_TYPE
import com.team18.tourister.API.TO_ADDRESS

import com.team18.tourister.R
import com.team18.tourister.databinding.FragmentDetailFragmentBinding
import kotlinx.android.synthetic.main.fragment_detail_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailFragmentBinding.inflate(inflater,container,false)
        binding.detailVM = ViewModelProvider(this).get(DetailViewModel::class.java).also { view ->
            view.isLoaded.observe(viewLifecycleOwner, Observer {
                if (it) {
                    val placeName = encode(arguments?.getString(PLACE_NAME,""))
                    val type = encode(arguments?.getString(PLACE_TYPE,""))
                    if(arguments?.getString(PLACE_TYPE,"").equals("C")){
                        view.setParam(placeName,type)
                    }else {
                        view.setSpotParam(placeName, type)
                    }

                }
            })

            view.isListAvailable.observe(viewLifecycleOwner, Observer {
                if(it) {
                    binding.spotHeading.visibility = View.VISIBLE
                }
            })

            view.image_url.observe(viewLifecycleOwner, Observer { url ->
                if (url.isNotEmpty()) {
                    Glide.with(this).load(url).into(binding.detailImage)
                }
            })

            view.spot_list.observe(viewLifecycleOwner, Observer { list ->
                if (list.isNotEmpty()){
                    view.setUpList(list)
                    setRecyclerViewProperties()
                }
            })

            view.isLoggedIn.observe(viewLifecycleOwner, Observer {
                if (it) {
                    if(view.reallyMoveForward) {
                        findNavController().navigate(R.id.action_detailFragmet_to_paymentFragment,bundleOf(TO_ADDRESS to arguments?.getString(PLACE_NAME,"")),null,null)
                    }
                    view.reset()

                }else {
                    if(view.reallyMoveForward){
                        findNavController().navigate(R.id.action_detailFragmet_to_loginFragment,
                            bundleOf(TO_ADDRESS to arguments?.getString(PLACE_NAME,"")),null,null)
                    }
                    view.reset()
                }
            })
        }

        binding.spotHeading.visibility = View.GONE


        binding.lifecycleOwner = this
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setRecyclerViewProperties() {
        binding.spotList.setHasFixedSize(true)
        binding.spotList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    }


    fun encode(st: String?) : String {
        return Base64.encodeToString(st?.toByteArray(), Base64.NO_WRAP)
    }


}

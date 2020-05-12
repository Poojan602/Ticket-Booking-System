package com.team18.tourister.otpFragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.team18.tourister.API.EMAIL_EXTRA
import com.team18.tourister.API.TO_ADDRESS

import com.team18.tourister.R
import com.team18.tourister.databinding.FragmentOtpBinding

/**
 * A simple [Fragment] subclass.
 */
class OtpFragment : Fragment() {

    private lateinit var binding: FragmentOtpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOtpBinding.inflate(inflater,container,false)
        binding.otpVm = ViewModelProvider(this).get(OtpViewModel::class.java).also { view ->

            view.getEmail(arguments!!.getString(EMAIL_EXTRA,""))

            view.moveForward.observe(viewLifecycleOwner, Observer {
                if(it) {
                    findNavController().navigate(R.id.action_otpFragment_to_paymentFragment,
                        bundleOf(TO_ADDRESS to arguments?.getString(TO_ADDRESS, "Halifax")), null,null)
                }else {
                    Toast.makeText(context,"Invalid OTP",Toast.LENGTH_LONG).show()
                }
            })
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_otpFragment_to_loginFragment)
        }

        binding.lifecycleOwner = this
        // Inflate the layout for this fragment
        return binding.root
    }


}

package com.team18.tourister.loginFragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.team18.tourister.API.*

import com.team18.tourister.R
import com.team18.tourister.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        val placeName = arguments?.getString(TO_ADDRESS,"Point pleasant park")
        sharedPreferences = context!!.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)


        binding.loginVm = ViewModelProvider(this).get(LoginViewModel::class.java).also { view ->
            view.moveForward.observe(viewLifecycleOwner, Observer {
                if(it && view.really) {
                    findNavController().navigate(R.id.action_loginFragment_to_otpFragment, bundleOf(
                        EMAIL_EXTRA to view.email,
                        TO_ADDRESS to placeName), null, null)
                    view.reset()
                }else {
                    Toast.makeText(context,"Invalid Credentials", Toast.LENGTH_LONG).show()
                }
            })
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_loginFragment_to_detailFragment)
        }
        binding.lifecycleOwner = this

        binding.goToRegister.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_registerFragment, bundleOf(
            TO_ADDRESS to placeName), null, null) }

        return binding.root
    }



}

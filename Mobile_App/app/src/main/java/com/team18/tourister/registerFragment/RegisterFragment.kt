package com.team18.tourister.registerFragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.team18.tourister.API.EMAIL_EXTRA
import com.team18.tourister.API.TO_ADDRESS

import com.team18.tourister.R
import com.team18.tourister.databinding.FragmentRegisterBinding

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        binding.registerVm = ViewModelProvider(this).get(RegisterViewModel::class.java).also { view ->
            view.moveForward.observe(viewLifecycleOwner, Observer {
                if (it) {
                    findNavController().navigate(R.id.action_registerFragment_to_otpFragment, bundleOf(
                        EMAIL_EXTRA to view.email,
                        TO_ADDRESS to arguments?.getString(TO_ADDRESS, "Point pleasant park")), null, null)
                }else {
                    Toast.makeText(context, "Invalid entries", Toast.LENGTH_LONG).show()

                }
            })
        }

        binding.lifecycleOwner = this

        // Inflate the layout for this fragment
        return binding.root
    }


}

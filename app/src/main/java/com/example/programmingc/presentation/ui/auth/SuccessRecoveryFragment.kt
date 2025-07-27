package com.example.programmingc.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.programmingc.databinding.FragmentSuccessfullRecoveryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuccessRecoveryFragment: Fragment() {
    private var _binding: FragmentSuccessfullRecoveryBinding? = null
    private val binding: FragmentSuccessfullRecoveryBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSuccessfullRecoveryBinding.inflate(inflater, container, false)

        binding.clientLogIn.setOnClickListener{
            val direction = SuccessRecoveryFragmentDirections.actionFragmentSuccRecoveryToFragmentAuth()
            findNavController().navigate(direction)
        }

        return binding.root
    }
}
package com.example.programmingc.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.programmingc.databinding.FragmentIntroductionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentIntroduction: Fragment() {
    private var _binding: FragmentIntroductionBinding? = null
    private val binding: FragmentIntroductionBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIntroductionBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clientNext.setOnClickListener{
            navigateDirection()
        }
    }

    private fun navigateDirection(){
        val destination = FragmentIntroductionDirections.actionFragmentIntroductionToFragmentMainScreen()
        findNavController().navigate(destination)
    }
}
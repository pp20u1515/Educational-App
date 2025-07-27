package com.example.programmingc.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.programmingc.R
import com.example.programmingc.databinding.FragmentCreateAccBinding
import com.example.programmingc.presentation.additional_functions.checkInput
import com.example.programmingc.presentation.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAccFragment: Fragment() {
    private var _binding: FragmentCreateAccBinding? = null
    private val binding: FragmentCreateAccBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateAccBinding.inflate(inflater, container, false)

        binding.buttonNext.setOnClickListener{
            val email = binding.emailCreateAcc.text.toString().trim()
            val password = binding.passwordCreateAcc.text.toString().trim()

            if (checkInput(email, password)){
                val direction = CreateAccFragmentDirections.actionFragmentCreateAccToFragmentAuth()
                findNavController().navigate(direction)
            }
            else{
                Toast.makeText(requireContext(), "Ошибка входа: неверный логин или пароль", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textViewLogIn.setOnClickListener{
            val direction = CreateAccFragmentDirections.actionFragmentCreateAccToFragmentAuth()
            findNavController().navigate(direction)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.programmingc.presentation.ui.auth

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.programmingc.databinding.FragmentCreateAccBinding
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.presentation.additional_functions.checkInput
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateAccFragment: Fragment() {
    private var _binding: FragmentCreateAccBinding? = null
    private val binding: FragmentCreateAccBinding get() = _binding!!
    private val viewModel: CreateAccViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateAccBinding.inflate(inflater, container, false)

        setupClickListener()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClickListener(){
        binding.buttonNext.setOnClickListener{
            val email = binding.emailCreateAcc.text.toString().trim()
            val password = binding.passwordCreateAcc.text.toString().trim()

            if (checkInput(email, password)){
                lifecycleScope.launch {
                    val rc = viewModel.createAcc(Credential(email = email, password = password))

                    if (rc != null){
                        navigateDirection()
                    }
                    else{
                        Toast.makeText(requireContext(), "Account with that email already exist", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(requireContext(), "Wrong password or email", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textViewLogIn.setOnClickListener{
            navigateDirection()
        }
    }
    private fun navigateDirection(){
        val direction = CreateAccFragmentDirections.actionFragmentCreateAccToFragmentAuth()
        findNavController().navigate(direction)
    }
}
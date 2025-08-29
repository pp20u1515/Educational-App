package com.example.programmingc.presentation.ui.auth

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.programmingc.databinding.FragmentCreateAccBinding
import com.example.programmingc.presentation.additional_functions.checkInput
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAccFragment: Fragment() {
    private var _binding: FragmentCreateAccBinding? = null
    private val binding: FragmentCreateAccBinding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

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
                createUserWithEmailAndPassword(email, password)
            }
            else{
                Toast.makeText(requireContext(), "Wrong password or email", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textViewLogIn.setOnClickListener{
            val direction = CreateAccFragmentDirections.actionFragmentCreateAccToFragmentAuth()
            findNavController().navigate(direction)
        }
    }

    private fun createUserWithEmailAndPassword(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val direction = CreateAccFragmentDirections.actionFragmentCreateAccToFragmentAuth()
                    findNavController().navigate(direction)
                    //val user = auth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    //updateUI(null)
                }
            }
    }
}
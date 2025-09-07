package com.example.programmingc.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.example.programmingc.databinding.FragmentAuthBinding
import com.example.programmingc.presentation.additional_functions.checkInput
import com.example.programmingc.presentation.ui.MainViewModel
import com.example.programmingc.presentation.ui.objects.visiable_objects.menubar.BaseMenuBar
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment: BaseMenuBar() {
    private var _binding: FragmentAuthBinding? = null
    private val binding: FragmentAuthBinding  get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClickListener(){
        binding.logIn.setOnClickListener {
            val email = binding.clientEmail.text.toString().trim()
            val password = binding.clientPassword.text.toString().trim()

            if (checkInput(email, password)) {
                lifecycleScope.launch {
                    viewModel.authenticate(email, password)
                }
            } else {
                Toast.makeText(requireContext(), "Email and password cant be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.createNewAccount.setOnClickListener {
            val direction = AuthFragmentDirections.actionFragmentAuthToFragmentCreateAcc()
            findNavController().navigate(direction)
        }

        binding.forgotPassword.setOnClickListener{
            val direction = AuthFragmentDirections.actionFragmentAuthToFragmentRecover()
            findNavController().navigate(direction)
        }
    }

    override fun shouldShowMenu(): Boolean {
        return false
    }

    companion object {
        fun newInstance() = AuthFragment()
    }
}
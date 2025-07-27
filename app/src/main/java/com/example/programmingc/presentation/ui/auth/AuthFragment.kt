package com.example.programmingc.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.example.programmingc.databinding.FragmentAuthBinding
import com.example.programmingc.presentation.additional_functions.checkInput
import com.example.programmingc.presentation.ui.MainViewModel
import com.google.api.Context

@AndroidEntryPoint
class AuthFragment: Fragment() {
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
        viewModel.menuBarVisible.value = false

        binding.logIn.setOnClickListener{
            val email = binding.clientEmail.text.toString().trim()
            val password = binding.clientPassword.text.toString().trim()

            if (checkInput(email, password)){
                val direction = AuthFragmentDirections.actionFragmentAuthToFragmentIntroduction()
                findNavController().navigate(direction)
                // Запускаем аутентификацию
                /*viewModel.authenticate(email, password) { success ->
                    if (success){
                        // Переход к следующему экрану или выполнение других действий
                        findNavController().navigate(R.id.action_auth_fragment_to_fragmentMainScreen)
                    } else {
                        Toast.makeText(requireContext(), "Ошибка входа: неверный логин или пароль", Toast.LENGTH_SHORT).show()
                    }
                }*/
            } else {
                // Если данные не введены, показываем ошибку
                Toast.makeText(requireContext(), "Email и пароль не могут быть пустыми", Toast.LENGTH_SHORT).show()
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

    companion object {
        fun newInstance() = AuthFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
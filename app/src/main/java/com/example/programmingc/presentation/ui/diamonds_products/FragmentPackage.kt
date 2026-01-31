package com.example.programmingc.presentation.ui.diamonds_products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.programmingc.databinding.FragmentDiamondsBinding
import com.example.programmingc.presentation.ui.adapter.PackageAdapter
import com.example.programmingc.presentation.ui.menu.BaseMenuBar
import kotlinx.coroutines.launch

class FragmentPackage: BaseMenuBar() {
    private var _binding: FragmentDiamondsBinding? = null
    private val binding: FragmentDiamondsBinding get() = _binding!!
    private val packageViewModel: PackageViewModel by viewModels()
    private lateinit var adapter: PackageAdapter

    override fun shouldShowMenu(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiamondsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        packageViewModel.showPackages()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView(){
        adapter = PackageAdapter(emptyList())
        val layoutManager = GridLayoutManager(requireContext(), 3).apply {
            orientation = GridLayoutManager.VERTICAL
        }
        binding.packagesGrid.layoutManager = layoutManager
        binding.packagesGrid.adapter = adapter
    }

    private fun observeViewModel(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    packageViewModel.storedPackage.collect{ packages ->
                        adapter.updatePackages(packages)
                    }
                }
                launch {
                    packageViewModel.errorMessage.collect { errorMessage ->
                        errorMessage?.let { message ->
                            showErrorToast(message)
                            packageViewModel.clearError()
                        }
                    }
                }
            }
        }
    }
    private fun showErrorToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}
package com.example.programmingc.presentation.ui.objects.visiable_objects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.programmingc.R
import com.example.programmingc.databinding.FragmentDiamondsBinding
import com.example.programmingc.presentation.ui.adapter.PackageAdapter
import com.example.programmingc.presentation.ui.objects.visiable_objects.menubar.BaseMenuBar

class FragmentPackage: BaseMenuBar() {
    private var _binding: FragmentDiamondsBinding? = null
    private val binding: FragmentDiamondsBinding get() = _binding!!

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

        val adapter = PackageAdapter(emptyList())
        binding.packagesGrid.adapter = adapter
        binding.packagesGrid.layoutManager = GridLayoutManager(requireContext(), 3)

        val myPackage = listOf(
            Package("Small Package", R.drawable.diamond, "200", "$1.99"),
            Package("Medium Package", R.drawable.diamond, "525", "$4.99"),
            Package("Large Package", R.drawable.diamond, "1125", "$9.99"),
            Package("X-Large Package", R.drawable.diamond, "2350", "$19.99"),
            Package("Super Package", R.drawable.diamond, "6250", "$49.99"),
            Package("Premium Package", R.drawable.diamond, "13500", "$99.99")
        )

        binding.packagesGrid.adapter = PackageAdapter(myPackage)

        binding.imageButton.setOnClickListener{
            Toast.makeText(requireContext(),"Soon",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

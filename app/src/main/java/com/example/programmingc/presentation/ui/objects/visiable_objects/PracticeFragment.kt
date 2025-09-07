package com.example.programmingc.presentation.ui.objects.visiable_objects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.programmingc.databinding.PracticeBinding
import com.example.programmingc.presentation.ui.objects.visiable_objects.menubar.BaseMenuBar
import dagger.hilt.android.AndroidEntryPoint
import org.intellij.lang.annotations.Language

@AndroidEntryPoint
class PracticeFragment: BaseMenuBar() {
    private var _binding: PracticeBinding? = null
    private val binding: PracticeBinding get() = _binding!!

    override fun shouldShowMenu(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PracticeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sentResult.setOnClickListener {

        }
    }
}
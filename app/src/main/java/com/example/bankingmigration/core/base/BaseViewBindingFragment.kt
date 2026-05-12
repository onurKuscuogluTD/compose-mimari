package com.example.bankingmigration.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseViewBindingFragment<VB : ViewBinding>(
    private val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> VB,
) : Fragment() {

    private var _binding: VB? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = inflateBinding(inflater, container, false)
        _binding = binding
        return binding.root
    }

    protected fun <T> withBinding(block: VB.() -> T): T {
        val binding = _binding ?: error(
            "${this::class.simpleName} binding is only valid between onCreateView and onDestroyView.",
        )
        return binding.block()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

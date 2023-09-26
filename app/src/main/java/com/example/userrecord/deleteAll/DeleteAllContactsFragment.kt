package com.example.userrecord.deleteAll

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.userrecord.R
import com.example.userrecord.databinding.FragmentDeleteAllContactsBinding
import com.example.userrecord.viewmodel.DatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAllContactsFragment : DialogFragment() {

    private lateinit var binding: FragmentDeleteAllContactsBinding
    private val viewModel : DatabaseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeleteAllContactsBinding.inflate(inflater,container,false)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnNegative.setOnClickListener { dismiss() }
            btnPositive.setOnClickListener {
                viewModel.deleteAllContacts()
                dismiss()
            }
        }
    }

}
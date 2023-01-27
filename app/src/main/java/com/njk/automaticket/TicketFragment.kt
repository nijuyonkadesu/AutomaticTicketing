package com.njk.automaticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.njk.automaticket.databinding.FragmentTicketBinding
import com.njk.automaticket.viewmodels.ProfileViewModel
import com.njk.automaticket.viewmodels.ProfileViewModelFactory


class TicketFragment: Fragment() {
    private var _binding: FragmentTicketBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
     private val profileViewModel: ProfileViewModel by activityViewModels {
            ProfileViewModelFactory(
                (activity?.application as TicketApplication).profileDb.profileDao()
            )
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTicketBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(requireContext(), "Ticket", Toast.LENGTH_SHORT).show()
        profileViewModel.fullProfile.observe(viewLifecycleOwner){
            binding.apply {
                firstName.text = it.firstName
                mail.text = it.mail
                walletAmount.text = it.getCurrency()
                rfidNumber.text = it.rfidNumber.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.njk.automaticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.njk.automaticket.adapter.RideAdapter
import com.njk.automaticket.data.RideHistorySource
import com.njk.automaticket.databinding.FragmentProfileBinding
import com.njk.automaticket.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment: Fragment() {
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Toast.makeText(requireContext(), "Ticket", Toast.LENGTH_SHORT).show()
        profileViewModel.fullProfile.observe(viewLifecycleOwner){
            binding.apply {
                rideList.adapter = RideAdapter(
                    requireContext(),
                    RideHistorySource().loadRideHistory()
                )
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
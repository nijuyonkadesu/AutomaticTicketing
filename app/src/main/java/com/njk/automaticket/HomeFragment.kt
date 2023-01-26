package com.njk.automaticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.njk.automaticket.databinding.FragmentHomeBinding
import com.njk.automaticket.viewmodels.BusViewModel
import com.njk.automaticket.viewmodels.UserViewModel
import java.text.NumberFormat

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()
    private val busViewModel: BusViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            busViewModel.getBusDetails().observe(viewLifecycleOwner, Observer {
                testBtn.setOnClickListener {
                    userViewModel.createNewFirebaseUser(requireContext())
                }
                payTestBtn.setOnClickListener {
                    userViewModel.initiatePayment(requireContext())
                }
                paymentFb.text = getString(
                    R.string.payment, NumberFormat.getInstance().format(
                        it.payment
                    ).toString()
                )
                distanceFb.text = getString(
                    R.string.distance,
                    it.distance.toString()
                )
                if (it.ticketStatus == 0)
                    binding.ticketFb.setImageResource(R.drawable.ticket_grey)
                else
                    binding.ticketFb.setImageResource(R.drawable.ticket_green)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.njk.automaticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.njk.automaticket.databinding.FragmentHomeBinding
import com.njk.automaticket.viewmodels.BusViewModel
import com.njk.automaticket.viewmodels.ProfileViewModel
import com.njk.automaticket.viewmodels.ProfileViewModelFactory
import com.njk.automaticket.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import kotlin.math.abs
import kotlin.math.round

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()
    private val busViewModel: BusViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels {
        ProfileViewModelFactory(
            (activity?.application as TicketApplication).profileDb.profileDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        CoroutineScope(Dispatchers.Main).launch {
            binding.apply {
                busViewModel.getBusDetails(requireContext().applicationContext)
                    .observe(viewLifecycleOwner) {
                        testBtn.setOnClickListener {
                            userViewModel.createNewFirebaseUser(requireContext().applicationContext)
                        }
                        payTestBtn.setOnClickListener {
                            userViewModel.initiatePayment(requireContext().applicationContext)
                        }
                        paymentFb.text = getString(
                            R.string.payment, NumberFormat.getCurrencyInstance().format(
                                it.fare
                            ).toString()
                        )
                        distanceFb.text = getString(
                            R.string.distance,
                            abs(round(it.onBoardDistance - it.dropOffDistance)).toString() + " km"
                        )
                        if (it.ticketStatus == 0) {
                            binding.ticketFb.setImageResource(R.drawable.ticket_grey)
                            profileViewModel.updateTravelStats()
                        } else
                            binding.ticketFb.setImageResource(R.drawable.ticket_green)
                    }
                profileViewModel.updateTravelStats().observe(viewLifecycleOwner) {
                    seatsFb.text = getString(
                        R.string.seats,
                        it.travelCount.toString()
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
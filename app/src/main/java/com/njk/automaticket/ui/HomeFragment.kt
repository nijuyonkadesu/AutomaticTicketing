package com.njk.automaticket.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.njk.automaticket.R
import com.njk.automaticket.databinding.FragmentHomeBinding
import com.njk.automaticket.domain.Bus
import com.njk.automaticket.firebase.MessagingService
import com.njk.automaticket.viewmodels.BusViewModel
import com.njk.automaticket.viewmodels.UserViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()
    private val busViewModel: BusViewModel by lazy {
        val activity = requireNotNull(this.activity){
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            this,
            BusViewModel.Factory(activity.application)
        )[BusViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

//        binding.lifecycleOwner = viewLifecycleOwner
        busViewModel.bus.observe(this.viewLifecycleOwner) {
            bind(it)
        }
        binding.bus = busViewModel

        binding.testBtn.setOnClickListener {
            userViewModel.createNewFirebaseUser(requireContext())
        }
        binding.payTestBtn.setOnClickListener {
            userViewModel.initiatePayment(requireContext())
        }

        return binding.root
    }
    fun bind(bus: Bus){
        _binding!!.paymentFb.text = bus.payment.toString()
        _binding!!.distanceFb.text = bus.distance.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ticketFb.setImageResource(R.drawable.ticket_green)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
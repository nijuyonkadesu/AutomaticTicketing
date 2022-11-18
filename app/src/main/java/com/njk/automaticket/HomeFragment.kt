package com.njk.automaticket

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.njk.automaticket.databinding.FragmentHomeBinding
import com.njk.automaticket.model.Bus
import com.njk.automaticket.viewmodels.UserViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val sharedViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        busDatabaseFetch()

        binding.testBtn.setOnClickListener {
            sharedViewModel.createNewFirebaseUser(requireContext())
        }
        binding.payTestBtn.setOnClickListener {
            sharedViewModel.initiatePayment(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun busDatabaseFetch(){
        val busDatabase = Firebase.database("https://esp-firebase-demo-f2096-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("user1")
        val busDatabaseListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val bus = dataSnapshot.getValue<Bus>()!!
                binding.paymentFb.text = getString(R.string.payment, bus.payment.toString())
                binding.distanceFb.text = getString(R.string.distance, bus.distance.toString())
                if(bus.ticket_status==0)
                    binding.ticketFb.setImageResource(R.drawable.ticket_grey)
                else
                    binding.ticketFb.setImageResource(R.drawable.ticket_green)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(com.njk.automaticket.viewmodels.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        busDatabase.addValueEventListener(busDatabaseListener)
    }
}
package com.njk.automaticket

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.ktx.database
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.njk.automaticket.databinding.FragmentHomeBinding
import com.njk.moveit.model.TicketStatus
import com.njk.moveit.model.Token
import com.njk.moveit.model.User
import com.njk.moveit.viewmodels.TAG
import com.njk.moveit.viewmodels.URL

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
//    val sharedViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val database = Firebase.database(URL).getReference("Users")

        Toast.makeText(requireContext(), "Home", Toast.LENGTH_SHORT).show()

        binding.testBtn.setOnClickListener {
            FirebaseInstallations.getInstance().id.addOnCompleteListener(
                OnCompleteListener { task ->
                    if(!task.isSuccessful) {
                        Log.w(TAG, "Fetching Unique ID failed", task.exception)
                        return@OnCompleteListener
                    }
                    val token = Token(task.result, createUser())
                    database.child(task.result).setValue(token)
                    context?.getSharedPreferences("_", FirebaseMessagingService.MODE_PRIVATE)?.edit()?.putString("id", task.result)?.apply()
                    Log.d("firebase", "new unique Token: ${task.result}")
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createUser(): User {
        return User(
            8787,
            1000,
            10,
            TicketStatus.INVALID,
        )
    }
}
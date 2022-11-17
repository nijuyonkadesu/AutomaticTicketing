package com.njk.automaticket

import android.content.Context.MODE_PRIVATE
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
import com.google.firebase.installations.remote.FirebaseInstallationServiceClient
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.njk.automaticket.databinding.FragmentHomeBinding
import com.njk.automaticket.model.UniqueId
import com.njk.moveit.model.TicketStatus
import com.njk.moveit.model.FcmToken
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
                    context?.getSharedPreferences("_", MODE_PRIVATE)?.edit()?.putString("id", task.result)?.apply()
                    Log.d("firebase", "new unique Token: ${task.result}")
                })
            // TODO: Implement the same in a separate class by extending
            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val fcm = FcmToken(task.result)
                    val user = createUser(fcm)
                    val id = context?.getSharedPreferences("_", MODE_PRIVATE)?.getString("id", "fail")!!

                    database.child(id).setValue(user)
                    context?.getSharedPreferences("_", FirebaseMessagingService.MODE_PRIVATE)?.edit()?.putString("fcm", task.result)?.apply()
                    Log.d("firebase", "new FCM token: ${task.result}")
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createUser(fcm: FcmToken): User {
        return User(
            8787,
            1000,
            10,
            TicketStatus.INVALID,
            fcm
        )
    }
}
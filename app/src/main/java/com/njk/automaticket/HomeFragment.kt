package com.njk.automaticket

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.location.*
import com.njk.automaticket.databinding.FragmentHomeBinding
import com.njk.automaticket.viewmodels.BusViewModel
import com.njk.automaticket.viewmodels.ProfileViewModel
import com.njk.automaticket.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.text.NumberFormat
import kotlin.math.abs
import kotlin.math.round

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var map : MapView? = null
    private var startPoint = GeoPoint(12.9830971, 79.9715665)
    lateinit var mapController: IMapController
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastLoction: Location? = null
    var marker: Marker? = null
    private var locationRequest: LocationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        1000,
    ).build()
    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                // Update UI with location data
                updateLocation(location) //MY function
            }
        }
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()
    private val busViewModel: BusViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        map = binding.map
        // Inflate Open Street Map
        inflateMap()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        CoroutineScope(Dispatchers.Main).launch {
            binding.apply {
                // fetch data and keep layout in sync with any change in firebase
                // Bus details
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
                // User Profile Details
                profileViewModel.updateTravelStats().observe(viewLifecycleOwner) {
                    seatsFb.text = getString(
                        R.string.seats,
                        it.travelCount.toString()
                    )
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        // this will refresh the osmdroid configuration on resuming.
        map?.onResume() //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        map?.onPause()  //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        map = null
    }
    private fun inflateMap(){
        initLocation()
        // This sets your app package name as useragent. With this,
        // osm downloads tiles and renders map
        Configuration.getInstance().userAgentValue = requireContext().packageName
//        val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), map);
//        locationOverlay.apply {
//            isMyLocationEnabled
//            mapController.setCenter(locationOverlay.myLocation)
//        }
        startLocationUpdates()
        map?.apply {
//            overlays.add(locationOverlay)
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
        }
        mapController = map!!.controller
        mapController.apply {
            setZoom(14.6)
        }
    }
    fun initLocation() { //call in create
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        readLastKnownLocation()
    }
    @SuppressLint("MissingPermission") //permission are checked before
    fun readLastKnownLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let { updateLocation(it) }
            }
    }
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() { //onResume
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
    private fun stopLocationUpdates() { //onPause
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    fun updateLocation(newLocation: Location) {
        lastLoction = newLocation
        //var currentPoint: GeoPoint = GeoPoint(newLocation.latitude, newLocation.longitude);
        startPoint.apply {
            longitude = newLocation.longitude
            latitude = newLocation.latitude
        }
        mapController.setCenter(startPoint)
        getPositionMarker().position = startPoint
        map?.invalidate()
    }
    private fun getPositionMarker(): Marker { // Singleton
        if (marker == null) {
            marker = Marker(map)
            marker!!.title = "Here I am"
            marker!!.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker!!.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_position);
            map!!.overlays.add(marker)
        }
        return marker!!
    }
    // TODO: Separate map logic
}
package com.latihan.latihansplashscreen

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var tvAddress: TextView
    private lateinit var tvCoordinates: TextView
    private var currentMarker: com.google.android.gms.maps.model.Marker? = null
    
    // Default location: Jakarta
    private val defaultLocation = LatLng(-6.2088, 106.8456)
    
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize views
        tvAddress = view.findViewById(R.id.tv_address)
        tvCoordinates = view.findViewById(R.id.tv_coordinates)
        
        // Initialize map
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        
        // Set default location with marker
        addMarker(defaultLocation, "Jakarta")
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))
        
        // Enable location if permission granted
        enableMyLocation()
        
        // Set map click listener
        mMap.setOnMapClickListener { latLng ->
            moveMarkerToLocation(latLng)
        }
        
        // Get initial address
        getAddressFromLocation(defaultLocation)
    }
    
    private fun addMarker(location: LatLng, title: String) {
        currentMarker?.remove()
        currentMarker = mMap.addMarker(
            MarkerOptions()
                .position(location)
                .title(title)
        )
    }
    
    private fun moveMarkerToLocation(latLng: LatLng) {
        // Move marker
        addMarker(latLng, "Lokasi Terpilih")
        
        // Animate camera
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        
        // Update coordinates display
        tvCoordinates.text = "Lat: %.4f, Lon: %.4f".format(latLng.latitude, latLng.longitude)
        
        // Get address
        getAddressFromLocation(latLng)
    }
    
    private fun getAddressFromLocation(latLng: LatLng) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val addressText = address.getAddressLine(0) ?: "Alamat tidak ditemukan"
                tvAddress.text = addressText
            } else {
                tvAddress.text = "Alamat tidak ditemukan"
            }
        } catch (e: IOException) {
            tvAddress.text = "Error mendapatkan alamat"
            e.printStackTrace()
        }
    }
    
    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            // Request permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation()
            } else {
                Toast.makeText(requireContext(), "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    // Options Menu for Map Type
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.map_type_normal -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                Toast.makeText(requireContext(), "Normal Map", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.map_type_satellite -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                Toast.makeText(requireContext(), "Satellite Map", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.map_type_terrain -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                Toast.makeText(requireContext(), "Terrain Map", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

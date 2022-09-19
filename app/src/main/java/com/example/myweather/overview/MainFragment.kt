package com.example.myweather.overview

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.myweather.adapter.WeatherAdapter
import com.example.myweather.databinding.FragmentMainBinding
import com.example.myweather.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.jar.Manifest


class MainFragment : Fragment() {

    private lateinit var myFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var binding: FragmentMainBinding
    private val adapter = WeatherAdapter()
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("test", "Приложение запущено")
        checkPermission()
        initProperties()
    }

    override fun onResume() {
        super.onResume()

        workWithGPS()
    }

    private fun checkPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                activity as AppCompatActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {

            }
            else -> {
                createCallback()

                requestPermissionLauncher.launch(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    private fun createCallback() {

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("perm", "Есть разрешение")
            } else {
                Toast.makeText(activity, "Should too allow, down", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initProperties() {
        myFusedLocationProviderClient = getFusedLocationProviderClient(requireContext())
        Log.i("test", "Поля инициализированы")

        viewModel.hoursList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            Log.i("test", "Список обновлен")
        }

        binding.daysRecycler.adapter = adapter
    }

    private fun workWithGPS() {
        if(isLocationEnable()) {
            getCurrentLocation()
        } else {
            turnOnLocation()
        }
    }

    private fun isLocationEnable(): Boolean {
        val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        myFusedLocationProviderClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
            .addOnCompleteListener{
                Log.i("geo", "${it.result.latitude} || ${it.result.longitude}")
            }
    }

    private fun turnOnLocation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("GPS")
            .setMessage("GPS")
            .setCancelable(false)
            .setNegativeButton("POX") { _, _ ->
                //TODO work without GPS
            }
            .setPositiveButton("OK") { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .show()
    }

}
package com.example.myweather.overview

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myweather.R
import com.example.myweather.adapter.WeatherAdapter
import com.example.myweather.databinding.FragmentMainBinding
import com.example.myweather.network.repository.Repository
import com.example.myweather.overview.viewmodel.MainViewModel
import com.example.myweather.overview.viewmodel.MainViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*
import java.lang.Exception


class MainFragment : Fragment() {

    private lateinit var myFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private val adapter = WeatherAdapter()

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
        checkPermission()
        initProperties()
        initBottoms()
    }

    private fun checkPermission() {

        if (ContextCompat
                .checkSelfPermission(activity as AppCompatActivity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            createCallback()

            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun createCallback() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(activity, getString(R.string.unavailable_features), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initProperties() {
        myFusedLocationProviderClient = getFusedLocationProviderClient(requireContext())

        setViewModel()

        setObserves()

        binding.daysRecycler.adapter = adapter
    }

    private fun setViewModel() {
        val viewModelFactory = MainViewModelFactory(Repository())
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }
    private fun setObserves() {

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), getString(it), Toast.LENGTH_SHORT).show()
        }

        viewModel.hoursList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.showedData.observe(viewLifecycleOwner) {
            binding.apply {
                cityText.text = it.location
                weatherText.text = it.weatherTitle
                weatherIcon.load(it.weatherIcon.toUri().buildUpon().scheme("https").build())
                dateText.text = it.date
                localTimeText.text = it.localTime

                if (it.isToday) {
                    degreesText.text = it.currentTemp
                    localTimeText.visibility = View.VISIBLE
                } else {
                    localTimeText.visibility = View.INVISIBLE
                    degreesText.text = "${it.minTemp}/${it.maxTemp}"
                }
            }
        }
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
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(requireContext(), getString(R.string.allow_location_tracking), Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(requireContext(), getString(R.string.getting_location), Toast.LENGTH_SHORT).show()
        myFusedLocationProviderClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
            .addOnCompleteListener{
                viewModel.setCity("${it.result.latitude},${it.result.longitude}")
            }
    }

    private fun turnOnLocation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("GPS")
            .setMessage(getString(R.string.turn_on))
            .setCancelable(false)
            .setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(activity, getString(R.string.unavailable_features), Toast.LENGTH_LONG).show()
            }
            .setPositiveButton("OK") { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .show()
    }

    private fun inputCity() {
        val editText = EditText(requireContext())
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("City name")
            .setView(editText)
            .setNegativeButton("Cancel") { _, _ ->

            }
            .setPositiveButton("Search") { _, _ ->
                viewModel.setCity(editText.text.toString())
            }
            .show()
    }

    private fun initBottoms() {

        binding.updateButton.setOnClickListener {

            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    viewModel.updateData()
                }
            }
        }

        binding.searchByCityButton.setOnClickListener {
            inputCity()

            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    viewModel.updateData()
                }
            }

        }

        binding.showNextDay.setOnClickListener {
            viewModel.changeDate()
        }

        binding.myLocationButton.setOnClickListener {
            if (ContextCompat
                    .checkSelfPermission(activity as AppCompatActivity,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        workWithGPS()
                        viewModel.updateData()
                    }
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.allow_location_tracking), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
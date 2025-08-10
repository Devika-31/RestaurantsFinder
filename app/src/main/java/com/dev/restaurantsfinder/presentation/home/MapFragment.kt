package com.innov.hrms.presentation.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.restaurantsfinder.R
import com.dev.restaurantsfinder.databinding.HomeFragmentBinding
import com.dev.restaurantsfinder.domain.model.restaurants.GetRestaurantRequestModel
import com.dev.restaurantsfinder.domain.model.restaurants.RestaurantDataModel
import com.dev.restaurantsfinder.presentation.restaurants.adapter.RestaurantAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.dev.restaurantsfinder.base.BaseFragment
import com.dev.restaurantsfinder.domain.viewmodel.user.RestaurantsViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapFragment : BaseFragment(), OnMapReadyCallback, RestaurantAdapter.OnItemClick {

    private var selectedRadius = 500
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private var updatedLat: Double = 0.0
    private var updatedLong: Double = 0.0

    private val restaurantsViewModel: RestaurantsViewModel by viewModel()
    private var restaurantsList: ArrayList<RestaurantDataModel> = arrayListOf()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var googleMap: GoogleMap? = null
    private lateinit var restaurantsAdapter: RestaurantAdapter

    private var isLoadingMore = false
    private var offset = 0
    private val limit = 20
    private var isResetApi = false

    private val requestLocationPermissionLauncher =
        registerForActivityResult(RequestPermission()) { isGranted ->
            if (isGranted) {
                checkLocationPermissionAndFetch()
            } else {
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(binding.mapFragment.id) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        setUpObserver()
        setUpAdapter()
        setUpListeners()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        checkLocationPermissionAndFetch()
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun checkLocationPermissionAndFetch() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap?.isMyLocationEnabled = true
            fetchAndZoomToLocation()
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchAndZoomToLocation() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000)
            .setMaxUpdates(1)
            .build()

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val loc = locationResult.lastLocation ?: return
                    updatedLat = loc.latitude
                    updatedLong = loc.longitude

                    val currentLatLng = LatLng(updatedLat, updatedLong)
                    googleMap?.clear()
                    googleMap?.addMarker(MarkerOptions().position(currentLatLng).title("You are here"))
                    googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                    callFetchRestApi()
                    fusedLocationProviderClient.removeLocationUpdates(this)
                }
            },
            Looper.getMainLooper()
        )
    }

    private fun setZoomMap(latLng: LatLng, zoomLevel: Float = 15f) {
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(zoomLevel)
            .bearing(0f)
            .tilt(30f)
            .build()
        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun setUpObserver() {
        binding.apply {
            with(restaurantsViewModel) {
                getRestaurantsResponseData.observe(requireActivity()) {
                    if (it.businesses.isNotEmpty()) {
                        rvRestaurants.visibility = VISIBLE
                        noDataLayout.root.visibility = GONE
                        if (offset == 0) restaurantsList.clear()
                        restaurantsList.addAll(it.businesses)
                        setUpAdapter()
                        offset += limit
                        isLoadingMore = false
                    } else {
                        if (offset == 0) {
                            showToast(message = getString(R.string.data_not_found))
                            rvRestaurants.visibility = GONE
                            noDataLayout.root.visibility = VISIBLE
                        }
                        isLoadingMore = false
                    }
                }
                messageData.observe(requireActivity()) {
                    rvRestaurants.visibility = GONE
                    noDataLayout.root.visibility = VISIBLE
                    showToast(message = it ?: "")
                }
            }
        }
    }

    private fun callFetchRestApi(isLoadMore: Boolean = false) {
        if (requireContext().isNetworkAvailable()) {
            if (!isLoadMore) offset = 0

            lifecycleScope.launch {
                delay(300) // debounce
                restaurantsViewModel.callFetchRestApi(
                    request = GetRestaurantRequestModel(
                        latitude = if (isResetApi) null else updatedLat,
                        longitude = if (isResetApi) null else updatedLong,
                        radius = selectedRadius,
                        offset = offset,
                        limit = limit
                    )
                )
            }
        } else {
            showToast(message = getString(R.string.no_internet_connection_found_check_your_connection_or_try_again))
            isLoadingMore = false
        }
    }

    private fun setUpListeners() {
        binding.apply {
            rvRestaurants.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(rv, dx, dy)
                    val lm = rv.layoutManager as LinearLayoutManager
                    val visible = lm.childCount
                    val total = lm.itemCount
                    val first = lm.findFirstVisibleItemPosition()

                    if (!isLoadingMore && dy > 0 && (visible + first) >= (total - 3)) {
                        isLoadingMore = true
                        offset += limit
                        callFetchRestApi(isLoadMore = true)
                    }
                }
            })
            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = false
                isResetApi = true
                callFetchRestApi()
            }
            seekRadius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    selectedRadius = if (progress < 500) 500 else progress
                    val radiusText = if (selectedRadius >= 1000) {
                        String.format("Radius: %.1f km", selectedRadius / 1000.0)
                    } else {
                        "Radius: $selectedRadius m"
                    }
                    tvRadiusValue.text = radiusText
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    offset = 0
                    callFetchRestApi()
                }
            })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpAdapter() {
        if (::restaurantsAdapter.isInitialized) {
            restaurantsAdapter.notifyDataSetChanged()
        } else {
            restaurantsAdapter = RestaurantAdapter(items = restaurantsList, listener = this)
            binding.rvRestaurants.adapter = restaurantsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun Context.isNetworkAvailable(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
        val networks = connectivityManager.allNetworks
        var hasInternet = false
        if (networks.isNotEmpty()) {
            for (network in networks) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                if (networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
                    hasInternet = true
                }
            }
        }
        return hasInternet
    }
}

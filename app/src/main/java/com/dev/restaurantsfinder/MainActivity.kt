package com.dev.restaurantsfinder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dev.restaurantsfinder.databinding.ActivityMainBinding
import com.dev.restaurantsfinder.presentation.all_restaurants.AllRestaurantsFragment
import com.innov.hrms.presentation.home.MapFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_main)
        //chucker
        askNotificationPermission()
      setUpBottomNav()

    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setUpBottomNav() {
        binding.apply {
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment, MapFragment())
                            .commit()
                        true
                    }

//                    R.id.all_restaurants -> {
//                        supportFragmentManager.beginTransaction()
//                            .replace(R.id.nav_host_fragment, AllRestaurantsFragment())
//                            .commit()
//                        true
//                    }

//                    R.id.nav_profile -> {
//                        supportFragmentManager.beginTransaction()
//                            .replace(R.id.nav_host_fragment, AllRestaurantsFragment())
//                            .commit()
//                        true
//                    }

                    else -> false
                }
            }


            bottomNavigationView.selectedItemId = R.id.nav_home
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted
//                Toast.makeText(this, "Notifications enabled", Toast.LENGTH_SHORT).show()
            } else {
                // Permission denied
//                Toast.makeText(this, "Notifications disabled", Toast.LENGTH_SHORT).show()
            }
        }
}
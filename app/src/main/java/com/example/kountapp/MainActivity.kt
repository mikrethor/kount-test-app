package com.example.kountapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.kountapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.kount.api.analytics.AnalyticsCollector
import java.util.UUID

private const val MY_UUID = "my-uuid"

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var deviceSessionID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkLocationPermission()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestLocationPermission()
        } else {

            Log.d("TAG", "success completed with sessionId ")

        }
        // REQUIRED SECTION
        AnalyticsCollector.setMerchantId(666)
        // END REQUIRED SECTION

        //This turns the alpha collections on(true)/off(false). It defaults to true.
        AnalyticsCollector.collectAnalytics(true)

        //For production need to add AnalyticsCollector.ENVIRONMENT_PRODUCTION.
        AnalyticsCollector.setEnvironment(AnalyticsCollector.ENVIRONMENT_TEST)

        // OPTIONAL SESSION_ID SECTION

        if (savedInstanceState == null) {
            deviceSessionID = UUID.randomUUID().toString().replace("-", "")
            AnalyticsCollector.setSessionId(deviceSessionID)
        } else {
            deviceSessionID = savedInstanceState.getString(MY_UUID)!!
            AnalyticsCollector.setSessionId(deviceSessionID)
        }


        //Request location permission for Android 6.0 & above

        AnalyticsCollector.collectDeviceDataForSession(this)

        AnalyticsCollector.trackLoginEvent(true)
        AnalyticsCollector.collectAnalyticsForScreen(true)
        AnalyticsCollector.collectDeviceDataForSession(this);
        AnalyticsCollector.collectBatteryInfo(true)

        val sessionId = AnalyticsCollector.getSessionId()









        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == AnalyticsCollector.REQUEST_PERMISSION_LOCATION) {
            AnalyticsCollector.collectDeviceDataForSession(this)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        if (granted) {
            AnalyticsCollector.collectDeviceDataForSession(this)
        } else {
            // Permission refusée, gérer le cas
            println("erreur")
        }
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            AnalyticsCollector.collectDeviceDataForSession(this)
        } else {
            requestLocationPermission()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(MY_UUID, deviceSessionID)
    }
}
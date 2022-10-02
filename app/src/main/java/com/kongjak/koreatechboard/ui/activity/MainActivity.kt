package com.kongjak.koreatechboard.ui.activity

import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.kongjak.koreatechboard.R
import com.kongjak.koreatechboard.databinding.ActivityMainBinding
import com.kongjak.koreatechboard.ui.fragment.ArchFragment
import com.kongjak.koreatechboard.ui.fragment.CseFragment
import com.kongjak.koreatechboard.ui.fragment.DormFragment
import com.kongjak.koreatechboard.ui.fragment.EmcFragment
import com.kongjak.koreatechboard.ui.fragment.IdeFragment
import com.kongjak.koreatechboard.ui.fragment.IteFragment
import com.kongjak.koreatechboard.ui.fragment.MechanicalFragment
import com.kongjak.koreatechboard.ui.fragment.MechatronicsFragment
import com.kongjak.koreatechboard.ui.fragment.SchoolFragment
import com.kongjak.koreatechboard.ui.fragment.SimFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var defaultFragment: Fragment
    lateinit var fragment: Fragment

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
        }

        override fun onLost(network: Network) {
            runOnUiThread {
                finishApplication()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_nav_menu)
        }

        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }
        val schoolFragment = SchoolFragment()
        val cseFragment = CseFragment()
        val mechanicalFragment = MechanicalFragment()
        val mechatronicsFragment = MechatronicsFragment()
        val iteFragment = IteFragment()
        val ideFragment = IdeFragment()
        val archFragment = ArchFragment()
        val emcFragment = EmcFragment()
        val simFragment = SimFragment()
        val dormFragment = DormFragment()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        when (prefs.getString("default_board", "school")) {
            "school" -> {
                defaultFragment = schoolFragment
                binding.navView.setCheckedItem(R.id.nav_drawer_school)
            }
            "cse" -> {
                defaultFragment = cseFragment
                binding.navView.setCheckedItem(R.id.nav_drawer_cse)
            }
            "dorm" -> {
                defaultFragment = dormFragment
                binding.navView.setCheckedItem(R.id.nav_drawer_dorm)
            }
            "mechanical" -> {
                defaultFragment = mechanicalFragment
                binding.navView.setCheckedItem(R.id.nav_drawer_mechanical)
            }
            "mechatronics" -> {
                defaultFragment = mechatronicsFragment
                binding.navView.setCheckedItem(R.id.nav_drawer_mechatronics)
            }
            "ite" -> {
                defaultFragment = iteFragment
                binding.navView.setCheckedItem(R.id.nav_drawer_ite)
            }
            "ide" -> {
                defaultFragment = ideFragment
                binding.navView.setCheckedItem(R.id.nav_drawer_ide)
            }
            "arch" -> {
                defaultFragment = archFragment
                binding.navView.setCheckedItem(R.id.nav_drawer_arch)
            }
            "emc" -> {
                defaultFragment = emcFragment
                binding.navView.setCheckedItem(R.id.nav_drawer_emc)
            }
            "sim" -> {
                defaultFragment = simFragment
                binding.navView.setCheckedItem(R.id.nav_drawer_sim)
            }
            else -> {
                defaultFragment = schoolFragment
                binding.navView.setCheckedItem(R.id.nav_drawer_school)
            }
        }

        fragment = if (savedInstanceState == null) {
            defaultFragment
        } else {
            supportFragmentManager.getFragment(savedInstanceState, "fragment")!!
        }

        if (isNetworkConnected())
            loadFragment()
        else
            finishApplication()

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            binding.drawerLayout.close()
            when (menuItem.itemId) {
                R.id.nav_drawer_school -> {
                    fragment = schoolFragment
                    loadFragment()
                }
                R.id.nav_drawer_dorm -> {
                    fragment = dormFragment
                    loadFragment()
                }
                R.id.nav_drawer_cse -> {
                    fragment = cseFragment
                    loadFragment()
                }
                R.id.nav_drawer_mechanical -> {
                    fragment = mechanicalFragment
                    loadFragment()
                }
                R.id.nav_drawer_mechatronics -> {
                    fragment = mechatronicsFragment
                    loadFragment()
                }
                R.id.nav_drawer_ite -> {
                    fragment = iteFragment
                    loadFragment()
                }
                R.id.nav_drawer_ide -> {
                    fragment = ideFragment
                    loadFragment()
                }
                R.id.nav_drawer_arch -> {
                    fragment = archFragment
                    loadFragment()
                }
                R.id.nav_drawer_emc -> {
                    fragment = emcFragment
                    loadFragment()
                }
                R.id.nav_drawer_sim -> {
                    fragment = simFragment
                    loadFragment()
                }
                R.id.nav_drawer_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
        binding.lifecycleOwner = this
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    private fun finishApplication() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.network_unavailable)
            .setCancelable(false)
            .setPositiveButton(R.string.finish) { _, _ ->
                finish()
            }
        builder.show()
    }

    private fun loadFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_frame_layout, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    override fun onPause() {
        super.onPause()
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        supportFragmentManager.putFragment(outState, "fragment", fragment)
    }
}

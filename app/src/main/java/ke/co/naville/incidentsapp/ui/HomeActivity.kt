package ke.co.naville.incidentsapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ke.co.naville.incidentsapp.R
import ke.co.naville.incidentsapp.databinding.ActivityHomeBinding
import ke.co.naville.incidentsapp.util.invisible
import ke.co.naville.incidentsapp.util.visible
import ke.co.naville.incidentsapp.viewmodel.AuthViewModel
import ke.co.naville.incidentsapp.viewmodel.FirestoreViewModel

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val authViewViewModel: AuthViewModel by viewModels()

    private val fireStoreViewModel: FirestoreViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        fireStoreViewModel.getUserName(auth.currentUser?.uid!!)
        observeUsername()

        setSupportActionBar(binding.appBarNavDrawer.toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setNavigationItemSelectedListener(this)
        binding.navView.setCheckedItem(R.id.my_incidents)

        binding.appBarNavDrawer.postIncident.setOnClickListener {
            navController.navigate(
                R.id.action_global_postIncidentFragment
            )
        }
    }

    private fun observeUsername() {
        val header = binding.navView.getHeaderView(0)
        val msgTV = header.findViewById<TextView>(R.id.welcome_message)
        fireStoreViewModel.usernameRes.observe(this) {
            msgTV.text = "Welcome $it!"
        }
    }

    override fun onResume() {
        super.onResume()
        showFab()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun hideFab() {
        binding.appBarNavDrawer.postIncident.invisible()
    }

    fun showFab() {
        binding.appBarNavDrawer.postIncident.visible()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return when(item.itemId) {
            R.id.my_incidents -> {
                findNavController(R.id.nav_host_fragment)
                    .navigate(R.id.action_global_myIncidentsFragment)
                true
            }

            R.id.all_incidents -> {
                findNavController(R.id.nav_host_fragment)
                    .navigate(R.id.action_global_allIncidentsFragment)
                true
            }

            R.id.profile -> {
                findNavController(R.id.nav_host_fragment)
                    .navigate(R.id.action_global_profileFragment)
                true
            }

            R.id.action_logout -> {
                authViewViewModel.logOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> true
        }
    }
}
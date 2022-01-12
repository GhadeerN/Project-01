package sa.edu.tuwaiq.project_01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import sa.edu.tuwaiq.project_01.databinding.ActivityMainBinding
import sa.edu.tuwaiq.project_01.util.BottomAppBarHelper
import sa.edu.tuwaiq.project_01.views.AddPostActivity


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the bottom app helper to allow hiding/showing it on specific fragments
        BottomAppBarHelper.init(this)

        /* This line is to hide the shadow for the bottom nav view. The shadow will still appear
          in the xml, but it will disappear when we run the app */
        binding.bottomNavigationView.background = null

        // This line is to disable the placeholder item in the bottom nav view. 1 -> placeholder item index
        binding.bottomNavigationView.menu.getItem(1).isEnabled = false


        // Navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        // to link the nav bottom with nav host
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        // Add post - Floating action button
        binding.addPostFloatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddPostActivity::class.java))
        }
    }

    // To activate the back button functionality
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
package ru.klyuv.sample

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.klyuv.core.common.extensions.observe
import ru.klyuv.core.common.extensions.setupWithNavController
import ru.klyuv.core.common.extensions.toGone
import ru.klyuv.core.common.extensions.toVisible
import ru.klyuv.core.common.ui.BaseActivity
import ru.klyuv.sample.databinding.ActivityMainBinding

class MainActivity : BaseActivity(){

    private val viewBinding: ActivityMainBinding by viewBinding(
        ActivityMainBinding::bind,
        R.id.mainContainer
    )

    private var currentNavController: LiveData<NavController>? = null

    override fun getLayoutID(): Int = R.layout.activity_main

    override fun setUI(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = viewBinding.bottomNavView
        val navGraphsId = listOf(R.navigation.menu_graph)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphsId,
            fragmentManager = supportFragmentManager,
            containerId = R.id.navHostController
        )

        observe(controller) {
            it.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.menuFragment,
                    R.id.barcodeListFragment -> showBottomNav()
                    else -> hideBottomNav()
                }
            }
        }
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean =
        currentNavController?.value?.navigateUp() ?: false

    private fun hideBottomNav() {
        viewBinding.bottomNavView.toGone()
    }

    private fun showBottomNav() {
        viewBinding.bottomNavView.toVisible()
    }
}
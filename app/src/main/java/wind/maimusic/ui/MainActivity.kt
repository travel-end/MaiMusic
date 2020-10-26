package wind.maimusic.ui

import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleActivity
import wind.maimusic.utils.NavigationManager
import wind.maimusic.utils.inflate
import wind.maimusic.vm.MainViewModel

class MainActivity : BaseLifeCycleActivity<MainViewModel>(),NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationViewHeader: View
    private lateinit var navigationView: NavigationView
    private var navigationManager: NavigationManager? = null
    override fun layoutResId()=R.layout.activity_main
    override fun initView() {
        super.initView()
        navigationView = navigation_view
        drawerLayout = drawer_layout
        navigationViewHeader = R.layout.navigation_header.inflate(navigationView)
        navigationView.addHeaderView(navigationViewHeader)
        navigationView.setNavigationItemSelectedListener(this)
        if (navigationManager == null) {
            navigationManager = NavigationManager(this)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawers()
        return navigationManager?.onNavigationItemSelected(item) ?: false
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

}
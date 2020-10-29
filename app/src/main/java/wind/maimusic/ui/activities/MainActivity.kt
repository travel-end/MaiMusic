package wind.maimusic.ui.activities

import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_main.*
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleActivity
import wind.maimusic.service.PlayerService
import wind.maimusic.utils.NavigationManager
import wind.maimusic.utils.SongUtil
import wind.maimusic.utils.inflate
import wind.maimusic.utils.isServiceRunning
import wind.maimusic.vm.MainViewModel
import wind.maimusic.widget.BottomPlayerView
import wind.widget.cost.Consts
import wind.widget.interf.OnPlayerViewClickListener
import wind.widget.model.Song

class MainActivity : BaseLifeCycleActivity<MainViewModel>(),
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationViewHeader: View
    private lateinit var navigationView: NavigationView
    private lateinit var bottomPlayerView: BottomPlayerView
    private var navigationManager: NavigationManager? = null
    private var currentSong:Song?=null
    override fun layoutResId() = R.layout.activity_main
    override fun initView() {
        super.initView()
        initWidget()
        initCurrentSong()
        startService()
        if (isServiceRunning(PlayerService::class.java.name)) {
            val playStatus = currentSong?.playStatus
            if (playStatus != null && playStatus == Consts.SONG_PLAY) {
                bottomPlayerView.startPlay()
            }
        }
    }

    private fun initCurrentSong() {
        currentSong = SongUtil.getSong()
        if (currentSong != null) {
            bottomPlayerView.setCurrentSong(currentSong!!)
        } else {
            mViewModel.findFirstMeetSongs()
        }
    }

    override fun initAction() {
        super.initAction()
        bottomPlayerView.setOnPlayerViewClickListener(object : OnPlayerViewClickListener {
            override fun onPlayerViewClick(view: View) {
            }

            override fun onPlayerBtnClick(view: View) {
            }

            override fun onPlayerSongListClick(view: View) {
            }
        })
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            firstMeetSongs.observe(this@MainActivity,Observer{
                it?.let {
                    if (it.isNotEmpty()) {
                        val firstMeetSong = it[0]
                        val song = SongUtil.assemblySong(firstMeetSong,SongUtil.SONG_FIRST_MEET)
//                        mFirstSong = song
                        bottomPlayerView.setCurrentSong(song)
                    }
                }
            })
        }
    }

    private fun initWidget() {
        navigationView = navigation_view
        drawerLayout = drawer_layout
        bottomPlayerView = main_bottom_player_view
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

    override fun onDestroy() {
        super.onDestroy()
        // 将播放的服务提升至前台服务
        val playIntent = Intent(this, PlayerService::class.java)
        // 退出程序后依然能够播放
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(playIntent)
        } else {
            startService(playIntent)
        }
    }
}
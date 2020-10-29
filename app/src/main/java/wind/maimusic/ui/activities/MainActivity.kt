package wind.maimusic.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
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
import wind.maimusic.utils.*
import wind.maimusic.vm.MainViewModel
import wind.maimusic.widget.BottomPlayerView
import wind.widget.cost.Consts
import wind.widget.interf.OnPlayerViewClickListener
import wind.widget.model.Song
import java.lang.ref.WeakReference

class MainActivity : BaseLifeCycleActivity<MainViewModel>(),
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationViewHeader: View
    private lateinit var navigationView: NavigationView
    private lateinit var bottomPlayerView: BottomPlayerView
    private lateinit var updateSeekBarHandler:UpdateSeekBarProgressHandler
    private var navigationManager: NavigationManager? = null
    private var currentSong:Song?=null
    private var flag:Boolean = false
    private var existLivingService:Boolean = false
    private var isRePlay:Boolean = false // 是否是退出后重新进入继续播放
    private var currentPlayProgress:Long = 0L
    override fun layoutResId() = R.layout.activity_main
    override fun initView() {
        super.initView()
        initWidget()
        initCurrentSong()
        if (isServiceRunning(PlayerService::class.java.name)) {
            val playStatus = currentSong?.playStatus
            if (playStatus != null && playStatus == Consts.SONG_PLAY) {
                bottomPlayerView.startPlay()
                existLivingService = true
            }
        }
        startService()
    }

    private fun initCurrentSong() {
        currentSong = SongUtil.getSong()
        if (currentSong != null) {
            currentPlayProgress = currentSong!!.currentTime
            LogUtil.e("--->mainActivity currentPlayProgress:$currentPlayProgress")
            bottomPlayerView.setCurrentSong(currentSong!!)
        } else {
            mViewModel.findFirstMeetSongs()
        }
    }

    override fun serviceConnection() {
        super.serviceConnection()
        if (existLivingService) {
            startSeekBarProgress()
        }
    }

    override fun initAction() {
        super.initAction()
        bottomPlayerView.setOnPlayerViewClickListener(object : OnPlayerViewClickListener {
            /* 点击进入播放页面*/
            override fun onPlayerViewClick(view: View) {
                val song = SongUtil.getSong()
                if (song != null) {
                    val playIntent = Intent(this@MainActivity,PlayActivity::class.java)
                    if (playerServiceBinder?.playing == true) {
                        // 保存当前播放进度
                        val progress = playerServiceBinder?.playingTime?:0 / 1000
                        song.currentTime = progress.toLong()
                        SongUtil.saveSong(song)
                        playIntent.putExtra(Consts.PLAY_STATUS,Consts.SONG_PLAY)
                    } else {// 暂停
                        song.currentTime = bottomPlayerView.seekBarProgress.toLong()
                        playIntent.putExtra(Consts.PLAY_STATUS,Consts.SONG_PAUSE)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(
                            playIntent,
                            ActivityOptions.makeSceneTransitionAnimation(this@MainActivity)
                                .toBundle()
                        )
                    } else {
                        startActivity(playIntent)
                    }
                }
            }
            /* ****播放/暂停*/
            override fun onPlayerBtnClick(view: View) {
                when {
                    playerServiceBinder?.playing == true->{
                        playerServiceBinder?.pause()
                        flag = true
                    }
                    flag->{
                        playerServiceBinder?.resume()
                        flag = false
                    }
                    // 退出程序后重新进入的情况（包括首次打开app）
                     else ->{
                         val song = SongUtil.getSong()
                         if (song != null) {
                             isRePlay = true
                             if (song.isOnline) {
                                 playerServiceBinder?.playOnline((currentPlayProgress * 1000).toInt())
                             } else {
                                 if (currentPlayProgress != 0L) {
                                     playerServiceBinder?.play(song.listType,(currentPlayProgress* 1000).toInt())
                                 } else {
                                     playerServiceBinder?.play(song.listType)
                                 }
                             }
                         }
                     }
                }
            }
            /* 点击歌曲列表*/
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
                        isRePlay = true
                        val firstMeetSong = it[0]
                        val song = SongUtil.assemblySong(firstMeetSong,SongUtil.SONG_FIRST_MEET)
                        bottomPlayerView.setCurrentSong(song)
                        mViewModel.getSongPlayUrl(song)
                    }
                }
            })
            songPlayUrlResult.observe(this@MainActivity,Observer{
                it?.let {
                    val song = it.entries.find { entry ->
                        entry.key == "song"
                    }?.value as Song
                    val url = it.entries.find { entry ->
                        entry.key=="url"
                    }?.value as String
                    song.url = url
                    currentSong = song
                    SongUtil.saveSong(song)
                }
            })
        }
        /* *****切歌***/
        Bus.observe<Int>(Consts.SONG_STATUS_CHANGE,this) {status->
            when(status){
                Consts.SONG_CHANGE ->{
                    // TODO: 2020/10/29 如果是退出重新进的情况 只需要考虑播放 而不需要刷新bottomPlayView
                    currentSong = SongUtil.getSong()// 刷新当前播放的歌曲
                    if (currentSong != null) {
                        if (!isRePlay) {
                            bottomPlayerView.setCurrentSong(currentSong!!)
                        }
                        bottomPlayerView.startPlay()
                        startSeekBarProgress()
                    }
                }
                Consts.SONG_PAUSE->bottomPlayerView.pausePlay()
                Consts.SONG_RESUME->{
                    bottomPlayerView.resumePlay()
                    startSeekBarProgress()
                }
            }
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
        updateSeekBarHandler = UpdateSeekBarProgressHandler(this)
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

    private fun startSeekBarProgress() {
        updateSeekBarHandler.removeMessages(1)
        updateSeekBarHandler.sendEmptyMessageDelayed(1,1000)
    }

    private class UpdateSeekBarProgressHandler(content:MainActivity):Handler(Looper.getMainLooper()) {
        private val content: WeakReference<MainActivity> = WeakReference(content)
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            content.get()?.let {activity ->
                activity.run {
                    if (playerServiceBinder?.playing == true) {
                        val progress = playerServiceBinder?.playingTime?:0
                        bottomPlayerView.updateProgress(progress/1000)
                        startSeekBarProgress()
                    }
                }
            }
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
        bottomPlayerView.clearAnim()
        val song = SongUtil.getSong()
        song?.currentTime = ((playerServiceBinder?.playingTime?:0)/1000).toLong()
        if (flag) {
            song?.playStatus = Consts.SONG_PAUSE
        } else {
            song?.playStatus = Consts.SONG_PLAY
        }
        if (song != null) {
            SongUtil.saveSong(song)
        }
        updateSeekBarHandler.removeMessages(1)
        updateSeekBarHandler.removeCallbacksAndMessages(null)
    }
}
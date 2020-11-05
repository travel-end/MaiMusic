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

/**
 * 主界面
 */
class MainActivity : BaseLifeCycleActivity<MainViewModel>(),
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationViewHeader: View
    private lateinit var navigationView: NavigationView
    private lateinit var bottomPlayerView: BottomPlayerView
    private lateinit var updateSeekBarHandler: UpdateSeekBarProgressHandler
    private var navigationManager: NavigationManager? = null
    private var currentSong: Song? = null
    private var flag: Boolean = false
    private var playing: Boolean = false
    private var existLivingService: Boolean = false
    private var currentPlayProgress: Long = 0L
    override fun layoutResId() = R.layout.activity_main
    override fun initView() {
        super.initView()
        initWidget()
        initCurrentSong()
        if (isServiceRunning(PlayerService::class.java.name)) {
            val playStatus = currentSong?.playStatus
            LogUtil.e("------MainActivity ----isServiceRunning playStatus:$playStatus")
            if (playStatus != null && playStatus == Consts.SONG_PLAY) {
                bottomPlayerView.startPlay()
                existLivingService = true
            }
        }
    }

    /* 设置当前歌曲的播放信息*/
    private fun initCurrentSong() {
        currentSong = SongUtil.getSong()
        if (currentSong != null) {
            startService()
            currentPlayProgress = currentSong!!.currentTime
            bottomPlayerView.setCurrentSong(currentSong!!)
        } else {
            mViewModel.findLaunchSong()
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

        /* 点击下方进入播放页面*/
        bottomPlayerView.setOnPlayerViewClickListener(object : OnPlayerViewClickListener {
            override fun onPlayerViewClick(view: View) {
                val song = SongUtil.getSong()
                // 将当前播放歌曲的状态（播放/暂停）以及进度带到playActivity页面
                if (song != null) {
                    val playIntent = Intent(this@MainActivity, PlayActivity::class.java)
                    if (playerServiceBinder?.playing == true) {
                        val progress = (playerServiceBinder?.playingTime ?: 0) / 1000// 实时进度（秒）
                        song.currentTime = progress.toLong()
                        SongUtil.saveSong(song)
                        playIntent.putExtra(Consts.PLAY_STATUS, Consts.SONG_PLAY)
                    } else {// 暂停
                        song.currentTime = bottomPlayerView.seekBarProgress.toLong()
                        playIntent.putExtra(Consts.PLAY_STATUS, Consts.SONG_PAUSE)
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
                    playerServiceBinder?.playing == true -> {
                        playerServiceBinder?.pause()
                        flag = true
                    }
                    flag -> {
                        playerServiceBinder?.resume()
                        flag = false
                    }
                    // 退出程序后重新进入的情况（包括首次打开app） 由暂停进入播放的状态
                    else -> {
                        val song = SongUtil.getSong()
                        if (song != null) {
                            bottomPlayerView.setPlayBtnSelected(true)
                            LogUtil.e("--->mainActivity currentPlayProgress:$currentPlayProgress")
                            if (song.isOnline) {
                                playerServiceBinder?.playOnline((currentPlayProgress * 1000).toInt())
                            } else {
                                if (currentPlayProgress != 0L) {
                                    playerServiceBinder?.play(
                                        song.listType,
                                        (currentPlayProgress * 1000).toInt()
                                    )
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
            launchSong.observe(this@MainActivity, Observer {
                it?.let {
                    // 当前播放的是网络音乐
                    val song = SongUtil.assemblySong(it, Consts.ONLINE_FIRST_LAUNCH)
                    bottomPlayerView.setCurrentSong(song)
                    mViewModel.getSongPlayUrl(song)
                }
            })
            /*获取歌曲远程播放地址（只有在第一次打开app的时候调用）*/
            songPlayUrlResult.observe(this@MainActivity, Observer {
                it?.let {
                    val song = it.entries.find { entry ->
                        entry.key == "song"
                    }?.value as Song
                    val url = it.entries.find { entry ->
                        entry.key == "url"
                    }?.value as String
                    song.url = url
                    currentSong = song
                    SongUtil.saveSong(song)
                    startService()
                }
            })
        }
        Bus.observe<Int>(Consts.SONG_STATUS_CHANGE, this) { status ->
            when (status) {
                Consts.SONG_CHANGE -> {
                    currentSong = SongUtil.getSong()// 刷新当前播放的歌曲
                    if (currentSong != null) {
                        bottomPlayerView.setCurrentSong(currentSong!!)
                        bottomPlayerView.startPlay()
//                        bottomPlayerView.startCoverRotation()
                        startSeekBarProgress()
                        playing = true
                    }
                }
                Consts.SONG_PAUSE -> {
                    playing = false
                    bottomPlayerView.pausePlay()
                }
                Consts.SONG_RESUME -> {
                    playing = true
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
        updateSeekBarHandler.sendEmptyMessageDelayed(1, 1000)
    }

    private class UpdateSeekBarProgressHandler(content: MainActivity) :
        Handler(Looper.getMainLooper()) {
        private val content: WeakReference<MainActivity> = WeakReference(content)
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            content.get()?.let { activity ->
                activity.run {
                    if (playerServiceBinder?.playing == true) {
                        val progress = playerServiceBinder?.playingTime ?: 0
                        bottomPlayerView.updateProgress(progress / 1000)
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
        bottomPlayerView.clearAnim()// 这里停掉了  再次进入如果是播放状态 需要手动调用
        val song = SongUtil.getSong()
        song?.currentTime = ((playerServiceBinder?.playingTime ?: 0) / 1000).toLong()
//        if (flag) {
//            song?.playStatus = Consts.SONG_PAUSE
//        } else {
//            song?.playStatus = Consts.SONG_PLAY
//        }

        /* 记录播放的状态*/   // flag  false
        if (playing) {
            song?.playStatus = Consts.SONG_PLAY
        } else {
            song?.playStatus = Consts.SONG_PAUSE
        }
        SongUtil.saveSong(song)
        updateSeekBarHandler.removeMessages(1)
        updateSeekBarHandler.removeCallbacksAndMessages(null)
    }
}
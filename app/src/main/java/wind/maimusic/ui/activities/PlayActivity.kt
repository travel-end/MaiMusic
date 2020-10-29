package wind.maimusic.ui.activities

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.transition.Slide
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_play.*
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleActivity
import wind.maimusic.utils.BitmapUtil
import wind.maimusic.utils.LogUtil
import wind.maimusic.utils.SongUtil
import wind.maimusic.utils.isNotNullOrEmpty
import wind.maimusic.vm.PlayViewModel
import wind.maimusic.widget.PlayControllerView
import wind.widget.cost.Consts
import wind.widget.model.Song
import wind.widget.play.ImUtils
import wind.widget.play.bg.BlurBgLayout
import wind.widget.play.bg.DiscView
import wind.widget.play.lrc.LrcView
import wind.widget.utils.getScreenHeight
import wind.widget.utils.loadImg
import java.lang.ref.WeakReference
import kotlin.concurrent.thread

/**
 * @By Journey 2020/10/29
 * @Description
 */
class PlayActivity : BaseLifeCycleActivity<PlayViewModel>() {
    private var currentPlayStatus: Int = -1
    private var currentSong: Song? = null
    private lateinit var discView: DiscView
    private lateinit var discCoverView: ImageView
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var lrcView: LrcView
    private lateinit var playRootLayout: BlurBgLayout
    private lateinit var playControllerView: PlayControllerView
    private lateinit var seekBar: SeekBar
    private lateinit var currentSongSinger: TextView
    private lateinit var currentSongName: TextView
    private var discCoverBitmap: Bitmap? = null
    private var playMode: Int = Consts.PLAY_ORDER
    private var playType: Int = 0
    private var isOnlineSong: Boolean = false
    private var isDragSeekBar: Boolean = false
    private lateinit var updateProgressHandler: UpdateProgressHandler
    override fun layoutResId() = R.layout.activity_play
    override fun initStatusBar() {
        ImmersionBar
            .with(this)
            .statusBarView(R.id.top_view)
            .transparentBar()
            .fullScreen(true)
            .init()
    }

    override fun initView() {
        super.initView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition = Slide()
            window.exitTransition = Slide()
        }
        intiWidget()
        bindService()
        initSongUi()
    }

    private fun initSongUi() {
        currentSong = SongUtil.getSong()
        currentSong?.run {
            playType = listType
            currentSongSinger.text = singer
            currentSongName.text = songName
            LogUtil.e("--->当前播放进度：$currentTime")
        }
    }

    override fun serviceConnection() {
        super.serviceConnection()
        playMode = mViewModel.getPlayMode()
        playerServiceBinder?.setPlayModel(playMode)
        val song = SongUtil.getSong()
        if (song != null) {
            isOnlineSong = song.isOnline
            if (isOnlineSong) {
                val coverImgUrl = song.imgUrl
                if (coverImgUrl.isNotNullOrEmpty()) {
                    getOnlineCoverBitmap(coverImgUrl!!)
                }
                if (currentPlayStatus == Consts.SONG_PLAY) {
                    discView.play()
                    playControllerView.setPlay()
                }
            } else {// 不是网络歌曲
                seekBar.secondaryProgress = song.duration
            }
        }
        playerServiceBinder?.mp?.setOnBufferingUpdateListener { _, percent ->
            seekBar.secondaryProgress =
                percent * seekBar.progress
        }
    }

    private fun intiWidget() {
        playRootLayout = play_root_layout//背景
        discView = play_disk_view as DiscView// 唱碟
        discCoverView = discView.findViewById(R.id.iv_disc_background)// 唱碟封面
        lrcView = lrc_view// 歌词
        volumeSeekBar = findViewById(R.id.sb_volume)// 音量
        playControllerView = play_controller_view// 播放控制器
        currentSongSinger = play_tv_song_singer
        currentSongName = play_tv_song_name
        seekBar = playControllerView.progressSeekBar
        updateProgressHandler = UpdateProgressHandler(this)
    }

    private fun getOnlineCoverBitmap(coverUrl: String) {
        loadImg(
            context = this,
            url = coverUrl,
            block = {
                discCoverBitmap = (it as BitmapDrawable).bitmap
                if (discCoverBitmap != null) {
                    setDiscViewCover(discCoverBitmap!!)
                    setPlayBackground(discCoverBitmap!!)
                }
                if (!isOnlineSong) {

                }

            })
    }

    private fun setPlayBackground(bitmap: Bitmap) {
        thread {
            val drawable = BitmapUtil.getForegroundDrawable(this, bitmap)
            runOnUiThread {
                playRootLayout.setBgForeground(drawable)
                playRootLayout.beginAnimation()
            }
        }
    }

    private fun setDiscViewCover(bitmap: Bitmap) {
        discCoverView.setImageDrawable(discView.getDiscDrawable(bitmap))
        val marginTop = (ImUtils.SCALE_DISC_MARGIN_TOP * getScreenHeight(this)).toInt()
        val lp = discCoverView.layoutParams as RelativeLayout.LayoutParams
        lp.setMargins(0, marginTop, 0, 0)
        discCoverView.layoutParams = lp
    }

    private fun updatePlayProgress() {
        updateProgressHandler.removeMessages(0)
        updateProgressHandler.sendEmptyMessageDelayed(0, 300)
    }

    private class UpdateProgressHandler(content: PlayActivity) : Handler(Looper.getMainLooper()) {
        private val content: WeakReference<PlayActivity> = WeakReference(content)
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            content.get()?.let { activity ->
                activity.run {
                    if (!isDragSeekBar) {
                        // 当前播放进度 单位：毫秒
                        val progress = playerServiceBinder?.playingTime
                        if (progress != null) {
                            val sceProgress = progress / 1000 // 秒
                            playControllerView.updatePlay(sceProgress)
                            updatePlayProgress()
                            if (lrcView.hasLrc()) {
                                lrcView.updateTime(progress.toLong())
                            }
                        }
                    }
                }
            }
        }
    }
}
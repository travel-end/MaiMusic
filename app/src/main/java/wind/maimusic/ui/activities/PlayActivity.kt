package wind.maimusic.ui.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.transition.Slide
import android.view.animation.LinearInterpolator
import android.widget.*
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_play.*
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleActivity
import wind.maimusic.utils.*
import wind.maimusic.vm.PlayViewModel
import wind.maimusic.widget.PlayControllerView
import wind.widget.cost.Consts
import wind.widget.model.Song
import wind.widget.play.ImUtils
import wind.widget.play.bg.BlurBgLayout
import wind.widget.play.bg.DiscView
import wind.widget.play.lrc.LrcView
import wind.widget.utils.fastClickListener
import wind.widget.utils.getScreenHeight
import wind.widget.utils.loadImg
import java.lang.ref.WeakReference
import kotlin.concurrent.thread

/**
 * @By Journey 2020/10/29
 * @Description
 */
class PlayActivity : BaseLifeCycleActivity<PlayViewModel>() {
    private lateinit var discView: DiscView
    private lateinit var discCoverView: ImageView
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var lrcView: LrcView
    private lateinit var playRootLayout: BlurBgLayout
    private lateinit var playControllerView: PlayControllerView
    private lateinit var seekBar: SeekBar
    private lateinit var currentSongSinger: TextView
    private lateinit var currentSongName: TextView
    private lateinit var ivPlayModel: ImageView
    private lateinit var lLLrcContainer: LinearLayout
    private lateinit var lLFunction: LinearLayout
    private lateinit var audioManager: AudioManager
    private lateinit var updateProgressHandler: UpdateProgressHandler
    private var currentPlayStatus: Int? = null
    private var currentSong: Song? = null
    private var hideAlphaAnim: ObjectAnimator? = null
    private var showAlphaAnim: ObjectAnimator? = null
    private var discCoverBitmap: Bitmap? = null
    private var playMode: Int = Consts.PLAY_ORDER
    private var playType: Int = 0
    private var isOnlineSong: Boolean = false
    private var isDragSeekBar: Boolean = false
    private var hasInitLrc:Boolean = false
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
        currentPlayStatus = intent?.getIntExtra(Consts.PLAY_STATUS, 2)
        intiWidget()
        bindService()
        initPlayUi()
    }

    override fun initData() {
        super.initData()
        if (currentPlayStatus == Consts.SONG_PLAY) {
            discView.play()
            playControllerView.setPlayBtnSelected(true)
            updatePlayProgress()
        }
        discView.fastClickListener {
            hideAlphaAnim?.start()
        }
        hideAlphaAnim?.addListener(object :AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                if (!isOnlineSong) {

                } else {// 在线歌词
                    if (hasInitLrc) {
                        switchCoverLrc(false)
                    } else {// 获取在线歌词
                        val songId = currentSong?.songId
                        lrcView.setLabel(R.string.query_lrc.getStringRes())
                        switchCoverLrc(false)
                        if (songId.isNotNullOrEmpty()) {

                        }
                    }
                }
            }
        })
    }

    private fun initPlayUi() {
        currentSong = SongUtil.getSong()
        currentSong?.run {
            playType = listType
            currentSongSinger.text = singer
            currentSongName.text = songName
            LogUtil.e("------PlayActivity initSongUi currentPlayProgress：$currentTime")
            playControllerView.setCurrentSong(this)
        }
        initPlayMode()
        initLrcView()
        initVolume()
        switchCoverLrc(true)
    }

    /* 初始化播放模式*/
    private fun initPlayMode() {
        playMode = mViewModel.getPlayMode()
        playerServiceBinder?.setPlayModel(playMode)
        ivPlayModel.setImageLevel(playMode)
    }

    /* 初始化歌词设置*/
    private fun initLrcView() {
        /* true:支持手動拖动 回调：滚动至某处，点击左边播放按钮时触发*/
        lrcView.setDraggable(true) { _, time ->
            playerServiceBinder?.mp?.seekTo(time.toInt())
            playerServiceBinder?.resume()
            updatePlayProgress()
            playControllerView.setPlayBtnSelected(true)
            true
        }
        /* 点击歌词*/
        lrcView.setOnTapListener { _, _, _ ->
            switchCoverLrc(true)
            showAlphaAnim?.start()
        }
    }

    /* 切换唱碟和歌词的显示 默认显示唱碟*/
    private fun switchCoverLrc(showCover: Boolean) {
        if (showCover) {
            lLLrcContainer.gone()
            discView.visible()
            lLFunction.visible()
        } else {
            lLLrcContainer.visible()
            discView.gone()
            lLFunction.gone()
        }
    }

    /* 初始化音量设置*/
    private fun initVolume() {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        volumeSeekBar.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        volumeSeekBar.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }

    override fun serviceConnection() {
        super.serviceConnection()
        playMode = mViewModel.getPlayMode()
        playerServiceBinder?.setPlayModel(playMode)
        val song = SongUtil.getSong()
        if (song != null) {
            isOnlineSong = song.isOnline
            LogUtil.e("------PlayActivity serviceConnection isOnlineSong：$isOnlineSong")
            if (isOnlineSong) {
                val coverImgUrl = song.imgUrl
                if (coverImgUrl.isNotNullOrEmpty()) {
                    getOnlineCoverBitmap(coverImgUrl!!)
                }
                if (currentPlayStatus == Consts.SONG_PLAY) {
                    discView.play()
                    playControllerView.setPlayBtnSelected(true)
                    updatePlayProgress()
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
        ivPlayModel = findViewById(R.id.iv_mode) // 播放模式
        playControllerView = play_controller_view// 播放控制器
        currentSongSinger = play_tv_song_singer
        currentSongName = play_tv_song_name
        lLLrcContainer = play_lrc_view
        lLFunction = play_other_fun
        seekBar = playControllerView.progressSeekBar
        updateProgressHandler = UpdateProgressHandler(this)
    }

    /* 设置唱碟封面图片 并且根据封面图片设置模糊的背景图片*/
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

    /* 设置播放背景为封面模糊图片*/
    private fun setPlayBackground(bitmap: Bitmap) {
        thread {
            val drawable = BitmapUtil.getForegroundDrawable(this, bitmap)
            runOnUiThread {
                playRootLayout.setBgForeground(drawable)
                playRootLayout.beginAnimation()
            }
        }
    }

    /* 设置唱碟封面图片*/
    private fun setDiscViewCover(bitmap: Bitmap) {
        discCoverView.setImageDrawable(discView.getDiscDrawable(bitmap))
        val marginTop = (ImUtils.SCALE_DISC_MARGIN_TOP * getScreenHeight(this)).toInt()
        val lp = discCoverView.layoutParams as RelativeLayout.LayoutParams
        lp.setMargins(0, marginTop, 0, 0)
        discCoverView.layoutParams = lp
    }

    /* 初始化唱碟显示和隐藏的透明度动画*/
    private fun initAlphaAnim() {
        if (hideAlphaAnim == null) {
            hideAlphaAnim = ObjectAnimator.ofFloat(discView, "alpha", 1.0f, 0.0f)
            hideAlphaAnim?.duration = 240
            hideAlphaAnim?.interpolator = LinearInterpolator()
        }
        if (showAlphaAnim == null) {
            showAlphaAnim = ObjectAnimator.ofFloat(discView, "alpha", 0f, 1.0f)
            showAlphaAnim?.duration = 240
            showAlphaAnim?.interpolator = LinearInterpolator()
        }
    }

    /* 更新歌曲播放的进度*/
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
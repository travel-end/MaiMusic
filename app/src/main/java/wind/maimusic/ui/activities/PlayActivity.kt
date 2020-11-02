package wind.maimusic.ui.activities

import android.animation.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.transition.Slide
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_play.*
import wind.maimusic.Constants
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
 * @Description 播放音乐界面
 */
class PlayActivity : BaseLifeCycleActivity<PlayViewModel>() {
    private lateinit var discView: DiscView
    private lateinit var discCoverView: ImageView
    private lateinit var ivLoveSong:ImageView
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var lrcView: LrcView
    private lateinit var playRootLayout: BlurBgLayout
    private lateinit var playControllerView: PlayControllerView
    private lateinit var seekBar: SeekBar
    private lateinit var currentSongSinger: TextView
    private lateinit var currentSongName: TextView
    private lateinit var ivPlayModel: ImageView
    private lateinit var ivDownload:ImageView
    private lateinit var ivCollect:ImageView
    private lateinit var ivSurround:ImageView
    private lateinit var lLLrcContainer: LinearLayout
    private lateinit var lLFunction: LinearLayout
    private lateinit var audioManager: AudioManager
    private lateinit var updateProgressHandler: UpdateProgressHandler
    private var addLoveSongAnim:AnimatorSet?=null
    private var currentPlayStatus: Int? = null
    private var currentSong: Song? = null
    private var hideAlphaAnim: ObjectAnimator? = null
    private var showAlphaAnim: ObjectAnimator? = null
//    private var loadingSongAnim:ValueAnimator?=null
    private var discCoverBitmap: Bitmap? = null
    private var playMode: Int = Consts.PLAY_ORDER
    private var playType: Int = 0
    private var isOnlineSong: Boolean = false
    private var isDraggingSeekBar: Boolean = false
    private var lrcStatus:Int = 1 // 1 即将去查找歌词 2 已经确定是否有歌词 3歌曲正在加载中 不能查看歌词
    private var pauseProgress:Int = 0
    private var hasDragSeekBarOnPause:Boolean = false
    private var flag:Boolean = false
    private var isLoveSong:Boolean = false
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
        /* 点击唱碟--->显示歌词*/
        discView.fastClickListener {
            hideAlphaAnim?.start()
        }
        hideAlphaAnim?.addListener(object :AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                if (!isOnlineSong) {// 获取非网络音乐的歌词
                    val localLrc = SongUtil.loadLocalLrcText(currentSong?.songName?:"maizi")
                    if (localLrc == null){
                        // 第一次本地是没有歌词的，拿刚进入页面后获取到的qqId也就是songid找与之对应的网络音乐的歌词
                        val qqId = currentSong?.qqId
                        LogUtil.e("------PlayActivity initData hideAlphaAnim qqId(songId)：$qqId")
                        when (qqId) {
                            R.string.lrc_empty.getStringRes() -> {
                                switchCoverLrc(false)
                                setLocalNetLrcError(null)
                            }
                            null -> {
                                mViewModel.matchLocalSongNetLrc(currentSong?.songName,currentSong?.duration?:-1)
                            }
                            else -> {
                                mViewModel.getOnlineLyric(qqId,Consts.SONG_LOCAL,currentSong?.songName?:"maizi")
                            }
                        }
                    }
                } else {// 获取在线歌词
                    if (lrcStatus == 2) {// 歌词已经查找到了
                        switchCoverLrc(false)
                    } else if (lrcStatus == 1) {// 获取在线歌词
                        val songId = currentSong?.songId
                        lrcView.setLabel(R.string.query_lrc.getStringRes())
                        switchCoverLrc(false)
                        if (songId.isNotNullOrEmpty()) {
                            mViewModel.getOnlineLyric(songId!!,Consts.SONG_LOCAL,currentSong?.songName?:"maizi")
                        }
                    } else if (lrcStatus ==3) {// 当前歌曲正在初始化（加载中）
                        lrcView.setLabel(R.string.query_lrc.getStringRes())
                    }
                }
            }
        })
        playControllerView.setOnPlayControllerViewClick(object :PlayControllerView.OnPlayControllerViewClick{
            override fun onClickPlayMode(v: View) {
            }
            /*上一首音乐*/
            override fun onClickPreviousSong(v: View) {
                setDiscViewDefaultCover()
//                startSeekBarLoading()
                playerServiceBinder?.last()
                playControllerView.setPlayBtnSelected(true)
                discView.last()
            }
            /*播放 or 暂停*/
            override fun onClickPlayButton(v: View) {
                when{
                    playerServiceBinder?.playing == true->{
                        playerServiceBinder?.pause()
                        updateProgressHandler.removeMessages(0)
                        flag = true
                        v.isSelected = false
                        discView.pause()
                    }
                    flag->{
                        playerServiceBinder?.resume()
                        flag = false
                        if (hasDragSeekBarOnPause) {
                            playerServiceBinder?.mp?.seekTo(pauseProgress * 1000)
                            hasDragSeekBarOnPause = false
                        }
                        discView.play()
                        v.isSelected = true
                        updatePlayProgress()
                    }
                    else ->{
                        val restartProgress = currentSong?.currentTime?:0
                        if (isOnlineSong) {
                            playerServiceBinder?.playOnline((restartProgress *1000).toInt())
                        } else {
                            if (restartProgress != 0L) {
                                playerServiceBinder?.play(playType,(restartProgress* 1000).toInt())
                            } else {
                                playerServiceBinder?.play(playType)
                            }
                        }
                        discView.play()
                        v.isSelected = true
                        updatePlayProgress()
                    }
                }
            }
            /*下一首音乐*/
            override fun onClickNextSong(v: View) {
                setDiscViewDefaultCover()
//                startSeekBarLoading()
                playerServiceBinder?.next()
                playControllerView.setPlayBtnSelected(playerServiceBinder?.playing == true)
                discView.next()
            }
            /*进度条开始拖动时候调用*/
            override fun onSeekBarStartDrag(seekBar: SeekBar?) {
                isDraggingSeekBar = true
            }
            /* 进度条停止拖动的时候调用*/
            override fun onSeekBarStopDrag(seekBar: SeekBar?) {
                seekBar?.let {sb->
                    val progress = sb.progress// 当前进度条的进度（秒）
                    if (lrcView.hasLrc()) {
                        lrcView.updateTime(progress.toLong() * 1000)
                    }
                    if (playerServiceBinder?.playing == true) {// 如果正在播放 需要将播放进度与进度条对齐
                        playerServiceBinder?.mp?.seekTo(sb.progress*1000)
                        updatePlayProgress()
                    } else {
                        pauseProgress = sb.progress
                        hasDragSeekBarOnPause = true
                    }
                    isDraggingSeekBar = false
                    playControllerView.tvCurrentProgress.text = StringUtil.formatProgress(progress.toLong())
                }
            }
        })
        /*音量*/
        volumeSeekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
                p0?.let {
                    audioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC,it.progress,
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
                    )
                }
            }
        })
        /* 喜欢该音乐*/
        ivLoveSong.fastClickListener {
            addLoveSongAnim?.start()
            if (isLoveSong) {
                it.isSelected = false
                mViewModel.deleteFromLove(currentSong?.songId)
            } else {
                it.isSelected = true
                mViewModel.add2LoveSong(currentSong)
            }
            isLoveSong = !isLoveSong
        }
        /*播放顺序模式*/
        ivPlayModel.fastClickListener {
            switchPlayMode()
        }
        /* 下载音乐*/
        ivDownload.fastClickListener {
            if (playType == Consts.LIST_TYPE_LOCAL) {
                R.string.local_no_need_download.getStringRes().toast()
            } else {
                val downloadSong = mViewModel.getDownloadSong(currentSong)
                downloadSong?.let {
                    bindDownloadService()
                    downloadServiceBinder?.startDownload(it)
                }
            }
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            /* 显示在线歌词*/
            onlineLyric.observe(this@PlayActivity,Observer{
                if (it != null) {
                    if (it.lyric.isNotNullOrEmpty()) {
                        lrcStatus = 2
                        lrcView.loadLrc(it.lyric)
                        if (!isOnlineSong) {
                            switchCoverLrc(false)
                        }
                    } else {
                        lrcStatus = 2
                        lrcView.setLabel(R.string.lrc_empty.getStringRes())
                    }
                } else {
                    lrcStatus = 2
                    lrcView.setLabel(R.string.lrc_empty.getStringRes())
                }
            })
            /* 显示非网络音乐的封面图片*/
            localCoverImg.observe(this@PlayActivity,Observer{
                it?.let {setNetImgCover(it)}
            })
            /* 没有匹配到非网络音乐与之对应的网络音乐资源~即也没有对应的歌词*/
            matchSongError.observe(this@PlayActivity,Observer{
                setLocalNetLrcError(it)
            })
            /* 匹配到了非网络音乐与之对应的网络音乐资源~*/
            matchSongRight.observe(this@PlayActivity,Observer{
                it?.let {
                    currentSong?.qqId = it
                    if (currentSong != null) {
                        SongUtil.saveSong(currentSong!!)
                    }
                }
            })
            matchSongRightAgain.observe(this@PlayActivity,Observer{
                currentSong?.qqId =it
                if (currentSong != null) {
                    SongUtil.saveSong(currentSong!!)
                }
                getOnlineLyric(it,Consts.SONG_LOCAL,currentSong?.songName?:"maizi")
            })
            /*查询/添加/删除喜欢的音乐*/
            handleLoveSong.observe(this@PlayActivity,Observer{
                it?.let {
                    when(it) {
                        PlayViewModel.is_love -> ivLoveSong.isSelected = true
                        PlayViewModel.add_to_love->Bus.post(Consts.ADD_TO_LOVE,true)
                        PlayViewModel.delete_from_love->Bus.post(Consts.ADD_TO_LOVE,false)
                    }
                }
            })
        }
        Bus.observe<Int>(Consts.SONG_STATUS_CHANGE,this) {
            when(it) {
                Consts.SONG_CHANGE->{
                    lrcStatus = 1// 歌曲已经就绪 可以查找歌词
                    currentSong = SongUtil.getSong()
                    discView.play()
//                    loadingSongAnim?.cancel()
                    currentSong?.let { song->
                        currentSongSinger.text=song.singer
                        currentSongName.text = song.songName
                        playControllerView.setCurrentSong(song)
                        updatePlayProgress()
                        playerServiceBinder?.mp?.setOnBufferingUpdateListener { _, percent ->
                            seekBar.secondaryProgress = percent *(seekBar.progress)
                        }
                        mViewModel.findIsLoveSong(song.songId)
                        if (song.isOnline) setNetImgCover(song.imgUrl!!) else setLocalSongCover(song.singer?:"maizi")
                    }
                }
                /* 当前歌曲播放完毕*/
                Consts.SONG_COMPLETE ->{
//                    startSeekBarLoading()
                    discView.pause()
                    switchCoverLrc(true)
                    showAlphaAnim?.start()
                    lrcStatus = 3
                    setDiscViewDefaultCover()
//                    playerServiceBinder?.pause()
                    updateProgressHandler.removeMessages(0)
//                    flag = true
                }
            }
        }
    }

//    private fun startSeekBarLoading() {
//        val drawable = seekBar.thumb
//        clearLoadingAnim()
//        if (loadingSongAnim == null) {
//            loadingSongAnim = ValueAnimator.ofFloat(10f,255f).apply {
//                repeatMode = ValueAnimator.REVERSE
//                duration=800
//                startDelay=150
//                repeatCount=-1
//                addUpdateListener {anim->
//                    val value = anim.animatedValue as Float
//                    drawable?.alpha = value.toInt()
//                }
//            }
//        }
//        loadingSongAnim?.start()
//    }

    /* 设置唱碟封面为默认图片（在切歌的时候起到过渡作用）*/
    private fun setDiscViewDefaultCover() {
        setDiscViewCover(BitmapFactory.decodeResource(resources, R.drawable.disk))
    }

    /* 初始化界面歌曲信息和组件*/
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
        initAlphaAnim()
    }

    /* 初始化播放模式*/
    private fun initPlayMode() {
        playMode = mViewModel.pm
        playerServiceBinder?.setPlayModel(playMode)
        ivPlayModel.setImageLevel(playMode)
    }

    /* 初始化歌词设置*/
    private fun initLrcView() {
        /* true:支持手動拖动 回调：滚动至某处，点击左边播放按钮时触发*/
        lrcView.setDraggable(true) { _, time ->
            playerServiceBinder?.mp?.seekTo(time.toInt())
            if (playerServiceBinder?.playing == false) {
                playerServiceBinder?.resume()
                updatePlayProgress()
                playControllerView.setPlayBtnSelected(true)
            }
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
    /* 切换播放顺序模式*/
    private fun switchPlayMode() {
        var mode = mViewModel.pm
        var modeText:String?=null
        when(mode){
            Consts.PLAY_ORDER->{
                mode = Consts.PLAY_RANDOM
                modeText = R.string.random_play.getStringRes()
            }
            Consts.PLAY_RANDOM->{
                mode = Consts.PLAY_SINGER
                modeText = R.string.single_play.getStringRes()
            }
            Consts.PLAY_SINGER->{
                mode = Consts.PLAY_ORDER
                modeText = R.string.order_play.getStringRes()
            }
        }
        modeText?.toast()
        mViewModel.setPlayMode(mode)
        initPlayMode()
    }

    /* 初始化音量设置*/
    private fun initVolume() {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        volumeSeekBar.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        volumeSeekBar.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }

    /* 监听音量改变的广播*/
    private val volumeReceiver = object :BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            volumeSeekBar.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        }
    }
    /*动态注册广播*/
    override fun onResume() {
        super.onResume()
        registerReceiver(volumeReceiver,IntentFilter("android.media.VOLUME_CHANGED_ACTION"))
    }

    override fun serviceConnection() {
        super.serviceConnection()
        playMode = mViewModel.pm
        playerServiceBinder?.setPlayModel(playMode)
        val song = SongUtil.getSong()
        if (song != null) {
            isOnlineSong = song.isOnline
            LogUtil.e("------PlayActivity serviceConnection isOnlineSong：$isOnlineSong")
            if (isOnlineSong) {
                val coverImgUrl = song.imgUrl
                if (coverImgUrl.isNotNullOrEmpty()) {
                    setNetImgCover(coverImgUrl!!)
                }
                if (currentPlayStatus == Consts.SONG_PLAY) {
                    discView.play()
                    playControllerView.setPlayBtnSelected(true)
                    updatePlayProgress()
                }
            } else {// 不是网络歌曲
                seekBar.secondaryProgress = song.duration
                setLocalSongCover(song.singer?:"maizi")
            }
            mViewModel.findIsLoveSong(song.songId)
        }
        playerServiceBinder?.mp?.setOnBufferingUpdateListener { _, percent ->
            seekBar.secondaryProgress = percent * seekBar.progress
        }
    }

    /*初始化页面组件*/
    private fun intiWidget() {
        playRootLayout = play_root_layout//背景
        discView = play_disk_view as DiscView// 唱碟
        discCoverView = discView.findViewById(R.id.iv_disc_background)// 唱碟封面
        lrcView = lrc_view// 歌词
        volumeSeekBar = findViewById(R.id.sb_volume)// 音量
        ivPlayModel = findViewById(R.id.iv_mode) // 播放模式
        ivDownload = findViewById(R.id.play_iv_download) // 下载音乐
        ivCollect = findViewById(R.id.play_iv_collect)
        ivSurround = findViewById(R.id.play_iv_surround)
        playControllerView = play_controller_view// 播放控制器
        currentSongSinger = play_tv_song_singer
        currentSongName = play_tv_song_name
        lLLrcContainer = play_lrc_view
        lLFunction = play_other_fun
        ivLoveSong = findViewById(R.id.play_iv_love)
        seekBar = playControllerView.progressSeekBar
        updateProgressHandler = UpdateProgressHandler(this)
    }

    /* 设置唱碟封面图片 并且根据封面图片设置模糊的背景图片*/
    private fun setNetImgCover(coverUrl: String) {
        loadImg(
            context = this,
            url = coverUrl,
            block = {
                discCoverBitmap = (it as BitmapDrawable).bitmap
                if (discCoverBitmap != null) {
                    setDiscViewCover(discCoverBitmap!!)
                    setPlayBackground(discCoverBitmap!!)
                }
                if (!isOnlineSong) {// 保存非网络音乐的封面图片
                    val formatSinger = StringUtil.formatSinger(currentSong?.singer?:"maizi")
                    SongUtil.saveSongCover(discCoverBitmap,formatSinger)
                    // 保存至数据库以便在首页能获取到
                    val dbPic = "${Constants.coverImgUrl()}$formatSinger.jpg"
                    mViewModel.updateLocalSongCoverBySongId(dbPic,currentSong?.songId?:"")
                }
            })
    }

    /* 没有匹配到非网络音乐与之对应的网络音乐资源~即也没有对应的歌词*/
    private fun setLocalNetLrcError(result:String?) {
        lrcView.setLabel(R.string.lrc_empty.getStringRes())
        currentSong?.qqId = result
        SongUtil.saveSong(currentSong!!)
    }

    /* 设置播放背景为封面模糊图片*/
    private fun setPlayBackground(bitmap: Bitmap) {
        thread {
            // TODO: 2020/10/31 这里的模糊效果并不是很好 有优化的空间
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

    /* 设置非网络歌曲的封面图片 如果是第一次则通过网络获取封面，不是第一次则直接拿储存在本地的图片*/
    private fun setLocalSongCover(singer:String) {
        val formatSinger = StringUtil.formatSinger(singer)
        val imgUrl = "${Constants.coverImgUrl()}$formatSinger.jpg"
        LogUtil.e("------PlayActivity setLocalSongCover imgUrl：$imgUrl; formatSinger:$formatSinger")
        val opts = RequestOptions
            .placeholderOf(R.drawable.disk)
            .error(R.drawable.disk)
        Glide
            .with(this)
            .load(imgUrl)
            .listener(object :RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    // 第一次加载本地音乐会触发 从网上搜索下来保存在本地
                    mViewModel.getLocalCoverImgUrlAndLrc(formatSinger,currentSong?.songName?:"",currentSong?.duration?:0)
                    // TODO: 2020/10/30 是否需要设置默认图片
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .apply(opts)
            .into(object :SimpleTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    // 播放的非网络音乐 但不是第一次播放  图片之前已经保存在本地了
                    discCoverBitmap = (resource as BitmapDrawable).bitmap
                    setDiscViewCover(discCoverBitmap!!)
                }
            })
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
        if (addLoveSongAnim == null) {
            addLoveSongAnim = AnimatorInflater.loadAnimator(this,R.animator.favorites_anim) as AnimatorSet
        }
        addLoveSongAnim?.setTarget(ivLoveSong)
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
                    if (!isDraggingSeekBar) {
                        // 当前播放进度 单位：毫秒
                        val progress = playerServiceBinder?.playingTime
                        if (progress != null) {
                            val sceProgress = progress / 1000 // 秒
                            playControllerView.updatePlayProgress(sceProgress)
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

    override fun onDestroy() {
        super.onDestroy()
        /*停止计时*/
        updateProgressHandler.removeMessages(0)
        updateProgressHandler.removeCallbacksAndMessages(null)
        /*解绑广播*/
        unregisterReceiver(volumeReceiver)
        val song = SongUtil.getSong()
//        if (flag) {
//            song?.playStatus = Consts.SONG_PAUSE
//        } else {
//            song?.playStatus = Consts.SONG_PLAY
//        }
//        if (flag) {
//            song?.playStatus = Consts.SONG_PLAY
//        }
        SongUtil.saveSong(song)
        if (hideAlphaAnim?.isRunning == true) {
            hideAlphaAnim?.cancel()
            hideAlphaAnim = null
        }
        if (showAlphaAnim?.isRunning == true) {
            showAlphaAnim?.cancel()
            showAlphaAnim = null
        }
        if (addLoveSongAnim?.isRunning == true) {
            addLoveSongAnim?.cancel()
            addLoveSongAnim = null
        }
//        if (loadingSongAnim?.isRunning == true) {
//            loadingSongAnim?.cancel()
//            loadingSongAnim = null
//        }
    }
//    private fun clearLoadingAnim() {
//        if (loadingSongAnim?.isRunning == true) {
//            loadingSongAnim?.cancel()
//            loadingSongAnim = null
//        }
//    }
}
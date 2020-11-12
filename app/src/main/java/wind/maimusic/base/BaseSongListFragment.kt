package wind.maimusic.base

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ImmersionBar
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.model.HistorySong
import wind.maimusic.model.LocalSong
import wind.maimusic.model.LoveSong
import wind.maimusic.model.OnlineSong
import wind.maimusic.model.download.Downloaded
import wind.maimusic.model.songlist.SongListTop
import wind.maimusic.service.PlayerService
import wind.maimusic.utils.*
import wind.maimusic.widget.dialog.BottomFunctionDialog
import wind.widget.cost.Consts
import wind.widget.effcientrv.*
import wind.widget.utils.fastClickListener
import wind.widget.utils.loadImg
import wind.widget.utils.toIntPx

/**
 * @By Journey 2020/10/28
 * @Description 歌单公共类  包含：全部播放 加载view 空view 歌单content recyclerView
 */
abstract class BaseSongListFragment<VM : BaseViewModel> : BaseLifeCycleFragment<VM>() {
    protected var playerBinder: PlayerService.PlayerBinder? = null
    protected var bottomFunctionDialog:BottomFunctionDialog?=null

    /*非必须组件*/
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    protected var tvFunc: TextView? = null
    private var appBarLayout: AppBarLayout? = null
    private var toolBar: Toolbar? = null
    private var tvSongListName: TextView? = null
    private var tvSongListDescription: TextView? = null
    private var ivSongListSmallCover: ImageView? = null
    private var ivSongListLargeCover: ImageView? = null
    private var tvSongListAuthor: TextView? = null
    private var tvSongListTitleName: TextView? = null
    private var tvSongListTagA: TextView? = null
    private var tvSongListTagB: TextView? = null

    private var ivSongListTitleBack: ImageView? = null

    /*歌单顶部信息*/
    private var songListTop: SongListTop? = null

    /*必须组件*/
    protected lateinit var ivPlayAll: ImageView
    protected lateinit var tvDownloadAll: TextView
    protected lateinit var flPlayAll: FrameLayout
    protected lateinit var rvSongList: RecyclerView

    /*歌单分类列表*/
    protected var localSongs = mutableListOf<LocalSong>()
    protected var recentListenSongs = mutableListOf<HistorySong>()
    protected var downloadedSongs = mutableListOf<Downloaded>()
    protected var lovedSongs = mutableListOf<LoveSong>()
    protected var onlineSongs = mutableListOf<OnlineSong>()

    /*当前播放的曲目*/
    private var currentSong: Any? = null

    /*当前播放的曲目信息*/
    private var songName: String? = null
    private var songSinger: String? = null
    private var songId: String? = null
    private var isDownloaded: Boolean = false
    private var canRefresh: Boolean = true

    /*点击改变item状态的标志*/
    private var lastPosition: Int = -1

    /*需要操作layoutManager的地方调用*/
    protected lateinit var layoutManager: LinearLayoutManager
    private val playerConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            playerBinder = service as PlayerService.PlayerBinder
            onBindConnection()
        }
    }

    open fun onBindConnection() {
    }

    override fun initView() {
        super.initView()
        initOptionsWidget()
        /*必有的组件*/
        rvSongList = mRootView.findViewById(R.id.song_list_rv)
        ivPlayAll = mRootView.findViewById(R.id.view_song_list_iv_play_all)
        flPlayAll = mRootView.findViewById(R.id.fl_play_all)
        tvDownloadAll = mRootView.findViewById(R.id.tv_download_all)
        layoutManager = LinearLayoutManager(requireContext())
        /*默认的recyclerView*/
        setRvContent()
    }
    open fun setRvContent() {
        rvSongList.setHasFixedSize(true)
        rvSongList.setup<Any> {
            withLayoutManager {
                return@withLayoutManager layoutManager
            }
            adapter {
                addItem(R.layout.item_song_list) {
                    bindViewHolder { data, position, _ ->
                        data?.let {
                            setDataType(it)
                            setText(R.id.item_song_list_tv_song_name, songName)
                            setText(R.id.item_song_list_tv_song_singer, songSinger)
                            setVisible(R.id.item_song_list_iv_downloaded, isDownloaded)
                            val song = SongUtil.getSong()
                            val currentSongId = song?.songId
                            if (currentSongId != null) {
                                if (currentSongId == songId) {
                                    setVisible(R.id.item_song_list_iv_playing, true)
                                    lastPosition = position
                                } else {
                                    setVisible(R.id.item_song_list_iv_playing, false)
                                }
                            }
                            itemClicked(View.OnClickListener {
                                if (position != lastPosition) {
                                    notifyItemChanged(lastPosition)
                                    lastPosition = position
                                }
                                notifyItemChanged(position)
                                setCurrentSong(position)// 设置当前点击的歌曲
                                val s = SongUtil.assemblySong(currentSong!!, songListType(), position)
                                SongUtil.saveSong(s)
                                playerBinder?.play(s.listType,songListType())
                            })
                            clicked(R.id.item_song_list_iv_more,View.OnClickListener {
                                bottomFunctionDialog?.show()
                            })
                        }
                    }
                }
            }
        }
    }

    open fun initTitle(title: String, func: String, showFuc: Boolean = true) {
        tvTitle?.text = title
        tvFunc?.text = func
        if (!showFuc) tvFunc.gone()
    }

    open fun intiPlayAll(showDownAll: Boolean = true) {
        if (!showDownAll) tvDownloadAll.gone()
    }

    abstract fun songListType(): Int

    override fun initAction() {
        super.initAction()
        ivBack?.fastClickListener {
        }
//        tvFunc?.fastClickListener {
//            doFunc()
//        }
        tvDownloadAll.fastClickListener {
            downloadAll()
        }
        ivPlayAll.fastClickListener {
            playAll()
        }
        initToolBar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val playerIntent = Intent(requireContext(), PlayerService::class.java)
        requireContext().bindService(playerIntent, playerConnection, Context.BIND_AUTO_CREATE)
    }

    private fun setDataType(data: Any) {
        when (songListType()) {
            Consts.LIST_TYPE_LOCAL -> {
                data as LocalSong
                songName = data.name
                songSinger = data.singer
                songId = data.songId
                isDownloaded = true
            }
            Consts.LIST_TYPE_HISTORY -> {
                data as HistorySong
                songName = data.name
                songSinger = data.singer
                songId = data.songId
                isDownloaded = data.isDownload
            }
            Consts.LIST_TYPE_DOWNLOAD -> {
                data as Downloaded
                songName = data.name
                songSinger = data.singer
                songId = data.songId
                isDownloaded = true
            }
            Consts.LIST_TYPE_LOVE -> {
                data as LoveSong
                songName = data.name
                songSinger = data.singer
                songId = data.songId
                isDownloaded = data.isDownload ?: false
            }
            Constants.ST_DAILY_RECOMMEND -> {
                data as OnlineSong
                songName = data.name
                songSinger = data.singer
                songId = data.songId
                isDownloaded = data.isDownload
            }
        }
    }

    private fun setCurrentSong(position: Int) {
        when (songListType()) {
            Consts.LIST_TYPE_LOCAL -> currentSong = localSongs[position]
            Consts.LIST_TYPE_HISTORY -> currentSong = recentListenSongs[position]
            Consts.LIST_TYPE_DOWNLOAD -> currentSong = downloadedSongs[position]
            Consts.LIST_TYPE_LOVE -> currentSong = lovedSongs[position]
            Constants.ST_DAILY_RECOMMEND -> currentSong = onlineSongs[position]
        }
    }

    /*全部播放*/
    open fun playAll() {
    }

    /*标题栏右侧的功能按钮*/
    open fun doFunc() {
    }

    /*全部下载*/
    open fun downloadAll() {
    }

    /*初始化歌单数据 由继承的类实现 对于Md类型歌单必须实现这个方法*/
    private fun initSongListInfo() {
        songListTop?.let { info ->
            tvSongListName?.text = info.songListName
            tvSongListDescription?.text = info.songListDescription
            tvSongListAuthor?.text = info.songListAuthor
            tvSongListTagA?.text = info.songListTagA
            tvSongListTagB?.text = info.songListTagB
            tvSongListTitleName?.text = info.songListName
            ivSongListLargeCover?.loadImg(
                url = info.songListCoverImgUrl ?: "",
                placeholder = R.drawable.place_holder_half_translate,
                error = R.drawable.place_holder_half_translate
            )
            ivSongListSmallCover?.loadImg(
                url = info.songListCoverImgUrl ?: "",
                round = 6f
            )
        }
    }

    /*初始化歌单数据 赋值*/
    open fun setSongListTop(songListType: Int) {
        when (songListType) {
            Consts.LIST_TYPE_LOVE -> {
                songListTop = SongListTop(
                    "我喜欢的音乐",
                    "我喜欢你，像风走了八千里，不问归期",
                    "By Journey - Travel end -",
                    if (lovedSongs.isNullOrEmpty()) Constants.TEMP_SONG_COVER1_NORMAL else lovedSongs[0].pic,
                    "喜欢",
                    "清凉"
                )
            }
            Constants.ST_DAILY_RECOMMEND -> {
                songListTop = SongListTop(
                    "每日推荐",
                    "入我相思门，知我相思苦。早知如此绊人心，何如当初莫相识",
                    "By suo luo -",
                    if (onlineSongs.isNullOrEmpty()) Constants.TEMP_SONG_COVER1_NORMAL else onlineSongs[0].imgUrl,
                    "推荐",
                    "清新"
                )
            }
        }
        initSongListInfo()
    }

    /*监听md toolBar透明度的变化*/
    private fun initToolBar() {
        val alphaMaxOffset = Constants.TOOLBAR_MAX_OFFSET.toFloat().toIntPx()
        toolBar?.background?.alpha = 0
        tvSongListTitleName?.alpha = 0f
        appBarLayout?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset > -alphaMaxOffset) {
                toolBar?.background?.alpha = 255 * -verticalOffset / alphaMaxOffset
//                LogUtil.e("alpha:${(-verticalOffset.toFloat() / 600)}, verticalOffset:$verticalOffset")
                var textAlpha = -verticalOffset.toFloat() / 1000
                if (textAlpha > 1) {
                    textAlpha = 1.00f
                }
                tvSongListTitleName?.alpha = textAlpha
                canRefresh = true
                if (canRefresh) {
                    canRefresh = false
                    ivSongListTitleBack?.setImageResource(R.drawable.ic_arrow_left_white)
                }
            } else {
                canRefresh = true
                if (canRefresh) {
                    canRefresh = false
                    toolBar?.background?.alpha = 255
                    tvSongListTitleName?.alpha = 1.0f
                    ivSongListTitleBack?.setImageResource(R.drawable.ic_arrow_left)
                }
            }
        })
    }

    private fun initOptionsWidget() {
        /*可选的组件*/
        mRootView.findViewById<ImageView>(R.id.title_iv_back)?.let {
            ivBack = it
        }
        mRootView.findViewById<TextView>(R.id.title_tv)?.let {
            tvTitle = it
        }
        mRootView.findViewById<TextView>(R.id.title_tv_fun)?.let {
            tvFunc = it
        }
        mRootView.findViewById<AppBarLayout>(R.id.md_song_list_appbar_layout)?.let {
            appBarLayout = it
        }
        mRootView.findViewById<Toolbar>(R.id.md_song_list_toolbar)?.let {
            toolBar = it
        }
        mRootView.findViewById<TextView>(R.id.md_song_list_tv_name)?.let {
            tvSongListName = it
        }
        mRootView.findViewById<TextView>(R.id.md_song_list_tv_description)?.let {
            tvSongListDescription = it
        }
        mRootView.findViewById<TextView>(R.id.md_song_list_tv_author)?.let {
            tvSongListAuthor = it
        }
        mRootView.findViewById<TextView>(R.id.md_song_list_tv_title_name)?.let {
            tvSongListTitleName = it
        }
        mRootView.findViewById<ImageView>(R.id.md_song_list_iv_title_back)?.let {
            ivSongListTitleBack = it
        }
        mRootView.findViewById<ImageView>(R.id.md_song_list_iv_small_cover)?.let {
            ivSongListSmallCover = it
        }
        mRootView.findViewById<ImageView>(R.id.md_song_list_iv_large_cover)?.let {
            ivSongListLargeCover = it
        }
        mRootView.findViewById<TextView>(R.id.md_song_list_tv_tab_a)?.let {
            tvSongListTagA = it
        }
        mRootView.findViewById<TextView>(R.id.md_song_list_tv_tab_b)?.let {
            tvSongListTagB = it
        }

        if (bottomFunctionDialog == null) {
            bottomFunctionDialog = BottomFunctionDialog(requireContext())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().unbindService(playerConnection)
    }

    override fun initStatusBar() {
        super.initStatusBar()
        ImmersionBar
            .with(this)
            .statusBarDarkFont(false)
            .init()
    }
}
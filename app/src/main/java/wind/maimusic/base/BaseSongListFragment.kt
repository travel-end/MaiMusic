package wind.maimusic.base

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.imageview.ShapeableImageView
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
import wind.maimusic.ui.activities.MainActivity
import wind.maimusic.utils.*
import wind.maimusic.widget.dialog.BottomFunctionDialog
import wind.widget.cost.Consts
import wind.widget.effcientrv.*
import wind.widget.rippleview.RippleView
import wind.widget.utils.fastClickListener
import wind.widget.utils.loadImg
import wind.widget.utils.toIntPx

/**
 * @By Journey 2020/10/28
 * @Description 歌单公共类  包含：全部播放 加载view 空view 歌单content recyclerView
 */
abstract class BaseSongListFragment<VM : BaseViewModel> : BaseLifeCycleFragment<VM>() {
    protected var playerBinder: PlayerService.PlayerBinder? = null
    protected var bottomFunctionDialog: BottomFunctionDialog? = null
    protected var listType: Int = 0
    protected var singerId: Int = 0
    private lateinit var mAdapter: EfficientAdapter<Any>
    /*非必须组件*/
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    protected var tvFunc: TextView? = null
    private var appBarLayout: AppBarLayout? = null
    private var toolBar: Toolbar? = null
    protected var tvSongListName: TextView? = null
    protected var tvSongListDescription: TextView? = null
    protected var ivSongListSmallCover: ShapeableImageView? = null
    protected var ivSongListLargeCover: ImageView? = null
    //    protected var tvSongListAuthor: TextView? = null
    protected var tvSongListTitleName: TextView? = null
    private var tvSongListTagA: TextView? = null
    private var tvSongListTagB: TextView? = null
    private var immersionBar: ImmersionBar? = null
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
    private var mIsDownloaded: Boolean = false
    private var alphaFlag: Boolean = true
    /*点击改变item状态的标志*/
    protected var lastPosition: Int = -1
    /*需要操作layoutManager的地方调用*/
    protected lateinit var mLayoutManager: LinearLayoutManager
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
        mLayoutManager = LinearLayoutManager(requireContext())
        /*默认的recyclerView*/
        setRvContent()
    }

    open fun setRvContent() {
        rvSongList.setHasFixedSize(true)
        rvSongList.layoutManager = mLayoutManager
        mAdapter = efficientAdapter<Any> {
            addItem(R.layout.item_song_list) {
                bindViewHolder { data, position, _ ->
                    data?.let {
                        setDataType(it)
                        setText(R.id.item_song_list_tv_song_name, songName)
                        setText(R.id.item_song_list_tv_song_singer, songSinger)
                        setVisible(R.id.item_song_list_iv_downloaded, mIsDownloaded)
                        val song = SongUtil.getSong()
                        val currentSongId = song?.songId
                        if (currentSongId != null) {
                            if (currentSongId == songId) {
                                setVisible(R.id.item_song_list_iv_playing, true)
                                setTextColor(
                                    R.id.item_song_list_tv_song_name,
                                    R.color.colorPrimary.getColorRes()
                                )
                                setTextColor(
                                    R.id.item_song_list_tv_song_singer,
                                    R.color.colorPrimary.getColorRes()
                                )
                                lastPosition = position
                            } else {
                                setTextColor(
                                    R.id.item_song_list_tv_song_name,
                                    R.color.black2.getColorRes()
                                )
                                setTextColor(
                                    R.id.item_song_list_tv_song_singer,
                                    R.color.text_color.getColorRes()
                                )
                                setVisible(R.id.item_song_list_iv_playing, false)
                            }
                        }
                        itemView?.findViewById<RippleView>(R.id.ripple_view)
                            ?.setOnRippleCompleteListener {
                                if (position != lastPosition) {
                                    notifyItemChanged(lastPosition)
                                    lastPosition = position
                                }
                                notifyItemChanged(position)
                                setCurrentSong(position)// 设置当前点击的歌曲
                                val s = SongUtil.assemblySong(
                                    currentSong!!,
                                    songListType(),
                                    position
                                )
                                SongUtil.saveSong(s)
                                (requireActivity() as MainActivity).showLoadingNormal("")
                                playerBinder?.play(
                                    s.listType,
                                    songListType()
                                )
                            }
                        itemView?.findViewById<ImageView>(R.id.item_song_list_iv_more)
                            ?.fastClickListener {
                                bottomFunctionDialog?.show()
                            }
                    }
                }
            }
        }
        val song = SongUtil.getSong()
        if (song != null) {
            mLayoutManager.scrollToPositionWithOffset(song.position - 4, rvSongList.height)
        }
        rvSongList.adapter = mAdapter
    }

    override fun observe() {
        super.observe()
        Bus.observe<Int>(Consts.SONG_STATUS_CHANGE, this) { status ->
            if (status == Consts.SONG_CHANGE) {
                mAdapter.notifyDataSetChanged()
                val song = SongUtil.getSong()
                if (song != null) {
                    mLayoutManager.scrollToPositionWithOffset(song.position + 4, rvSongList.height)
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
                mIsDownloaded = true
            }
            Consts.LIST_TYPE_HISTORY -> {
                data as HistorySong
                songName = data.name
                songSinger = data.singer
                songId = data.songId
                mIsDownloaded = data.isDownload
            }
            Consts.LIST_TYPE_DOWNLOAD -> {
                data as Downloaded
                songName = data.name
                songSinger = data.singer
                songId = data.songId
                mIsDownloaded = true
            }
            Consts.LIST_TYPE_LOVE -> {
                data as LoveSong
                songName = data.name
                songSinger = data.singer
                songId = data.songId
                mIsDownloaded = data.isDownload ?: false
            }
            Constants.ST_DAILY_RECOMMEND -> {
                data as OnlineSong
                songName = data.name
                songSinger = data.singer
                songId = data.songId
                mIsDownloaded = data.isDownload
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
            tvSongListTagA?.text = info.songListTagA
            tvSongListTagB?.text = info.songListTagB
            tvSongListTitleName?.text = info.songListTitleName
            ivSongListLargeCover?.loadImg(
                url = info.songListCoverImgUrl ?: "",
                placeholder = R.drawable.place_holder_half_translate,
                error = R.drawable.place_holder_half_translate
            )
            if (info.songListCoverImgUrl.isNotNullOrEmpty()) {
                ivSongListSmallCover?.loadImg(
                    url = info.songListCoverImgUrl!!
                )
            }
            tvSongListName?.fastClickListener {
            }
        }
    }

    /*初始化歌单数据 赋值*/
    open fun setSongListTop(songListType: Int) {
        when (songListType) {
            Consts.LIST_TYPE_LOVE -> {
                songListTop = SongListTop(
                    "我喜欢的音乐",
                    "我喜欢的音乐",
                    "By Journey - Travel end -",
                    songListCoverImgUrl = if (lovedSongs.isNullOrEmpty()) Constants.TEMP_SONG_COVER1_NORMAL else lovedSongs[0].pic)
            }
            Constants.ST_DAILY_RECOMMEND -> {
                songListTop = SongListTop(
                    songListTitleName = R.string.today_recommend.getStringRes(),
                    songListName = "世界微尘里 吾宁爱与憎",
                    songListDescription = "残阳入西掩，茅屋仿股僧。落叶人何在，韩云路基层。独敲初夜磬，闲倚一枝藤。世界微尘里，吾宁爱与憎。",
                    songListCoverImgUrl = if (onlineSongs.isNullOrEmpty()) Constants.TEMP_SONG_COVER1_NORMAL else onlineSongs[0].imgUrl
                )
            }
        }
        initSongListInfo()
    }

    open fun showTag(showTag: Boolean) {
        if (showTag) {
            tvSongListTagA.visible()
            tvSongListTagB.visible()
        }
    }

    /*监听md toolBar透明度的变化*/
    private fun initToolBar() {
        val alphaMaxOffset = Constants.TOOLBAR_MAX_OFFSET.toFloat().toIntPx()
        toolBar?.background?.alpha = 0
        tvSongListTitleName?.alpha = 0f
        appBarLayout?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset > -alphaMaxOffset) {
                toolBar?.background?.alpha = 255 * -verticalOffset / alphaMaxOffset
                var textAlpha = -verticalOffset.toFloat() / 1000
                if (textAlpha > 1) {
                    textAlpha = 1.00f
                }
                tvSongListTitleName?.alpha = textAlpha
                if (alphaFlag) {
                    alphaFlag = false
                    ivSongListTitleBack?.setImageResource(R.drawable.ic_arrow_left_white)
                    immersionBar?.statusBarDarkFont(false)?.init()
                }
            } else {
                if (!alphaFlag) {
                    alphaFlag = true
                    toolBar?.background?.alpha = 255
                    tvSongListTitleName?.alpha = 1.0f
                    ivSongListTitleBack?.setImageResource(R.drawable.ic_arrow_left)
                    immersionBar?.statusBarDarkFont(true)?.init()
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
//        mRootView.findViewById<TextView>(R.id.md_song_list_tv_author)?.let {
//            tvSongListAuthor = it
//        }
        mRootView.findViewById<TextView>(R.id.md_song_list_tv_title_name)?.let {
            tvSongListTitleName = it
        }
        mRootView.findViewById<ImageView>(R.id.md_song_list_iv_title_back)?.let {
            ivSongListTitleBack = it
        }
        mRootView.findViewById<ShapeableImageView>(R.id.md_song_list_iv_small_cover)?.let {
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
        immersionBar = ImmersionBar.with(this)

        immersionBar
        ?.statusBarDarkFont(false)
        ?.init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        immersionBar
            ?.statusBarDarkFont(true)
            ?.init()
    }
}
package wind.maimusic.ui.fragment.singer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ImmersionBar
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.model.OnlineSong
import wind.maimusic.service.PlayerService
import wind.maimusic.ui.activities.MainActivity
import wind.maimusic.utils.*
import wind.maimusic.vm.SingerSongListViewModel
import wind.maimusic.widget.dialog.BottomFunctionDialog
import wind.widget.cost.Consts
import wind.widget.effcientrv.*
import wind.widget.rippleview.RippleView
import wind.widget.utils.fastClickListener
import wind.widget.utils.loadImg
import wind.widget.utils.toIntPx

/**
 * @By Journey 2020/11/18
 * @Description 歌手歌单
 */
class SingerSongListFragment : BaseLifeCycleFragment<SingerSongListViewModel>() {
    private var singerCover: String = ""
    private var singerId: Int = 0
    private var singerName: String = ""
    private var playerBinder: PlayerService.PlayerBinder? = null
    private var bottomFunctionDialog: BottomFunctionDialog? = null
    private lateinit var mAdapter: EfficientAdapter<OnlineSong>
    private var appBarLayout: AppBarLayout? = null
    private var toolBar: Toolbar? = null
    private var immersionBar: ImmersionBar? = null
    private lateinit var ivPlayAll: ImageView
    private lateinit var tvDownloadAll: TextView
    private lateinit var flPlayAll: FrameLayout
    private lateinit var rvSongList: RecyclerView
    private lateinit var tvSingerNameTitle: TextView
    private lateinit var tvSingerName: TextView
    private lateinit var tvDesc: TextView
    private lateinit var ivBack:ImageView
    private lateinit var ivSingerCover:ImageView
    private var currentSong: OnlineSong? = null
    private var alphaFlag: Boolean = true
    private var onlineSongs = mutableListOf<OnlineSong>()
    private var lastPosition: Int = -1

    /*需要操作layoutManager的地方调用*/
    private lateinit var mLayoutManager: LinearLayoutManager
    private val playerConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            playerBinder = service as PlayerService.PlayerBinder
        }
    }

    override fun initView() {
        super.initView()
        rvSongList = mRootView.findViewById(R.id.song_list_rv)
        ivPlayAll = mRootView.findViewById(R.id.view_song_list_iv_play_all)
        flPlayAll = mRootView.findViewById(R.id.fl_play_all)
        tvDownloadAll = mRootView.findViewById(R.id.tv_download_all)
        tvSingerNameTitle = mRootView.findViewById(R.id.md_song_list_tv_title_name)
        tvSingerName = mRootView.findViewById(R.id.md_song_list_tv_name)
        tvDesc = mRootView.findViewById(R.id.md_song_list_tv_description)
        ivBack = mRootView.findViewById(R.id.md_song_list_iv_title_back)
        toolBar = mRootView.findViewById(R.id.md_song_list_toolbar)
        appBarLayout = mRootView.findViewById(R.id.md_song_list_appbar_layout)
        ivSingerCover = mRootView.findViewById(R.id.md_song_list_iv_large_cover)
        initSingerInfo()
        initToolBar()
        mLayoutManager = LinearLayoutManager(requireContext())
        rvSongList.setHasFixedSize(true)
        rvSongList.layoutManager = mLayoutManager
        mAdapter = efficientAdapter<OnlineSong> {
            addItem(R.layout.item_song_list) {
                bindViewHolder { data, position, _ ->
                    data?.let {
                        setText(R.id.item_song_list_tv_song_name, it.name)
                        setText(R.id.item_song_list_tv_song_singer, it.singer)
                        setVisible(R.id.item_song_list_iv_downloaded, it.isDownload)
                        val song = SongUtil.getSong()
                        val currentSongId = song?.songId
                        if (currentSongId != null) {
                            if (currentSongId == it.songId) {
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
                                currentSong = onlineSongs[position]
                                val s = SongUtil.assemblySong(
                                    currentSong!!,
                                    Consts.ONLINE_SINGER_SONG,
                                    position
                                )
                                SongUtil.saveSong(s)
                                (requireActivity() as MainActivity).showLoadingNormal("")
                                playerBinder?.singerPlay(singerId,changeSingerId = true)
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
        mViewModel.onlineSongs.observe(this, Observer {
            if (isNotNullOrEmpty(it)) {
                onlineSongs.clear()
                onlineSongs.addAll(it)
                rvSongList.submitList(onlineSongs)
                rvSongList.visible()
                flPlayAll.visible()
            }
        })
    }

    override fun layoutResId() = R.layout.fragment_md_singer_song_list
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val playerIntent = Intent(requireContext(), PlayerService::class.java)
        requireContext().bindService(playerIntent, playerConnection, Context.BIND_AUTO_CREATE)
    }

    override fun initData() {
        super.initData()
        mViewModel.findSingerSongs(singerId)
    }

    private fun initToolBar() {
        val alphaMaxOffset = Constants.TOOLBAR_MAX_OFFSET.toFloat().toIntPx()
        toolBar?.background?.alpha = 0
        tvSingerNameTitle.alpha = 0f
        appBarLayout?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset > -alphaMaxOffset) {
                toolBar?.background?.alpha = 255 * -verticalOffset / alphaMaxOffset
                var textAlpha = -verticalOffset.toFloat() / 1000
                if (textAlpha > 1) {
                    textAlpha = 1.00f
                }
                tvSingerNameTitle.alpha = textAlpha
                if (alphaFlag) {
                    alphaFlag = false
                    ivBack.setImageResource(R.drawable.ic_arrow_left_white)
                    immersionBar?.statusBarDarkFont(false)?.init()
                }
            } else {
                if (!alphaFlag) {
                    alphaFlag = true
                    toolBar?.background?.alpha = 255
                    tvSingerNameTitle.alpha = 1.0f
                    ivBack.setImageResource(R.drawable.ic_arrow_left)
                    immersionBar?.statusBarDarkFont(true)?.init()
                }
            }
        })
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

    private fun initSingerInfo() {
        arguments?.let {
            val id = it.getString(Constants.SINGER_ID)
            if (id.isNotNullOrEmpty()) {
                singerId=id!!.toInt()
            }
            singerCover = it.getString(Constants.SINGER_COVER,"")
            singerName = it.getString(Constants.SINGER_NAME,"")
        }
        tvSingerNameTitle.text = singerName
        tvSingerName.text = singerName
        if (singerCover.isNotEmpty()) {
            ivSingerCover.loadImg(singerCover)
        }
        tvDesc.text = "我喜欢你，相逢走了八万里，不问归期"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        immersionBar
            ?.statusBarDarkFont(true)
            ?.init()
    }
}
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
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_play_all.*
import wind.maimusic.R
import wind.maimusic.model.HistorySong
import wind.maimusic.model.LocalSong
import wind.maimusic.model.LoveSong
import wind.maimusic.model.download.DownloadSong
import wind.maimusic.model.download.Downloaded
import wind.maimusic.service.PlayerService
import wind.maimusic.utils.SongUtil
import wind.maimusic.utils.gone
import wind.widget.cost.Consts
import wind.widget.effcientrv.*
import wind.widget.utils.fastClickListener

/**
 * @By Journey 2020/10/28
 * @Description 歌单公共类  包含：全部播放 加载view 空view 歌单content recyclerView
 */
abstract class BaseSongListFragment<VM:BaseViewModel> :BaseLifeCycleFragment<VM>() {
    protected var playerBinder : PlayerService.PlayerBinder?=null
    protected lateinit var rvSongList:RecyclerView
    private lateinit var ivBack:ImageView
    private lateinit var tvTitle:TextView
    private lateinit var tvFunc:TextView
    protected lateinit var ivPlayAll:ImageView
    protected lateinit var tvDownloadAll:TextView
    protected lateinit var flPlayAll:FrameLayout

    protected var localSongs = mutableListOf<LocalSong>()
    protected var recentListenSongs = mutableListOf<HistorySong>()
    protected var downloadedSongs = mutableListOf<Downloaded>()
    protected var lovedSongs = mutableListOf<LoveSong>()

    private var currentSong:Any?= null

    private var songName:String?=null
    private var songSinger:String?=null
    private var songId:String? = null

    protected lateinit var layoutManager:LinearLayoutManager

    private var lastPosition:Int = -1
    private val playerConnection = object :ServiceConnection {
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
        ivBack = mRootView.findViewById(R.id.title_iv_back)
        tvTitle = mRootView.findViewById(R.id.title_tv)
        tvFunc =  mRootView.findViewById(R.id.title_tv_fun)
        rvSongList = mRootView.findViewById(R.id.song_list_rv)
        ivPlayAll =  mRootView.findViewById(R.id.view_song_list_iv_play_all)
        flPlayAll = mRootView.findViewById(R.id.fl_play_all)
        tvDownloadAll = mRootView.findViewById(R.id.tv_download_all)
        layoutManager = LinearLayoutManager(requireContext())
        setRvContent()
    }
    open fun setRvContent() {
        rvSongList.setup<Any> {
            withLayoutManager {
                return@withLayoutManager  layoutManager
            }
            adapter {
                addItem(R.layout.item_song_list) {
                    bindViewHolder { data, position, _ ->
                        data?.let {
                            setDataType(it)
                            setText(R.id.item_song_list_tv_song_name,songName)
                            setText(R.id.item_song_list_tv_song_singer,songSinger)
                            val song = SongUtil.getSong()
                            val currentSongId=song?.songId
                            if (currentSongId != null) {
                                if (currentSongId == songId) {
                                    setVisible(R.id.item_song_list_iv_playing,true)
                                    lastPosition = position
                                } else {
                                    setVisible(R.id.item_song_list_iv_playing,false)
                                }
                            }
                            itemClicked(View.OnClickListener {
                                if (position != lastPosition) {
                                    notifyItemChanged(lastPosition)
                                    lastPosition = position
                                }
                                notifyItemChanged(position)
                                setCurrentSong(position)
                                SongUtil.saveSong(SongUtil.assemblySong(currentSong!!, songListType(),position))
                                playerBinder?.play(songListType())
                            })
                        }
                    }
                }
            }
        }
    }

    open fun initTitle(title:String,func:String,showFuc:Boolean=true) {
        tvTitle.text = title
        tvFunc.text = func
        if (!showFuc)  tvFunc.gone()
    }
    open fun intiPlayAll(showDownAll:Boolean = true) {
        if (!showDownAll) tvDownloadAll.gone()
    }
    abstract fun songListType():Int

    override fun initAction() {
        super.initAction()
        ivBack.fastClickListener {

        }
        tvFunc.fastClickListener {
            doFunc()
        }
        tvDownloadAll.fastClickListener {
            downloadAll()
        }
        ivPlayAll.fastClickListener {
            playAll()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val playerIntent = Intent(requireContext(),PlayerService::class.java)
        requireContext().bindService(playerIntent,playerConnection,Context.BIND_AUTO_CREATE)
    }

    private fun setDataType(data:Any) {
        when(songListType()) {
            Consts.LIST_TYPE_LOCAL->{
                data as LocalSong
                songName = data.name
                songSinger = data.singer
                songId = data.songId
            }
            Consts.LIST_TYPE_HISTORY->{
                data as HistorySong
                songName = data.name
                songSinger = data.singer
                songId = data.songId
            }
            Consts.LIST_TYPE_DOWNLOAD->{
                data as DownloadSong
                songName = data.songName
                songSinger = data.singer
                songId = data.songId
            }
            Consts.LIST_TYPE_LOVE->{
                data as LoveSong
                songName = data.name
                songSinger = data.singer
                songId = data.songId
            }
        }
    }
    private fun setCurrentSong(position:Int) {
        when(songListType()) {
            Consts.LIST_TYPE_LOCAL->{
//                currentSong as LocalSong
                currentSong = localSongs[position]
            }
            Consts.LIST_TYPE_HISTORY->{
//                currentSong as HistorySong
                currentSong = recentListenSongs[position]
            }
            Consts.LIST_TYPE_DOWNLOAD->{
                currentSong = downloadedSongs[position]
            }
            Consts.LIST_TYPE_LOVE->{
                currentSong = lovedSongs[position]
            }
        }
    }


    open fun playAll() {

    }

    open fun doFunc() {

    }
    open fun downloadAll() {

    }


    override fun onDestroy() {
        super.onDestroy()
        requireContext().unbindService(playerConnection)
    }
}
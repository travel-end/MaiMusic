package wind.maimusic.base

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_play_all.*
import wind.maimusic.R
import wind.maimusic.service.PlayerService
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
    private lateinit var ivPlayAll:ImageView
    private lateinit var tvDownloadAll:TextView
    protected lateinit var flPlayAll:FrameLayout
    protected var lastPosition:Int = -1
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
    }


    open fun initTitle(title:String,func:String) {
        tvTitle.text = title
        tvFunc.text = func
    }

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
package wind.maimusic.base

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import wind.maimusic.R
import wind.maimusic.service.PlayerService
import wind.widget.effcientrv.EfficientAdapter

/**
 * @By Journey 2020/10/28
 * @Description
 */
abstract class BaseSongListFragment<VM:BaseViewModel> :BaseLifeCycleFragment<VM>() {
    protected var playerBinder : PlayerService.PlayerBinder?=null
    protected lateinit var rvSongList:RecyclerView
    protected lateinit var flPlayAll:FrameLayout
    protected lateinit var ivPlayAll:ImageView

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val playerIntent = Intent(requireContext(),PlayerService::class.java)
        requireContext().bindService(playerIntent,playerConnection,Context.BIND_AUTO_CREATE)
    }

    override fun initView() {
        super.initView()
        rvSongList = mRootView.findViewById(R.id.song_list_rv)
        flPlayAll = mRootView.findViewById(R.id.view_song_list_play_all)
        ivPlayAll= mRootView.findViewById(R.id.view_song_list_iv_play_all)
    }
    // 判断点击的是否为上一个点击的项目
    open fun checkPosition(position:Int,adapter:EfficientAdapter<*>?) {
        if (position != lastPosition) {
            if (lastPosition != -1) adapter?.notifyItemChanged(lastPosition)
            lastPosition = position
        }
        adapter?.notifyItemChanged(lastPosition)
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().unbindService(playerConnection)
    }
}
package wind.maimusic.base

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import wind.maimusic.R
import wind.maimusic.service.DownloadService
import wind.maimusic.service.PlayerService

/**
 * @By Journey 2020/10/25
 * @Description
 */
abstract class BaseActivity:AppCompatActivity() {
    protected var playerServiceBinder:PlayerService.PlayerBinder?=null
    protected var downloadServiceBinder:DownloadService.DownloadBinder?=null
    private var isBindService:Boolean = false
    private var isDownloadServiceBind:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId())
        initView()
        initStatusBar()
        initData()
        initAction()
    }
    /**
     * 布局id
     */
    abstract fun layoutResId():Int

    open fun initView() {

    }
    open fun initData() {

    }
    open fun initAction() {

    }
    open fun initStatusBar() {
        ImmersionBar
            .with(this)
            .statusBarView(R.id.top_view)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .init()
    }

    open fun startService() {
        val playerIntent = Intent(this,PlayerService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(playerIntent)
        } else {
            startService(playerIntent)
        }
        bindService(playerIntent, playerConnection, Context.BIND_AUTO_CREATE)
        isBindService = true
    }

    open fun bindService() {
        val playerIntent = Intent(this, PlayerService::class.java)
        bindService(playerIntent, playerConnection, Context.BIND_AUTO_CREATE)
        isBindService = true
    }

    open fun bindDownloadService() {
        val downloadIntent = Intent(this,DownloadService::class.java)
        bindService(downloadIntent,downloadConnection,Context.BIND_AUTO_CREATE)
    }

    open fun serviceConnection() {

    }

    private val playerConnection = object :ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            playerServiceBinder = p1 as PlayerService.PlayerBinder
            serviceConnection()
        }
    }
    private val downloadConnection = object :ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            downloadServiceBinder = p1 as DownloadService.DownloadBinder
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            onDownloadServiceConnection()
        }
    }

    open fun onDownloadServiceConnection() {

    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBindService) {
            unbindService(playerConnection)
        }
        if (isDownloadServiceBind) {
            unbindService(downloadConnection)
        }
    }
}
package wind.maimusic.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class PlayerService : Service() {
    // todo 播放通知

    private val listType:Int = 0 // 0表示的播放的詩網絡音乐

    private val mediaPlayer by lazy {
        MediaPlayer()
    }

    override fun onCreate() {
        super.onCreate()
//        val song = SongUtil.getSong()
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    inner class PlayerBinder:Binder() {

    }
}

package wind.maimusic.service

import android.media.MediaPlayer
import android.net.Uri
import android.os.PowerManager
import android.preference.PreferenceManager
import wind.maimusic.MaiApp
import wind.maimusic.utils.LogUtil
import wind.maimusic.utils.SpUtil
import java.lang.ref.WeakReference

/**
 * @By Journey 2020/11/24
 * @Description
 */
class MusicPlayerEngine(service: PlayerService) : MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener,
    MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener {
    companion object {
        private const val TAG = "MusicPlayerEngine"
    }
    private var mIsInitialized:Boolean = false // 是否已经初始化
    private var mIsPrepared:Boolean = false
    private var mService:WeakReference<PlayerService> = WeakReference(service)

    private val mMediaPlayer = MediaPlayer()
    init {
        mMediaPlayer.setWakeMode(mService.get(),PowerManager.PARTIAL_WAKE_LOCK)
    }

    fun setDataSource(path:String) {
    }

    private fun setDataSourceImpl(mediaPlayer: MediaPlayer,path: String?):Boolean {
        if (path == null) return false
        try {
           if (mediaPlayer.isPlaying) mediaPlayer.stop()
            mIsPrepared = false
            mediaPlayer.reset()
            val cacheSetting = SpUtil.getBoolean("key_cache_mode",true)
            // 本地歌曲无需缓存
            if (path.startsWith("content://") || path.startsWith("/storage")) {
                mService.get()?.let { mediaPlayer.setDataSource(it,Uri.parse(path)) }
            } else if (cacheSetting) {
                // 开启缓存，读取缓存
            } else {
                // 不缓存
                mediaPlayer.setDataSource(path)
            }
            mediaPlayer.run {
                setOnPreparedListener(this@MusicPlayerEngine)
                setOnBufferingUpdateListener(this@MusicPlayerEngine)
                setOnErrorListener(this@MusicPlayerEngine)
                setOnCompletionListener(this@MusicPlayerEngine)
                prepareAsync()
            }
        } catch (e:Exception) {
            LogUtil.e(TAG,"exception:${e.message}")
            e.printStackTrace()
            return false
        }
        return true
    }


    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
    }

    override fun onPrepared(mp: MediaPlayer?) {
    }
}
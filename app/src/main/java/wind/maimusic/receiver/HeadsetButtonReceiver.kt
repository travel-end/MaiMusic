package wind.maimusic.receiver
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import wind.maimusic.utils.LogUtil
import java.util.*

/**
 * @By Journey 2020/11/6
 * @Description
 * 通过耳机控制播放状态
 * @author https://blog.csdn.net/u010005281/article/details/79550492
 */
class HeadsetButtonReceiver:BroadcastReceiver() {
    private var clickCount:Int = 0
    private val timer = Timer()
    private var onHeadsetClickListener:OnHeadsetClickListener?=null
    override fun onReceive(context: Context?, intent: Intent?) {
        LogUtil.e("----HeadsetButtonReceiver---1")
        intent?.let { i ->
            LogUtil.e("----HeadsetButtonReceiver---2")
            if (Intent.ACTION_MEDIA_BUTTON == i.action) {
                val keyEvent = i.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)
                if (keyEvent != null) {
                    if (keyEvent.keyCode == KeyEvent.KEYCODE_HEADSETHOOK && keyEvent.action == KeyEvent.ACTION_UP) {
                        Log.e("headSet","HeadsetButtonReceiver："+"onReceive:"+"if:"+"if"+" HEADSETHOOK");
                        clickCount += 1
                        if (clickCount == 1) {
                            val task = HeadsetTimerTask()
                            timer.schedule(task,1000)
                        }
                    } else if (keyEvent.keyCode == KeyEvent.KEYCODE_MEDIA_NEXT) {
                        Log.e("headSet","HeadsetButtonReceiver："+"onReceive:"+"if:"+"if"+" KEYCODE_HEADSETHOOK");
                        handler.sendEmptyMessage(2)
                    } else if (keyEvent.keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS) {
                        Log.e("headSet","HeadsetButtonReceiver："+"onReceive:"+"if:"+"if"+ " KEYCODE_MEDIA_PREVIOUS");
                        handler.sendEmptyMessage(3)
                    }
                }
            }
        }
    }
    fun registerHeadsetReceiver(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val name = ComponentName(context.packageName,HeadsetButtonReceiver::class.java.name)
        audioManager.registerMediaButtonEventReceiver(name)
    }

    fun unregisterHeadsetReceiver(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val name = ComponentName(context.packageName,HeadsetButtonReceiver::class.java.name)
        audioManager.unregisterMediaButtonEventReceiver(name)
    }

    inner class HeadsetTimerTask :TimerTask() {
        override fun run() {
            try {
                when {
                    clickCount==1 -> {
                        handler.sendEmptyMessage(1)
                    }
                    clickCount==2 -> {
                        handler.sendEmptyMessage(2)
                    }
                    clickCount>=3 -> {
                        handler.sendEmptyMessage(3)
                    }
                }
            } catch (e:Exception) {
                e.printStackTrace()
            }
        }
    }
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            try {
                when (msg.what) {
                    1 -> {
                        onHeadsetClickListener?.playOrPause()
                    }
                    2 -> {
                        onHeadsetClickListener?.playNext()
                    }
                    3 -> {
                        onHeadsetClickListener?.playPrevious()
                    }
                }
            }catch (e:Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setOnHeadsetClickListener(listener: OnHeadsetClickListener) {
        this.onHeadsetClickListener = listener
    }
    interface OnHeadsetClickListener {
        fun playOrPause()
        fun playNext()
        fun playPrevious()
    }
}
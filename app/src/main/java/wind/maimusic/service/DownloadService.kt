package wind.maimusic.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import wind.maimusic.download.DownloadTask
import wind.maimusic.model.DownloadSong
import wind.maimusic.utils.LogUtil
import wind.maimusic.utils.toast
import java.util.*

class DownloadService : Service() {
    private var downloadTask:DownloadTask?=null
    private var downloadUrl:String?=null
    private val downloadQueue = LinkedList<DownloadSong>()
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
    inner class DownloadBinder:Binder() {
        fun startDownload(downloadSong: DownloadSong) {
            try {

            } catch (e:Exception) {
                e.printStackTrace()
                LogUtil.e("------DownloadService DownloadBinder startDownload error:${e.message}")
            }
            if (downloadTask != null) {
                "已加入下载队列".toast()
            } else {
                "开始下载".toast()
                start()
            }
        }
        fun start() {

        }

    }
}

package wind.maimusic.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import wind.maimusic.R
import wind.maimusic.download.DownloadListener
import wind.maimusic.download.DownloadTask
import wind.maimusic.model.download.DownloadEvent
import wind.maimusic.model.download.DownloadSong
import wind.maimusic.room.database.MaiDatabase
import wind.maimusic.ui.activities.MainActivity
import wind.maimusic.utils.*
import wind.widget.cost.Consts
import java.util.*

class DownloadService : Service() {
    private var downloadTask:DownloadTask?=null
    private var downloadUrl:String?=null
    private val downloadQueue = LinkedList<DownloadSong>()
    private var position:Int = 0// 下载歌曲在下载歌曲列表的位置
    private val downloadBinder = DownloadBinder()
    override fun onBind(intent: Intent): IBinder {
        return downloadBinder
    }
    inner class DownloadBinder:Binder() {
        fun startDownload(downloadSong: DownloadSong) {
            try {
                postDownloadEvent(downloadSong)
            } catch (e:Exception) {
                e.printStackTrace()
                LogUtil.e("------DownloadService DownloadBinder startDownload error:${e.message}")
            }
            if (downloadTask != null) {
                R.string.in_download_queue.getStringRes().toast()
            } else {
                R.string.start_download.getStringRes().toast()
                start()
            }
        }

    }

    /**
     * offer(E e)，向链表末尾添加元素，返回是否成功；
     * public E poll()，删除并返回第一个元素；
     * public E peek()，返回第一个元素；
     */
    private fun postDownloadEvent(downloadSong: DownloadSong) {
        val downloadDao =  MaiDatabase.getDatabase().downloadSongDao()
        GlobalUtil.execute {
            val downloadSongList =downloadDao.findDownloadedSongBySongId(downloadSong.songId?:"")
            if (downloadSongList.isNotEmpty()) {
                val historyDownloadInfo =downloadSongList[0]
                historyDownloadInfo.status = Consts.DOWNLOAD_WAIT
                downloadDao.addDownloadSong(historyDownloadInfo)
                Bus.post(Consts.DOWNLOAD_EVENT, DownloadEvent(downloadStatus = Consts.DOWNLOAD_PAUSED, downloadSong = historyDownloadInfo))
                downloadQueue.offer(historyDownloadInfo)
            } else {
                position = downloadDao.findAllDownloadSong().size
                downloadSong.position = position
                downloadSong.status= Consts.DOWNLOAD_WAIT
                downloadDao.addDownloadSong(downloadSong)
                downloadQueue.offer(downloadSong)
                Bus.post(Consts.DOWNLOAD_EVENT, DownloadEvent(downloadStatus = Consts.TYPE_DOWNLOAD_ADD))
            }
        }
    }
    private fun start() {
        if (downloadTask == null && downloadQueue.isNotEmpty()) {
            val downloadSong = downloadQueue.peek()
            if (downloadSong != null) {
                var songList :List<DownloadSong>?=null
                GlobalUtil.execute {
                    songList = MaiDatabase.getDatabase().downloadSongDao().findDownloadedSongBySongId(downloadSong.songId?:"")
                }
                LogUtil.e("------DownloadService start downloadList:$songList; list.size:${songList?.size}")
                if (isNotNullOrEmpty(songList)) {
                    /*当前下载的歌曲*/
                    val currentDownloadSong = songList!![0]
                    currentDownloadSong.status = Consts.DOWNLOAD_READY
                    Bus.post(Consts.DOWNLOAD_EVENT, DownloadEvent(downloadStatus = Consts.TYPE_DOWNLOADING,downloadSong = currentDownloadSong))
                    downloadUrl = currentDownloadSong.url
                    downloadTask = DownloadTask(downloadListener)
                    downloadTask?.execute(currentDownloadSong)
                    notificationManager.notify(1, getNotification("正在下载: ${currentDownloadSong.songName ?: "麦浪音乐"}", 0))
                }
            }
        }
    }

    private val downloadListener = object :DownloadListener {
        override fun onProgress(downloadSong: DownloadSong) {
            downloadSong.status = Consts.DOWNLOAD_ING
            Bus.post(Consts.DOWNLOAD_EVENT, DownloadEvent(downloadStatus = Consts.TYPE_DOWNLOADING,downloadSong = downloadSong))
            if (downloadSong.progress != 100) {
                notificationManager.notify(1, getNotification("正在下载: ${downloadSong.songName}", downloadSong.progress ?: 0))
            } else {
                if (downloadQueue.isEmpty()) {
                    notificationManager.notify(1, getNotification("歌曲下载完毕！", -1))
                }
            }
        }

        override fun onSuccess() {
            downloadTask = null
            val downloadSong = downloadQueue.poll()
            if (downloadSong != null) {
                operateDb(downloadSong)
                start()
                stopForeground(true)
            }
            if (downloadQueue.isEmpty()) notificationManager.notify(1, getNotification("下载成功啦~", -1))
        }

        override fun hasDownloaded() {
            downloadTask = null
            R.string.song_has_download.getStringRes().toast()
        }
        override fun onFailed() {
            downloadTask = null
            stopForeground(true)
            notificationManager.notify(1,getNotification("下载失败",-1))
            R.string.download_failed.getStringRes().toast()
        }

        override fun onPaused() {
            downloadTask = null
            val downloadSong = downloadQueue.poll()
            downloadSong?.songId?.let {
                updateDbOfPause(it)
            }
            notificationManager.notify(1,getNotification("下载已暂停",-1))
            start()
            downloadSong?.status = Consts.DOWNLOAD_PAUSED
            Bus.post(Consts.DOWNLOAD_EVENT, DownloadEvent(downloadStatus = Consts.DOWNLOAD_PAUSED,downloadSong = downloadSong))
            "下载已暂停".toast()
        }

        override fun onCancel() {
            downloadTask = null
            stopForeground(true)
        }

        override fun onStart() {
        }
    }

    private fun operateDb(downloadSong: DownloadSong) {
        downloadSong.songId?.let {
            updateDb(it)
            deleteDb(it)
            Bus.post(Consts.DOWNLOAD_EVENT, DownloadEvent(downloadStatus = Consts.TYPE_DOWNLOAD_SUCCESS))
            Bus.post(Consts.DOWNLOAD_EVENT, DownloadEvent(downloadStatus = Consts.LIST_TYPE_DOWNLOAD))
        }
    }

    private fun updateDb(songId:String) {
        val downloadDao = MaiDatabase.getDatabase().downloadSongDao()
        GlobalUtil.execute {
            val id = downloadDao.findIdBySongId(songId)[0].id
            LogUtil.e("------DownloadService updateDb id:$id")
            if (id != null && id != 0L) {
                val songIdList = downloadDao.findDownloadSongById(id)
                for (song in songIdList) {
                    downloadDao.updateDownloadSongPositionBySongId((song.position?:0)-1,song.songId?:"")
                }
            }
        }
    }
    private fun deleteDb(songId:String) {
        GlobalUtil.async {
            MaiDatabase.getDatabase().downloadSongDao().deleteDownloadedBySongId(songId)
        }
    }

    private fun updateDbOfPause(songId: String) {
        GlobalUtil.execute {
            val statusList = MaiDatabase.getDatabase().downloadSongDao().findDownloadedSongBySongId(songId?:"")
            val downloadSong = statusList[0]
            MaiDatabase.getDatabase().downloadSongDao().updateDownloadSongStatusBySongId(Consts.DOWNLOAD_PAUSED,downloadSong.songId?:"")
        }
    }

    private val notificationManager get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun getNotification(title: String, progress: Int): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id = "channel_01"
            val name = "下载通知"
            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
            val builder = Notification.Builder(this, id)
                .setSmallIcon(R.drawable.temp_logo)
                .setContentIntent(pi)
                .setContentTitle(title)
            if (progress > 0) {
                builder.setContentText("$progress%")
                builder.setProgress(100, progress, false)
            }
            return builder.build()
        } else {
            val builder = NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.drawable.temp_logo)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.temp_logo))
                .setContentIntent(pi)
                .setContentTitle(title)
            if (progress > 0) {
                builder.setContentText("$progress%")
                builder.setProgress(100, progress, false)
            }
            return builder.build()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadTask = null
        downloadQueue.clear()
    }
}

package wind.maimusic.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import wind.maimusic.model.*
import wind.maimusic.room.MaiDatabase
import wind.maimusic.utils.*
import wind.widget.cost.Consts
import java.io.IOException

class PlayerService : Service() {
    private var localSongs: MutableList<LocalSong>? = null
    private var historySongs: MutableList<HistorySong>? = null
    private var downloadSongs: MutableList<Downloaded>? = null
    private var loveSongs: MutableList<LoveSong>? = null
    private var listType: Int = 0 // 0表示的播放的詩網絡音乐
    private var currentPosition: Int = 0
    private var isPlaying: Boolean = false
    private var isPaused: Boolean = false
    private val playerBinder = PlayerBinder()
    private var playMode: Int = Consts.PLAY_ORDER
    private val mediaPlayer by lazy {
        MediaPlayer()
    }

    override fun onCreate() {
        super.onCreate()
        val song = SongUtil.getSong()
        song?.let {
            listType = it.listType
            when (listType) {
                Consts.LIST_TYPE_LOCAL -> {
                    localSongs = GlobalUtil.execute {
                        MaiDatabase.getDatabase().localSongDao().findAllLocalSong().toMutableList()
                    }
                    LogUtil.e("--->onCreate localSongs: $localSongs")
                }
                Consts.LIST_TYPE_HISTORY -> {
                    historySongs = GlobalUtil.execute {
                        MaiDatabase.getDatabase().historySongDao().findAllHistorySong()
                            .toMutableList()
                    }
                    LogUtil.e("--->onCreate historySongs: $historySongs")
                    SongUtil.getSong()?.let { s ->
                        s.position = 0
                        SongUtil.saveSong(s)
                    }
                }
                Consts.LIST_TYPE_DOWNLOAD -> {
                    downloadSongs = DownloadedUtil.getSongFromFile()
                    LogUtil.e("--->onCreate downloadSongs: $downloadSongs")
                }
                Consts.LIST_TYPE_LOVE -> {
                    loveSongs = GlobalUtil.execute {
                        MaiDatabase.getDatabase().loveSongDao().findAllLoveSong().toMutableList()
                    }
                    LogUtil.e("--->onCreate loveSongs: $loveSongs")
                }
                else -> {

                }
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        mediaPlayer.setOnCompletionListener {
            Bus.post(Consts.SONG_STATUS_CHANGE, Consts.SONG_PAUSE)
            val song = SongUtil.getSong()
            if (song != null) {
                currentPosition = song.position
                when (listType) {
                    Consts.LIST_TYPE_LOCAL -> {
                        currentPosition = PlayServiceHelper.getNextSongPosition(
                            currentPosition,
                            playMode,
                            localSongs?.size
                        )
                        PlayServiceHelper.saveLocalSong(currentPosition)
                    }
                    Consts.LIST_TYPE_HISTORY -> {
                        currentPosition = PlayServiceHelper.getNextSongPosition(
                            currentPosition,
                            playMode,
                            historySongs?.size
                        )
                        PlayServiceHelper.saveHistorySong(currentPosition, historySongs)
                    }
                    Consts.LIST_TYPE_DOWNLOAD -> {
                        currentPosition = PlayServiceHelper.getNextSongPosition(
                            currentPosition,
                            playMode,
                            downloadSongs?.size
                        )
                        PlayServiceHelper.saveDownloadSong(currentPosition, downloadSongs)
                    }
                    Consts.LIST_TYPE_LOVE -> {
                        currentPosition = PlayServiceHelper.getNextSongPosition(
                            currentPosition,
                            playMode,
                            loveSongs?.size
                        )
                        PlayServiceHelper.saveLoveSong(currentPosition)
                    }
                }
                if (listType != 0) {
                    playerBinder.play(listType)
                } else {
                    playerBinder.stop()
                }
            }
        }
        mediaPlayer.setOnErrorListener { mediaPlayer, i, i2 ->
            LogUtil.e("--->播放出错：setOnErrorListener:$i, $i2")
            "播放出错".toast()
            true
        }
        return playerBinder
    }

    inner class PlayerBinder : Binder() {
        fun setPlayModel(mode: Int) {
            playMode = mode
        }

        fun play(type: Int, restartTime: Int? = null) {
            try {
                listType = type
                if (listType != 0) {
                    when (listType) {
                        Consts.LIST_TYPE_LOCAL -> {
                            localSongs = GlobalUtil.execute {
                                MaiDatabase.getDatabase().localSongDao().findAllLocalSong()
                                    .toMutableList()
                            }
                            LogUtil.e("--->play localsongs:$localSongs")
                        }
                        Consts.LIST_TYPE_HISTORY -> {
                            // todo
                        }
                        Consts.LIST_TYPE_DOWNLOAD -> {
                            downloadSongs = DownloadedUtil.getSongFromFile()
                            LogUtil.e("--->play downloadSongs:$downloadSongs")
                        }
                        Consts.LIST_TYPE_LOVE -> {
                            loveSongs = GlobalUtil.execute {
                                MaiDatabase.getDatabase().loveSongDao().findAllLoveSong()
                                    .toMutableList()
                            }
                            if (isNotNullOrEmpty(localSongs)) {
                                loveSongs = PlayServiceHelper.orderLoveList(loveSongs!!)
                            }
                            LogUtil.e("--->play loveSongs:$loveSongs")
                        }
                    }
                }
                val song = SongUtil.getSong()
                if (song != null) {
                    currentPosition = song.position
                    mediaPlayer.reset()
                    when (listType) {
                        Consts.LIST_TYPE_LOCAL -> {
                            if (isNotNullOrEmpty(localSongs)) {
                                val playUrl = localSongs!![currentPosition].url
                                if (playUrl.isNotNullOrEmpty()) {
                                    mediaPlayer.setDataSource(playUrl)
                                    startPlay(restartTime)
                                }
                            }
                        }
                        Consts.LIST_TYPE_HISTORY -> {
                            if (isNotNullOrEmpty(historySongs)) {
                                val playUrl = historySongs!![currentPosition].url
                                if (playUrl.isNotNullOrEmpty()) {
                                    mediaPlayer.setDataSource(playUrl)
                                    startPlay(restartTime)
                                }
                            }
                        }
                        Consts.LIST_TYPE_DOWNLOAD -> {
                            if (isNotNullOrEmpty(downloadSongs)) {
                                val playUrl = downloadSongs!![currentPosition].url
                                if (playUrl.isNotNullOrEmpty()) {
                                    mediaPlayer.setDataSource(playUrl)
                                    startPlay(restartTime)
                                }
                            }
                        }
                        Consts.LIST_TYPE_LOVE -> {
                            if (isNotNullOrEmpty(loveSongs)) {
                                val playUrl = loveSongs!![currentPosition].url
                                if (playUrl.isNotNullOrEmpty()) {
                                    mediaPlayer.setDataSource(playUrl)
                                    startPlay(restartTime)
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                LogUtil.e("play exception:${e.message}")
                "播放异常".toast()
            }
        }

        fun playOnline(restartTime: Int? = null) {
            try {
                val song = SongUtil.getSong()
                if (song != null) {
                    mediaPlayer.run {
                        reset()
                        val playUrl = song.url
                        if (playUrl.isNotNullOrEmpty()) {
                            setDataSource(playUrl)
                            prepareAsync()
                            setOnPreparedListener { mp ->
                                this@PlayerService.isPlaying = true
                                PlayServiceHelper.save2History()
                                if (restartTime != null && restartTime != 0) {
                                    mp.seekTo(restartTime)
                                }
                                mp.start()
                                Bus.post(Consts.SONG_STATUS_CHANGE, Consts.SONG_CHANGE)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                "播放在线歌曲出错".toast()
            }

        }

        fun pause() {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                isPlaying = false
                isPaused = true
                Bus.post(Consts.SONG_STATUS_CHANGE, Consts.SONG_PAUSE)
            }
        }

        fun resume() {
            if (isPaused) {
                mediaPlayer.start()
                isPlaying = true
                isPaused = false
                Bus.post(Consts.SONG_STATUS_CHANGE, Consts.SONG_RESUME)
            }
        }

        fun next() {
            Bus.post(Consts.SONG_STATUS_CHANGE, Consts.SONG_RESUME)
            val song = SongUtil.getSong()
            if (song != null) {
                currentPosition = song.position
                when (listType) {
                    Consts.LIST_TYPE_LOCAL -> {
                        currentPosition = PlayServiceHelper.getNextSongPosition(
                            currentPosition,
                            playMode,
                            localSongs?.size
                        )
                        PlayServiceHelper.saveLocalSong(currentPosition)
                    }
                    Consts.LIST_TYPE_HISTORY -> {
                        currentPosition = PlayServiceHelper.getNextSongPosition(
                            currentPosition,
                            playMode,
                            historySongs?.size
                        )
                        PlayServiceHelper.saveHistorySong(currentPosition, historySongs)
                    }
                    Consts.LIST_TYPE_DOWNLOAD -> {
                        currentPosition = PlayServiceHelper.getNextSongPosition(
                            currentPosition,
                            playMode,
                            downloadSongs?.size
                        )
                        PlayServiceHelper.saveDownloadSong(currentPosition, downloadSongs)
                    }
                    Consts.LIST_TYPE_LOVE -> {
                        currentPosition = PlayServiceHelper.getNextSongPosition(
                            currentPosition,
                            playMode,
                            loveSongs?.size
                        )
                        PlayServiceHelper.saveLoveSong(currentPosition)
                    }
                }
                if (listType != 0) {
                    play(listType)
                }
            }
        }

        fun last() {
            Bus.post(Consts.SONG_STATUS_CHANGE, Consts.SONG_RESUME)
            val song = SongUtil.getSong()
            if (song != null) {
                currentPosition = song.position
                when (listType) {
                    Consts.LIST_TYPE_LOCAL -> {
                        currentPosition = PlayServiceHelper.getLastSongPosition(
                            currentPosition,
                            playMode,
                            localSongs?.size
                        )
                        PlayServiceHelper.saveLocalSong(currentPosition)
                    }
                    Consts.LIST_TYPE_HISTORY -> {
                        currentPosition = PlayServiceHelper.getLastSongPosition(
                            currentPosition,
                            playMode,
                            historySongs?.size
                        )
                        PlayServiceHelper.saveHistorySong(currentPosition, historySongs)
                    }
                    Consts.LIST_TYPE_DOWNLOAD -> {
                        currentPosition = PlayServiceHelper.getLastSongPosition(
                            currentPosition,
                            playMode,
                            downloadSongs?.size
                        )
                        PlayServiceHelper.saveDownloadSong(currentPosition, downloadSongs)
                    }
                    Consts.LIST_TYPE_LOVE -> {
                        currentPosition = PlayServiceHelper.getLastSongPosition(
                            currentPosition,
                            playMode,
                            loveSongs?.size
                        )
                        PlayServiceHelper.saveLoveSong(currentPosition)
                    }
                }
                if (listType != 0) {
                    play(listType)
                }
            }
        }

        fun stop() {
            mediaPlayer.stop()
            isPlaying = false

            // TODO: 2020/10/28 在调用stop后如果需要再次通过start进行播放，需要重新调用prepare函数
        }

        val playing get() = isPlaying

        val mp get() = mediaPlayer

        // 当前播放进度（秒）
        val playingTime get() = mediaPlayer.currentPosition / 1000
    }


    @Throws(IOException::class)
    private fun startPlay(restartTime: Int? = null) {
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { mp ->
            isPlaying = true
            if (restartTime != null && restartTime != 0) {
                mp.seekTo(restartTime)
            }
            mp.start()
            PlayServiceHelper.save2History()
            Bus.post(Consts.SONG_STATUS_CHANGE, Consts.SONG_CHANGE)
        }
    }
}

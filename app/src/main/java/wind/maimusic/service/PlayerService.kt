package wind.maimusic.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import wind.maimusic.model.*
import wind.maimusic.model.firstmeet.FirstMeetSong
import wind.maimusic.room.MaiDatabase
import wind.maimusic.room.OnlineSongDatabase
import wind.maimusic.utils.*
import wind.widget.cost.Consts
import wind.widget.model.Song
import java.io.IOException

/**
 * 关于Service何时销毁
 * StartService开启的Service，调用者退出后Service仍然存在
 * BindService开启的Service，调用者退出后，Service随着调用者的退出销毁
 *
 * 即使一个Service被startService启动多次，onCreate方法也只会调用一次
 */
class PlayerService : Service() {
    private var localSongs: MutableList<LocalSong>? = null
    private var historySongs: MutableList<HistorySong>? = null
    private var downloadSongs: MutableList<Downloaded>? = null
    private var loveSongs: MutableList<LoveSong>? = null
    private var firstMeetSongs: MutableList<FirstMeetSong>? = null
    private var onlineSongs: MutableList<OnlineSong>? = null
    private var listType: Int = 0
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
        LogUtil.e("-------PlayService----onCreate currentSong:$song; songName:${song?.songName}------")
        if (song != null) {
            listType = song.listType
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
                        MaiDatabase.getDatabase().loveSongDao().findAllLoveSongs().toMutableList()
                    }
                    LogUtil.e("--->onCreate loveSongs: $loveSongs")
                }
                Consts.LIST_TYPE_ONLINE -> {
                    when (song.onlineSubjectType) {
                        Consts.ONLINE_LIST_TYPE_FIRST_MEET -> {
                            firstMeetSongs = GlobalUtil.execute {
                                OnlineSongDatabase.getDatabase().firstMeetSongDao().findAllFirstSong().toMutableList()
                            }
                        }
                        else->{}
                    }
                }
                else -> {}
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        LogUtil.e("-------PlayService----onBind------")
        mediaPlayer.setOnCompletionListener {
//            Bus.post(Consts.SONG_STATUS_CHANGE, Consts.SONG_PAUSE)
            Bus.post(Consts.SONG_STATUS_CHANGE, Consts.SONG_COMPLETE)// 歌曲播放完毕 在播放页面监听
            val song = SongUtil.getSong()
            if (song != null) {
                currentPosition = song.position
                LogUtil.e("-------PlayService----onBind listType:$listType------")
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
                    // 在线音乐
                    Consts.LIST_TYPE_ONLINE -> {
                        when (song.onlineSubjectType) {
                            Consts.ONLINE_LIST_TYPE_FIRST_MEET -> {
                                currentPosition = if (currentPosition==firstMeetSongs!!.size-1 && playMode == Consts.PLAY_ORDER) {
                                    0
                                } else {
                                    PlayServiceHelper.getNextSongPosition(
                                        currentPosition,
                                        playMode,
                                        firstMeetSongs?.size
                                    )
                                }
                                LogUtil.e("-------PlayService----onBind currentPosition:$currentPosition------")
                                PlayServiceHelper.saveFirstMeetSong(currentPosition,firstMeetSongs)
                            }
                        }
                    }
                }
                if (listType != 0) {
                    if (listType == Consts.LIST_TYPE_ONLINE) {
                        playerBinder.play(type = Consts.LIST_TYPE_ONLINE,onlineSubjectType = song.onlineSubjectType)
                    } else {
                        playerBinder.play(listType)
                    }
                } else {
                    playerBinder.stop()// TODO: 2020/10/30 对于未知音乐（不在listType之内的音乐的处理）
                }
            }
        }
        mediaPlayer.setOnErrorListener { _, i, i2 ->
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
        // 播放下一首（包括在线音乐）、播放暂停的音乐
        fun play(type: Int,onlineSubjectType:Int?=null, restartTime: Int? = null) {
            try {
                listType = type
                if (listType != 0) {
                    when (listType) {
                        Consts.LIST_TYPE_LOCAL -> {
                            localSongs = GlobalUtil.execute {
                                MaiDatabase.getDatabase().localSongDao().findAllLocalSong()
                                    .toMutableList()
                            }
//                            LogUtil.e("--->play localsongs:$localSongs")
                        }
                        Consts.LIST_TYPE_HISTORY -> {
                            // todo
                        }
                        Consts.LIST_TYPE_DOWNLOAD -> {
                            downloadSongs = DownloadedUtil.getSongFromFile()
//                            LogUtil.e("--->play downloadSongs:$downloadSongs")
                        }
                        Consts.LIST_TYPE_LOVE -> {
                            loveSongs = GlobalUtil.execute {
                                MaiDatabase.getDatabase().loveSongDao().findAllLoveSongs()
                                    .toMutableList()
                            }
                            if (isNotNullOrEmpty(localSongs)) {
                                loveSongs = PlayServiceHelper.orderLoveList(loveSongs!!)
                            }
//                            LogUtil.e("--->play loveSongs:$loveSongs")
                        }
                        Consts.LIST_TYPE_ONLINE->{
                            when(onlineSubjectType) {
                                Consts.ONLINE_LIST_TYPE_FIRST_MEET->{
                                    firstMeetSongs = GlobalUtil.execute {
                                        OnlineSongDatabase.getDatabase().firstMeetSongDao().findAllFirstSong().toMutableList()
                                    }
                                }
                            }
                        }
                    }
                }
                val song = SongUtil.getSong() // 根据播放模式获取的下一首音乐
                LogUtil.e("-------PlayService PlayerBinder play imgUrl:${song?.imgUrl}")
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
                        // 播放在线音乐（只有自动播放下一曲才会执行这里  其他的是走的playOnline）
                        Consts.LIST_TYPE_ONLINE->{
                            when(onlineSubjectType){
                                Consts.ONLINE_LIST_TYPE_FIRST_MEET->{
                                    if (isNotNullOrEmpty(firstMeetSongs)) {
                                        val currentSongId = firstMeetSongs!![currentPosition].songId
                                        getFirstMeetSongUrl(song,currentSongId,restartTime)
                                    }
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
        private fun getFirstMeetSongUrl(song:Song,songId:String?,restartTime: Int?) {
            if (songId.isNotNullOrEmpty()) {
                val playUrl = PlayServiceHelper.getOnlineSongUrl(songId!!)
                if (playUrl.isNullOrEmpty()) {
                    currentPosition = PlayServiceHelper.getNextSongPosition(currentPosition,playMode,firstMeetSongs?.size)
                    PlayServiceHelper.saveFirstMeetSong(currentPosition,firstMeetSongs)
                    val nextSongId = firstMeetSongs!![currentPosition].songId
                    getFirstMeetSongUrl(song,nextSongId,restartTime)
                } else {
                    song.url = playUrl// 保存在线音乐的url 这样如果暂停后再次进入则会继续播放这首音乐
                    LogUtil.e("-------PlayService getFirstMeetSongUrl imgUrl:${song.imgUrl}")
                    SongUtil.saveSong(song)
                    mediaPlayer.setDataSource(playUrl)
                    startPlay(restartTime)
                }
            }
        }

        // 播放搜索的歌曲 还有？？
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
                                    Bus.post(Consts.SONG_STATUS_CHANGE, Consts.SONG_RESUME)
                                } else {
                                    Bus.post(Consts.SONG_STATUS_CHANGE, Consts.SONG_CHANGE)
                                }
                                // TODO: 2020/10/30 如果是由暂停进入播放的状态  发送的应该是pause 这样就不用刷新bottomPlayView左边的图片和名称了
                                mp.start()
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
                    Consts.LIST_TYPE_ONLINE ->{
                        when(song.onlineSubjectType) {
                            Consts.ONLINE_LIST_TYPE_FIRST_MEET->{
                                currentPosition = if (currentPosition==firstMeetSongs!!.size-1 && playMode == Consts.PLAY_ORDER) {
                                    0
                                } else {
                                    PlayServiceHelper.getNextSongPosition(
                                        currentPosition,
                                        playMode,
                                        firstMeetSongs?.size
                                    )
                                }
                                LogUtil.e("-------PlayService----onBind currentPosition:$currentPosition------")
                                PlayServiceHelper.saveFirstMeetSong(currentPosition,firstMeetSongs)
                            }
                        }
                    }
                }
                if (listType != 0) {
                    if (listType == Consts.LIST_TYPE_ONLINE) {
                        playerBinder.play(type = Consts.LIST_TYPE_ONLINE,onlineSubjectType = song.onlineSubjectType)
                    } else {
                        playerBinder.play(listType)
                    }
                }
            }
        }

        fun last() {
            Bus.post(Consts.SONG_STATUS_CHANGE, Consts.SONG_RESUME)// 通知首页的状态改变
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
                    Consts.LIST_TYPE_ONLINE ->{
                        when(song.onlineSubjectType) {
                            Consts.ONLINE_LIST_TYPE_FIRST_MEET->{
                                currentPosition = if (currentPosition==0 && playMode == Consts.PLAY_ORDER) {
                                    firstMeetSongs!!.size-1
                                } else {
                                    PlayServiceHelper.getLastSongPosition(
                                        currentPosition,
                                        playMode,
                                        firstMeetSongs?.size
                                    )
                                }
                                LogUtil.e("-------PlayService----onBind currentPosition:$currentPosition------")
                                PlayServiceHelper.saveFirstMeetSong(currentPosition,firstMeetSongs)
                            }
                        }
                    }

                }
                if (listType != 0) {
                    if (listType == Consts.LIST_TYPE_ONLINE) {
                        playerBinder.play(type = Consts.LIST_TYPE_ONLINE,onlineSubjectType = song.onlineSubjectType)
                    } else {
                        playerBinder.play(listType)
                    }
                }
            }
        }

        fun stop() {
            mediaPlayer.stop()
            isPlaying = false
        }

        val playing get() = isPlaying

        val mp get() = mediaPlayer

        // 当前播放进度（ms）
        val playingTime get() = mediaPlayer.currentPosition
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

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.e("-------PlayService----onDestroy------")
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}

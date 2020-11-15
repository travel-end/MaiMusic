package wind.maimusic.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import wind.maimusic.Constants
import wind.maimusic.model.*
import wind.maimusic.model.download.Downloaded
import wind.maimusic.room.database.MaiDatabase
import wind.maimusic.room.database.OnlineSongDatabase
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
    private var launchSongs: MutableList<OnlineSong>? = null
    private var onlineSongs: MutableList<OnlineSong>? = null
    private var justOnlineSongs:MutableList<JustOnlineSong>?=null
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
                }
                Consts.LIST_TYPE_HISTORY -> {
                    historySongs = GlobalUtil.execute {
                        MaiDatabase.getDatabase().historySongDao().findAllHistorySong()
                            .toMutableList()
                    }
                    SongUtil.getSong()?.let { s ->
                        s.position = 0
                        SongUtil.saveSong(s)
                    }
                }
                Consts.LIST_TYPE_DOWNLOAD -> {
                    downloadSongs = DownloadedUtil.getSongFromFile()
                }
                Consts.LIST_TYPE_LOVE -> {
                    loveSongs = GlobalUtil.execute {
                        MaiDatabase.getDatabase().loveSongDao().findAllLoveSongs().toMutableList()
                    }
                }
                Consts.LIST_TYPE_ONLINE -> {
                    when (song.onlineSubjectType) {
                        Consts.ONLINE_FIRST_LAUNCH -> {// 首次使用app
                            launchSongs = GlobalUtil.execute {
                                OnlineSongDatabase.getDatabase().onlineSongDao().findLaunchSongs().toMutableList()
                            }
                        }
                        Constants.ST_DAILY_RECOMMEND->{// 每日推荐歌曲
                            val startIndex = DataUtil.getTheDayStartIndex(Constants.ST_DAILY_RECOMMEND)
                            onlineSongs = GlobalUtil.execute {
                                OnlineSongDatabase.getDatabase().onlineSongDao().findRangeOnlineSongs(startIndex,Constants.PAGE_SIZE_DAILY_RECOMMEND).toMutableList()
                            }
                        }
                        Consts.JUST_ONLINE_SONG->{// 搜索的在线歌单（如专辑、、）
                            justOnlineSongs = GlobalUtil.execute {
                                OnlineSongDatabase.getDatabase().justOnlineSongDao().findJustOnlineSongs().toMutableList()
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
        /*音乐播放完毕*/
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
                            Consts.ONLINE_FIRST_LAUNCH -> {
                                // TODO: 2020/11/5 放到一个方法里面去 上面的也是跟这个一样的逻辑
                                if (isNotNullOrEmpty(launchSongs)) {
                                    currentPosition = if (currentPosition==launchSongs!!.size-1 && playMode == Consts.PLAY_ORDER) {
                                        0
                                    } else {
                                        PlayServiceHelper.getNextSongPosition(
                                            currentPosition,
                                            playMode,
                                            launchSongs?.size
                                        )
                                    }
                                }
                                LogUtil.e("-------PlayService----onBind currentPosition:$currentPosition------")
                                PlayServiceHelper.saveCurrentOnlineSong(currentPosition,Consts.ONLINE_FIRST_LAUNCH,launchSongs)
                            }
                            Constants.ST_DAILY_RECOMMEND->{
                                if (isNotNullOrEmpty(onlineSongs)) {
                                    currentPosition = if (currentPosition==onlineSongs!!.size-1 && playMode == Consts.PLAY_ORDER) {
                                        0
                                    } else {
                                        PlayServiceHelper.getNextSongPosition(
                                            currentPosition,
                                            playMode,
                                            onlineSongs?.size
                                        )
                                    }
                                }
                                LogUtil.e("-------PlayService----onBind currentPosition:$currentPosition------")
                                PlayServiceHelper.saveCurrentOnlineSong(currentPosition,Constants.ST_DAILY_RECOMMEND,onlineSongs)
                            }
                            Consts.JUST_ONLINE_SONG->{
                                if (isNotNullOrEmpty(justOnlineSongs)) {
                                    currentPosition = if (currentPosition==justOnlineSongs!!.size-1 && playMode == Consts.PLAY_ORDER) {
                                        0
                                    } else {
                                        PlayServiceHelper.getNextSongPosition(
                                            currentPosition,
                                            playMode,
                                            justOnlineSongs?.size
                                        )
                                    }
                                }
                                LogUtil.e("-------PlayService----onBind currentPosition:$currentPosition------")
                                PlayServiceHelper.saveJustOnlineSong(currentPosition,Constants.ST_DAILY_RECOMMEND,justOnlineSongs)
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
            Bus.post(Consts.SONG_STATUS_CHANGE,Consts.SONG_ERROR)
            true
        }
        return playerBinder
    }

    inner class PlayerBinder : Binder() {
        fun setPlayModel(mode: Int) {
            playMode = mode
        }
        fun singerPlay(singerId:Int,restartTime: Int?=null) {
            onlineSongs = GlobalUtil.execute {
                OnlineSongDatabase.getDatabase().onlineSongDao().findSongBySingerId(singerId).toMutableList()
            }
            val song = SongUtil.getSong()
            if (song != null) {
                currentPosition = song.position
                mediaPlayer.reset()
                if (isNotNullOrEmpty(onlineSongs)) {
                    val currentSongId = onlineSongs!![currentPosition].songId
                    getOnlineSongPlayUrl(song,currentSongId,Consts.ONLINE_SINGER_SONG,restartTime)
                }
            }
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
                        }
                        Consts.LIST_TYPE_HISTORY -> {
                            // todo
                        }
                        Consts.LIST_TYPE_DOWNLOAD -> {
                            downloadSongs = DownloadedUtil.getSongFromFile()
                        }
                        Consts.LIST_TYPE_LOVE -> {
                            loveSongs = GlobalUtil.execute {
                                MaiDatabase.getDatabase().loveSongDao().findAllLoveSongs()
                                    .toMutableList()
                            }
                        }
                        Consts.LIST_TYPE_ONLINE->{
                            when(onlineSubjectType) {
                                Consts.ONLINE_FIRST_LAUNCH->{
                                    launchSongs = GlobalUtil.execute {
                                        OnlineSongDatabase.getDatabase().onlineSongDao().findLaunchSongs().toMutableList()
                                    }
                                }
                                Constants.ST_DAILY_RECOMMEND->{
                                    val startIndex = DataUtil.getTheDayStartIndex(Constants.ST_DAILY_RECOMMEND)
                                    onlineSongs = GlobalUtil.execute {
                                        OnlineSongDatabase.getDatabase().onlineSongDao().findRangeOnlineSongs(startIndex,Constants.PAGE_SIZE_DAILY_RECOMMEND).toMutableList()
                                    }
                                    LogUtil.e("-------PlayService PlayerBinder play onlineSongs:$onlineSongs")
                                }
                                Consts.JUST_ONLINE_SONG->{
                                    justOnlineSongs = GlobalUtil.execute {
                                        OnlineSongDatabase.getDatabase().justOnlineSongDao().findJustOnlineSongs().toMutableList()
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
                                LogUtil.e("-----PlayerService---PlayerBinder ---playUrl:$playUrl")
                                if (playUrl.isNotNullOrEmpty()) {
                                    mediaPlayer.setDataSource(playUrl)
                                    startPlay(restartTime)
                                }
                            }
                        }
                        // 播放在线音乐（只有自动播放下一曲才会执行这里  其他的是走的playOnline）
                        Consts.LIST_TYPE_ONLINE->{
                            when(onlineSubjectType){
                                Consts.ONLINE_FIRST_LAUNCH->{
                                    if (isNotNullOrEmpty(launchSongs)) {
                                        val currentSongId = launchSongs!![currentPosition].songId
                                        getOnlineSongPlayUrl(song,currentSongId,Consts.ONLINE_FIRST_LAUNCH,restartTime)
                                    }
                                }
                                Constants.ST_DAILY_RECOMMEND->{// TODO 每日推荐和专辑推荐应该属于同一个类型 在search_online_song表中
                                    if (isNotNullOrEmpty(onlineSongs)) {
                                        val currentSongId = onlineSongs!![currentPosition].songId
                                        getOnlineSongPlayUrl(song,currentSongId,Constants.ST_DAILY_RECOMMEND,restartTime)
                                    }
                                }
                                Consts.ONLINE_SEARCH->{// 搜索的歌曲
                                    val nextSearchSong = GlobalUtil.execute {
                                        OnlineSongDatabase.getDatabase().onlineSongDao().findRandomSingleSong()
                                    }
                                    val nextSong = PlayServiceHelper.transferOnlineSong(nextSearchSong, Consts.ONLINE_SEARCH)
                                    LogUtil.e("nextSearchSong$nextSearchSong")
                                    if (nextSong!= null) {
                                        val playUrl = PlayServiceHelper.getOnlineSongUrl(nextSong.songId!!)
                                        if (playUrl.isNotNullOrEmpty()) {
                                            nextSong.url = playUrl// 保存在线音乐的url 这样如果暂停后再次进入则会继续播放这首音乐
                                            SongUtil.saveSong(nextSong)
                                            mediaPlayer.setDataSource(playUrl)
                                            startPlay(restartTime)
                                        }
                                    }
                                }
                                Consts.JUST_ONLINE_SONG->{// TODO 每日推荐和专辑推荐应该属于同一个类型 在search_online_song表中
                                    if (isNotNullOrEmpty(justOnlineSongs)) {
                                        val currentSongId = justOnlineSongs!![currentPosition].songId
                                        getOnlineSongPlayUrl(song,currentSongId,Consts.JUST_ONLINE_SONG,restartTime)
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                LogUtil.e("-----PlayerService---PlayerBinder ---play exception:${e.message}")
                "播放异常".toast()
                Bus.post(Consts.SONG_STATUS_CHANGE,Consts.SONG_ERROR)
            }
        }
        private fun getOnlineSongPlayUrl(song:Song,songId:String?,subjectType:Int,restartTime: Int?) {
            if (songId.isNotNullOrEmpty()) {
                val playUrl = PlayServiceHelper.getOnlineSongUrl(songId!!)
                var nextSongId:String?=null
                if (playUrl.isNullOrEmpty()) {
                    when(subjectType) {
                        Consts.ONLINE_FIRST_LAUNCH->{
                            currentPosition = PlayServiceHelper.getNextSongPosition(currentPosition,playMode,launchSongs?.size)
                            PlayServiceHelper.saveCurrentOnlineSong(currentPosition,subjectType,launchSongs)
                            nextSongId = launchSongs!![currentPosition].songId
                        }
                        Constants.ST_DAILY_RECOMMEND->{
                            currentPosition = PlayServiceHelper.getNextSongPosition(currentPosition,playMode,onlineSongs?.size)
                            PlayServiceHelper.saveCurrentOnlineSong(currentPosition,subjectType,onlineSongs)
                            nextSongId = onlineSongs!![currentPosition].songId
                        }
                        Consts.ONLINE_SINGER_SONG->{
                            currentPosition = PlayServiceHelper.getNextSongPosition(currentPosition,playMode,onlineSongs?.size)
                            PlayServiceHelper.saveCurrentOnlineSong(currentPosition,subjectType,onlineSongs)
                            nextSongId = onlineSongs!![currentPosition].songId
                        }
                        Consts.JUST_ONLINE_SONG->{
                            currentPosition = PlayServiceHelper.getNextSongPosition(currentPosition,playMode,justOnlineSongs?.size)
                            PlayServiceHelper.saveJustOnlineSong(currentPosition,subjectType,justOnlineSongs)
                            nextSongId = justOnlineSongs!![currentPosition].songId
                        }
                    }
                    getOnlineSongPlayUrl(song,nextSongId,subjectType,restartTime)
                } else {
                    song.url = playUrl// 保存在线音乐的url 这样如果暂停后再次进入则会继续播放这首音乐
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
                                }
                                Bus.post(Consts.SONG_STATUS_CHANGE, Consts.SONG_CHANGE)
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
                            Consts.ONLINE_FIRST_LAUNCH->{
                                currentPosition = if (currentPosition==launchSongs!!.size-1 && playMode == Consts.PLAY_ORDER) {
                                    0
                                } else {
                                    PlayServiceHelper.getNextSongPosition(
                                        currentPosition,
                                        playMode,
                                        launchSongs?.size
                                    )
                                }
                                LogUtil.e("-------PlayService----onBind currentPosition:$currentPosition------")
                                PlayServiceHelper.saveCurrentOnlineSong(currentPosition,Consts.ONLINE_FIRST_LAUNCH,launchSongs)
                            }
                            Constants.ST_DAILY_RECOMMEND->{
                                currentPosition = if (currentPosition==onlineSongs!!.size-1 && playMode == Consts.PLAY_ORDER) {
                                    0
                                } else {
                                    PlayServiceHelper.getNextSongPosition(
                                        currentPosition,
                                        playMode,
                                        onlineSongs?.size
                                    )
                                }
                                LogUtil.e("-------PlayService----onBind currentPosition:$currentPosition------")
                                PlayServiceHelper.saveCurrentOnlineSong(currentPosition,Constants.ST_DAILY_RECOMMEND,onlineSongs)
                            }
                            Consts.JUST_ONLINE_SONG->{
                                currentPosition = if (currentPosition==justOnlineSongs!!.size-1 && playMode == Consts.PLAY_ORDER) {
                                    0
                                } else {
                                    PlayServiceHelper.getNextSongPosition(
                                        currentPosition,
                                        playMode,
                                        justOnlineSongs?.size
                                    )
                                }
                                LogUtil.e("-------PlayService----onBind currentPosition:$currentPosition------")
                                PlayServiceHelper.saveJustOnlineSong(currentPosition,Consts.JUST_ONLINE_SONG,justOnlineSongs)
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
                            Consts.ONLINE_FIRST_LAUNCH->{
                                currentPosition = if (currentPosition==0 && playMode == Consts.PLAY_ORDER) {
                                    launchSongs!!.size-1
                                } else {
                                    PlayServiceHelper.getLastSongPosition(
                                        currentPosition,
                                        playMode,
                                        launchSongs?.size
                                    )
                                }
                                LogUtil.e("-------PlayService----onBind currentPosition:$currentPosition------")
                                PlayServiceHelper.saveCurrentOnlineSong(currentPosition,Consts.ONLINE_FIRST_LAUNCH,launchSongs)
                            }
                            Constants.ST_DAILY_RECOMMEND->{
                                currentPosition = if (currentPosition==0 && playMode == Consts.PLAY_ORDER) {
                                    onlineSongs!!.size-1
                                } else {
                                    PlayServiceHelper.getLastSongPosition(
                                        currentPosition,
                                        playMode,
                                        onlineSongs?.size
                                    )
                                }
                                LogUtil.e("-------PlayService----onBind currentPosition:$currentPosition------")
                                PlayServiceHelper.saveCurrentOnlineSong(currentPosition,Consts.ONLINE_FIRST_LAUNCH,onlineSongs)
                            }
                            Consts.JUST_ONLINE_SONG->{
                                currentPosition = if (currentPosition==0 && playMode == Consts.PLAY_ORDER) {
                                    justOnlineSongs!!.size-1
                                } else {
                                    PlayServiceHelper.getLastSongPosition(
                                        currentPosition,
                                        playMode,
                                        justOnlineSongs?.size
                                    )
                                }
                                LogUtil.e("-------PlayService----onBind currentPosition:$currentPosition------")
                                PlayServiceHelper.saveJustOnlineSong(currentPosition,Consts.JUST_ONLINE_SONG,justOnlineSongs)
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

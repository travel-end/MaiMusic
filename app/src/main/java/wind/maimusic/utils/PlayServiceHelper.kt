package wind.maimusic.utils

import wind.maimusic.model.*
import wind.maimusic.model.download.Downloaded
import wind.maimusic.net.RetrofitClient
import wind.maimusic.room.database.MaiDatabase
import wind.widget.cost.Consts
import wind.widget.model.Song

/**
 * @By Journey 2020/10/28
 * @Description
 */
object PlayServiceHelper {
    fun saveLocalSong(currentPosition: Int) {
        val localSongs = GlobalUtil.execute {
            MaiDatabase.getDatabase().localSongDao().findAllLocalSong()
        }
        if (localSongs.isNotEmpty()) {
            val localSong = localSongs[currentPosition]
            val song = Song().apply {
                position = currentPosition
                songId = localSong.songId
                songName = localSong.name
                singer = localSong.singer
                url = localSong.url
                isOnline = false
                duration = localSong.duration?.toInt() ?: 0
                qqId = localSong.qqId
                listType = Consts.LIST_TYPE_LOCAL
            }
            SongUtil.saveSong(song)
        }
    }

    fun saveHistorySong(currentPosition: Int, historySongs: MutableList<HistorySong>?) {
        if (historySongs != null) {
            val historySong = historySongs[currentPosition]
            val song = Song().apply {
                position = currentPosition
                songId = historySong.songId
                qqId = historySong.qqId
                songName = historySong.name
                singer = historySong.singer
                url = historySong.url
                imgUrl = historySong.pic
                listType = Consts.LIST_TYPE_HISTORY
                isOnline = historySong.isOnline
                duration = historySong.duration ?: 0
                mediaId = historySong.mediaId
                isDownload = historySong.isDownload
            }
            SongUtil.saveSong(song)
        }
    }

    fun saveDownloadSong(currentPosition: Int, downloadSongs: MutableList<Downloaded>?) {
        if (downloadSongs != null) {
            val downloadSong = downloadSongs[currentPosition]
            val song = Song().apply {
                position = currentPosition
                songId = downloadSong.songId
                songName = downloadSong.name
                singer = downloadSong.singer
                url = downloadSong.url
                imgUrl = downloadSong.pic
                listType = Consts.LIST_TYPE_DOWNLOAD
                isOnline = false
                duration = downloadSong.duration?.toInt() ?: 0
                mediaId = downloadSong.mediaId
                isDownload = true
                albumName = downloadSong.albumName
            }
            SongUtil.saveSong(song)
        }
    }

    /**
     * 根据当前的位置  保存当前歌曲（根据播放模式切歌）
     */
    fun saveCurrentOnlineSong(currentPosition: Int, subjectType:Int,launchSongs: MutableList<OnlineSong>?) {
        if (launchSongs != null) {
            val launchSong = launchSongs[currentPosition]
            val song = Song().apply {
                position = currentPosition
                songId = launchSong.songId
                songName = launchSong.name
                singer = launchSong.singer
                imgUrl = launchSong.imgUrl
                listType = Consts.LIST_TYPE_ONLINE
                onlineSubjectType = subjectType
                isOnline = launchSong.isOnline?:false
                duration = launchSong.duration ?: 0
                mediaId = launchSong.mediaId
                isDownload = launchSong.isDownload?:false
                albumName = launchSong.albumName
            }
            SongUtil.saveSong(song)
        }
    }

    fun saveSingerSong(currentPosition: Int,singerSongs: MutableList<OnlineSong>?) {
        if (singerSongs != null) {
            val singerSong = singerSongs[currentPosition]
            val song = Song().apply {
                position = currentPosition
                songId = singerSong.songId
                songName = singerSong.name
                singer = singerSong.singer
                imgUrl = singerSong.imgUrl
                listType = Consts.ONLINE_SINGER_SONG
                isOnline = singerSong.isOnline?:false
                duration = singerSong.duration ?: 0
                mediaId = singerSong.mediaId
                isDownload = singerSong.isDownload?:false
                albumName = singerSong.albumName
            }
            SongUtil.saveSong(song)
        }
    }


    fun saveJustOnlineSong(currentPosition: Int, subjectType:Int,justOnlineSongs: MutableList<JustOnlineSong>?) {
        if (justOnlineSongs != null) {
            val justOnlineSong = justOnlineSongs[currentPosition]
            val song = Song().apply {
                position = currentPosition
                songId = justOnlineSong.songId
                songName = justOnlineSong.name
                singer = justOnlineSong.singer
                imgUrl = justOnlineSong.pic
                listType = Consts.LIST_TYPE_ONLINE
                onlineSubjectType = subjectType
                isOnline = justOnlineSong.isOnline?:false
                duration = if (justOnlineSong.duration ==null) 0 else justOnlineSong.duration!!.toInt()
                mediaId = justOnlineSong.mediaId
                isDownload = justOnlineSong.isDownloaded?:false
                albumName = justOnlineSong.albumName
            }
            SongUtil.saveSong(song)
        }
    }

    fun transferOnlineSong(onlineSong:OnlineSong?,subjectType:Int):Song? {
        if (onlineSong!= null) {
            return Song().apply {
                songId = onlineSong.songId
                singer = onlineSong.singer
                songName = onlineSong.name
                imgUrl =onlineSong.imgUrl
                duration = onlineSong.duration?:0
                isOnline = true
                listType =Consts.LIST_TYPE_ONLINE
                onlineSubjectType = subjectType
                mediaId = onlineSong.mediaId
                isDownload =onlineSong.isDownload
            }
        }
        return null
    }

    fun saveLoveSong(currentPosition: Int) {
        var loveSongs = GlobalUtil.execute {
            MaiDatabase.getDatabase().loveSongDao().findAllLoveSongs().toMutableList()
        }
        if (loveSongs.isNotEmpty()) {
            loveSongs =
                orderLoveList(loveSongs)
            val loveSong = loveSongs[currentPosition]
            val song = Song().apply {
                position = currentPosition
                songId = loveSong.songId
                songName = loveSong.name
                qqId = loveSong.qqId
                singer = loveSong.singer
                url = loveSong.url
                imgUrl = loveSong.pic
                listType = Consts.LIST_TYPE_LOVE
                mediaId = loveSong.mediaId
                isOnline = loveSong.isOnline ?: false
                isDownload = loveSong.isDownload ?: false
                duration = loveSong.duration ?: 0
            }
            SongUtil.saveSong(song)
        }
    }

    fun save2History() {
        val song = SongUtil.getSong()
        if (song != null) {
            val historySong = HistorySong().apply {
                songId = song.songId
                qqId = song.qqId
                name = song.songName
                singer = song.singer
                url = song.url
                pic = song.imgUrl
                isOnline = song.isOnline
                isDownload = song.isDownload
                duration = song.duration
                mediaId = song.mediaId
            }
            val historyDao = MaiDatabase.getDatabase().historySongDao()
            GlobalUtil.async {
                historyDao.addHistorySong(historySong)
                val historySongs = historyDao.findAllHistorySong()
                if (historySongs.size > Consts.HISTORY_MAX_SIZE) {
//                    historyDao.deleteFirst()
                }
            }
        }
    }

    fun getNextSongPosition(current: Int, playMode: Int, len: Int?): Int {
        return if (len != null && len != 0) {
            when (playMode) {
                Consts.PLAY_ORDER -> {
                    (current + 1) % len
                }
                Consts.PLAY_RANDOM -> {
                    (current + (Math.random() * len).toInt()) % len
                }
                else -> {
                    current
                }
            }
        } else {
            0
        }
    }

    fun getLastSongPosition(current: Int, playMode: Int, len: Int?): Int {
        return if (len != null && len != 0) {
            when (playMode) {
                Consts.PLAY_ORDER -> {
                    if (current - 1 == -1) len - 1 else current - 1
                }
                Consts.PLAY_RANDOM -> {
                    (current + (Math.random() * len).toInt()) % len
                }
                else -> current
            }
        } else {
            0
        }
    }

    fun orderLoveList(tempList: MutableList<LoveSong>): MutableList<LoveSong> {
        val loveSongList = mutableListOf<LoveSong>()
        loveSongList.clear()
        for (i in tempList.indices.reversed()) {
            loveSongList.add(tempList[i])
        }
        return loveSongList
    }

    @Throws(Exception::class)
    fun getOnlineSongUrl(songId: String): String? {
        val songUrl = "${Consts.SONG_URL_DATA_LEFT}$songId${Consts.SONG_URL_DATA_RIGHT}"
        var playUrl: String? = null
        GlobalUtil.execute {
            val result = RetrofitClient.instance.songUrlApiService.getSongUrl(songUrl)
            if (result.code == 0) {
                val sipList = result.req_0?.data?.sip
                var sip = ""
                if (sipList != null) {
                    if (sipList.isNotEmpty()) {
                        sip = sipList[0]
                    }
                }
                val purlList = result.req_0?.data?.midurlinfo
                var pUrl: String? = ""
                if (purlList != null) {
                    if (purlList.isNotEmpty()) {
                        pUrl = purlList[0].purl
                    }
                }
                playUrl = "$sip$pUrl"
            }
        }
        return playUrl
    }
}
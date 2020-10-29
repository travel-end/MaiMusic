package wind.maimusic.service

import wind.maimusic.model.*
import wind.maimusic.room.MaiDatabase
import wind.maimusic.utils.Bus
import wind.maimusic.utils.GlobalUtil
import wind.maimusic.utils.SongUtil
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
                listType = Consts.LIST_TYPE_DOWNLOAD
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

    fun saveLoveSong(currentPosition: Int) {
        var loveSongs = GlobalUtil.execute {
            MaiDatabase.getDatabase().loveSongDao().findAllLoveSong().toMutableList()
        }
        if (loveSongs.isNotEmpty()) {
            loveSongs = orderLoveList(loveSongs)
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
            val saveResult = GlobalUtil.async {
                MaiDatabase.getDatabase().historySongDao().addHistorySong(historySong)
            }
            if (saveResult != null && saveResult != 0L) {
                Bus.post(Consts.EVENT_LIST_TYPE, Consts.LIST_TYPE_HISTORY)
                // todo 如果最近播放的曲目>100 将第一个删除
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
}
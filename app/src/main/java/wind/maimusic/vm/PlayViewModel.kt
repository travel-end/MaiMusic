package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wind.maimusic.R
import wind.maimusic.base.BaseViewModel
import wind.maimusic.model.LoveSong
import wind.maimusic.model.core.ListBean
import wind.maimusic.model.core.OnlineSongLrc
import wind.maimusic.room.MaiDatabase
import wind.maimusic.utils.SongUtil
import wind.maimusic.utils.SpUtil
import wind.maimusic.utils.getStringRes
import wind.maimusic.utils.isNotNullOrEmpty
import wind.widget.cost.Consts
import wind.widget.model.Song

/**
 * @By Journey 2020/10/29
 * @Description
 */
class PlayViewModel : BaseViewModel() {
    val handleLoveSong:MutableLiveData<Int> = MutableLiveData()
    val onlineLyric:MutableLiveData<OnlineSongLrc?> = MutableLiveData()
    val localCoverImg:MutableLiveData<String> = MutableLiveData()
    val matchSongError:MutableLiveData<String?> = MutableLiveData()
    val matchSongRight:MutableLiveData<String> = MutableLiveData()
    val matchSongRightAgain:MutableLiveData<String> = MutableLiveData()
    companion object {
        const val is_love = 0
        const val add_to_love =1
        const val delete_from_love = 2
    }

    /*设置播放模式*/
    fun setPlayMode(mode: Int) {
        SpUtil.saveValue(Consts.KEY_PLAY_MODE, mode)
    }

    /*播放顺序模式*/
    val pm get() = SpUtil.getInt(Consts.KEY_PLAY_MODE, Consts.PLAY_ORDER)

    /* 查询是否为喜欢的音乐*/
    fun findIsLoveSong(songId: String?) {
        if (songId.isNotNullOrEmpty()) {
            viewModelScope.launch {
                val result = withContext(Dispatchers.IO) {
                    MaiDatabase.getDatabase().loveSongDao().findLoveSongBySongId(songId!!)
                }
                if (result.isNotEmpty()) {
                    handleLoveSong.value = is_love
                }
            }
        }
    }
    /* 添加至我的喜欢*/
    fun add2LoveSong(song:Song?) {
        if (song != null) {
            val loveSong = LoveSong(
                songId = song.songId,
                mediaId = song.mediaId,
                qqId = song.qqId,
                name = song.songName,
                singer = song.singer,
                url = song.url,
                pic = song.imgUrl,
                duration = song.duration,
                isOnline = song.isOnline,
                isDownload = song.isDownload
            )
            viewModelScope.launch {
                val result = MaiDatabase.getDatabase().loveSongDao().addToLoveSong(loveSong)
                if (result != 0L) {
                    handleLoveSong.value = add_to_love
                }
            }
        }
    }
    /*删除我的喜欢*/
    fun deleteFromLove(songId: String?) {
        if (songId != null) {
            viewModelScope.launch {
                val result = MaiDatabase.getDatabase().loveSongDao().deleteSongBySongId(songId)
                if (result != 0) {
                    handleLoveSong.value = delete_from_love
                }
            }
        }
    }

    /* 获取在线歌词*/
    fun getOnlineLyric(songId:String,songType:Int,songName:String) {
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    apiService.getOnlineSongLrc(songId)
                }
            }.onSuccess {
                // 本地音乐则保存歌词
                if (it != null) {
                    if (it.code == 0) {
                        onlineLyric.value = it
                        if (songType == Consts.SONG_LOCAL) {
                            SongUtil.saveLrcText(it.lyric,songName)
                        }
                    } else {
                        onlineLyric.value = null
                    }
                }
            }.onFailure {
                onlineLyric.value = null
            }
        }
    }

    /* 获取非网络音乐的封面图片和歌词*/
    fun getLocalCoverImgUrlAndLrc(singer:String,songName: String,duration:Int) {
        viewModelScope.launch {
            val singerImg = withContext(Dispatchers.IO) {
                singerApiService.getLocalSingerImg(singer)
            }
            singerImg.result?.artists?.let {
                localCoverImg.value = it[0].img1v1Url
            }
            // 根据songName获取所有歌曲
            val searchSongs = withContext(Dispatchers.IO) {
                apiService.search(songName,1)
            }
            // 根据当前非网络音乐的时长和搜索到的所有音乐的时长来判断是否匹配到了该歌曲
            if (searchSongs.code == 0) {
                matchSongByDuration(searchSongs.data?.song?.list,duration)
            } else {
                matchSongError.value = null
            }
        }
    }

    /* 再次匹配当前非网络音乐的网络音乐歌词*/
    fun matchLocalSongNetLrc(songName: String?,duration:Int) {
        if (songName.isNotNullOrEmpty()) {
            viewModelScope.launch {
                val searchResult = withContext(Dispatchers.IO) {
                    apiService.search(songName!!,1)
                }
                if (searchResult.code == 0) {
                    matchSong(searchResult.data?.song?.list,duration)
                } else {
                    matchSongError.value = null
                }
            }
        }
    }


    private fun matchSong(listBeans: List<ListBean>?, duration: Int) {
        var isFind: Boolean = false
        if (!listBeans.isNullOrEmpty()) {
            for (bean in listBeans) {
                if (!isFind) {
                    if (duration == bean.interval) {
                        isFind = true
                        matchSongRightAgain.value = bean.songmid
                    }
                }
            }
        }
        // 如果找不到歌曲id就传输找不到歌曲的消息
        if (!isFind) {
            matchSongError.value = R.string.lrc_empty.getStringRes()
        }
    }

    /* 根据当前非网络音乐的时长和搜索到的所有音乐的时长来判断是否匹配到了该歌曲*/
    private fun matchSongByDuration(list:List<ListBean>?,duration:Int) {
        var isFind = false
        if (isNotNullOrEmpty(list)) {
            for (bean in list!!) {
                if (!isFind) {
                    if (duration == bean.interval) {
                        isFind = true
                        matchSongRight.value = bean.songmid
                    }
                }
            }
        }
        if (!isFind) {
            // 没有匹配到本地音乐相同的网络音乐  即没有歌词~~
            matchSongError.value = R.string.lrc_empty.getStringRes()
        }
    }

    /* 更新本地音乐的封面图片*/
    fun updateLocalSongCoverBySongId(pic:String,songId:String) {
        request {
            MaiDatabase.getDatabase().localSongDao().updateLocalSongCoverBySongId(pic,songId)
        }
    }
}
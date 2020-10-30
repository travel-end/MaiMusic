package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wind.maimusic.R
import wind.maimusic.base.BaseViewModel
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
    val isLoveSong:MutableLiveData<Boolean> = MutableLiveData()
    val onlineLyric:MutableLiveData<OnlineSongLrc?> = MutableLiveData()
    val localCoverImg:MutableLiveData<String> = MutableLiveData()

    val matchSongError:MutableLiveData<String?> = MutableLiveData()
    val matchSongRight:MutableLiveData<String> = MutableLiveData()

    val matchSongRightAgain:MutableLiveData<String> = MutableLiveData()

    // 非网络音乐匹配到的网络音乐songId
    val localNetSongId: MutableLiveData<String> = MutableLiveData()

    fun getSingerName(song: Song): String? {
        val singer = song.singer
        if (singer != null) {
            return if (singer.contains("/")) {
                val s = singer.split("/")
                s[0].trim()
            } else {
                singer.trim()
            }
        }
        return null
    }


    fun setPlayMode(mode: Int) {
        SpUtil.saveValue(Consts.KEY_PLAY_MODE, mode)
    }

    fun getPlayMode(): Int {
        return SpUtil.getInt(Consts.KEY_PLAY_MODE, Consts.PLAY_ORDER)
    }

    fun findIsLoveSong() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                MaiDatabase.getDatabase().loveSongDao().findAllLoveSongs()
            }
            isLoveSong.value = result.isNotEmpty()
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
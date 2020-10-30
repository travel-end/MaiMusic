package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wind.maimusic.base.BaseViewModel
import wind.maimusic.model.OnlineLyric
import wind.maimusic.room.MaiDatabase
import wind.maimusic.utils.SpUtil
import wind.widget.cost.Consts
import wind.widget.model.Song

/**
 * @By Journey 2020/10/29
 * @Description
 */
class PlayViewModel : BaseViewModel() {
    val isLoveSong:MutableLiveData<Boolean> = MutableLiveData()
    val onlineLyric:MutableLiveData<OnlineLyric> = MutableLiveData()
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
            withContext(Dispatchers.IO) {
                runCatching {
                    apiService.getOnlineSongLrc(songId)
                }.onSuccess {
//                    onlineLyric.value = it
                }.onFailure {

                }
            }
        }
    }
}
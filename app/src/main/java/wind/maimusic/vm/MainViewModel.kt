package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wind.maimusic.base.BaseViewModel
import wind.maimusic.base.state.State
import wind.maimusic.base.state.StateType
import wind.maimusic.model.OnlineSong
import wind.maimusic.model.firstmeet.FirstMeetSong
import wind.maimusic.room.database.OnlineSongDatabase
import wind.maimusic.utils.LogUtil
import wind.widget.cost.Consts
import wind.widget.model.Song

class MainViewModel:BaseViewModel() {
    val launchSong :MutableLiveData<OnlineSong> = MutableLiveData()
    val songPlayUrlResult : MutableLiveData<Map<String, Any>> = MutableLiveData()
    fun findLaunchSong() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                OnlineSongDatabase.getDatabase().onlineSongDao().findLaunchSong()
            }
            LogUtil.e("-----MainViewModel findLaunchSong result:$result")
            launchSong.value = result
        }
    }

    fun getSongPlayUrl(song: Song) {
        loadStatus.value = State(StateType.LOADING_NORMAL)
        val songUrl = "${Consts.SONG_URL_DATA_LEFT}${song.songId}${Consts.SONG_URL_DATA_RIGHT}"
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    songUrlApiService.getSongUrl(songUrl)
                }
            }.onSuccess {
                if (it.code == 0) {
                    val sipList = it.req_0?.data?.sip
                    var sip=""
                    if (sipList != null) {
                        if (sipList.isNotEmpty()) {
                            sip = sipList[0]
                        }
                    }
                    val purlList = it.req_0?.data?.midurlinfo
                    var pUrl:String?=""
                    if (purlList != null) {
                        if (purlList.isNotEmpty()) {
                            pUrl=purlList[0].purl
                        }
                    }
                    if (pUrl.isNullOrEmpty()) {
//                        loadStatus.value = State(StateType.SHOW_TOAST,"暂时没有播放权限~")
                    } else {
                        val pair="song" to song
                        val pair2 = "url" to "$sip$pUrl"
                        val map = mapOf(pair,pair2)
                        LogUtil.e("--->play url:${pair2.second}")
                        songPlayUrlResult.value = map
                    }
                } else {
//                    loadStatus.value = State(StateType.SHOW_TOAST,"${it.code} :获取不到歌曲播放地址")
                }
            }.onFailure {
                handleException(it, State(StateType.SHOW_TOAST,msg = it.message?:"未知错误"))
            }
            loadStatus.value = State(StateType.DISMISSING_NORMAL)
        }
    }
}
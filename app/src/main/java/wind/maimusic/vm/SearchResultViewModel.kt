package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wind.maimusic.R
import wind.maimusic.base.BaseViewModel
import wind.maimusic.base.state.State
import wind.maimusic.base.state.StateType
import wind.maimusic.model.core.SearchSong
import wind.maimusic.utils.LogUtil
import wind.maimusic.utils.getStringRes
import wind.widget.cost.Consts
import wind.widget.model.Song

class SearchResultViewModel:BaseViewModel() {
    val searchResult: MutableLiveData<SearchSong> = MutableLiveData()
    val songPlayUrlResult : MutableLiveData<Map<String, Any>?> = MutableLiveData()
    /**
     * 关键词搜索
     */
    fun searchSong(searchContent: String, page: Int) {
        loadStatus.value = State(StateType.LOADING_SONG)
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    apiService.search(searchContent, page)
                }
            }.onSuccess {
//                val songList = it.data?.song?.list
//                Log.e("JG", "关键词搜索结果：${songList}")
//                Log.e("JG", "关键词搜索结果第一条：${songList!![0]}")
                searchResult.value = it
            }.onFailure {
                handleException(it, State(StateType.ERROR,msg = R.string.empty.getStringRes()))// TODO: 2020/10/29 处理搜索结果空
            }
            loadStatus.value = State(StateType.DISMISSING_SONG)
        }
    }

    fun getSongPlayUrl(song: Song) {
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
                        songPlayUrlResult.value = null
                        loadStatus.value = State(StateType.SHOW_TOAST,"暂时没有播放权限~")
                    } else {
                        val pair="song" to song
                        val pair2 = "url" to "$sip$pUrl"
                        val map = mapOf(pair,pair2)
                        LogUtil.e("--->play url:${pair2.second}")
                        songPlayUrlResult.value = map
                    }
                } else {
                    songPlayUrlResult.value = null
                    loadStatus.value = State(StateType.SHOW_TOAST,"${it.code} :获取不到歌曲播放地址")
                }
            }.onFailure {
                handleException(it, State(StateType.SHOW_TOAST,msg = it.message?:"未知错误"))
                songPlayUrlResult.value = null
            }
        }
    }
}

class SearchAlbumResultViewModel:BaseViewModel()  {

}
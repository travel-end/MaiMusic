package wind.maimusic.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wind.maimusic.R
import wind.maimusic.base.BaseViewModel
import wind.maimusic.base.state.State
import wind.maimusic.base.state.StateType
import wind.maimusic.model.core.Album
import wind.maimusic.model.core.SearchSong
import wind.maimusic.model.searchhot.HistoryTag
import wind.maimusic.room.database.MaiDatabase
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

    fun addOneSearchTag(searchText:String) {
        val searchDao = MaiDatabase.getDatabase().searchSongDao()
        request {
            val existOfTag = searchDao.findSearchTagByName(searchText).isEmpty()
            if (existOfTag) {
                val tag = HistoryTag(name = searchText)
                searchDao.addOneSearchTag(tag)
            }
        }
    }
}

class SearchAlbumResultViewModel:BaseViewModel()  {
    val searchAlbumResult: MutableLiveData<Album> = MutableLiveData()
    fun searchAlbum(keyWord:String,page: Int) {
        loadStatus.value = State(StateType.LOADING_SONG)
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    apiService.searchAlbum(keyWord, page)
                }
            }.onSuccess {
                val albumList = it.data?.album?.list
                Log.e("JG", "关键词搜索专辑结果：albumList $albumList")
                searchAlbumResult.value = it
            }.onFailure {
                handleException(it, State(StateType.ERROR,msg = R.string.empty.getStringRes()))// TODO: 2020/10/29 处理搜索结果空
            }
            loadStatus.value = State(StateType.DISMISSING_SONG)
        }
    }
}
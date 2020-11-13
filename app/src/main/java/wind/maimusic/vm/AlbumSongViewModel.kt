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
import wind.maimusic.model.core.AlbumListBean
import wind.maimusic.room.database.MaiDatabase
import wind.maimusic.room.database.OnlineSongDatabase
import wind.maimusic.utils.getStringRes
import wind.maimusic.utils.isNotNullOrEmpty

class AlbumSongViewModel:BaseViewModel() {
    val albumSongs: MutableLiveData<List<AlbumListBean>> = MutableLiveData()
    fun getAlbumSongs(albumId:String) {
            loadStatus.value = State(StateType.LOADING_SONG)
            viewModelScope.launch {
                runCatching {
                    withContext(Dispatchers.IO) {
                        apiService.getAlbumSong(albumId)
                    }
                }.onSuccess {
                    if (it.code ==0) {
                        albumSongs.value = it.data?.list
                        saveAlbumSongs(it.data?.list)
                    }
                }.onFailure {
                    handleException(it, State(StateType.ERROR,msg = R.string.empty.getStringRes()))// TODO: 2020/10/29 处理搜索结果空
                }
                loadStatus.value = State(StateType.DISMISSING_SONG)
            }
    }

    private fun saveAlbumSongs(list:List<AlbumListBean>?) {
        if (isNotNullOrEmpty(list)) {
        }
    }

    fun findIsDownloaded(songId:String):Boolean {
        var hasDownloaded:Boolean = false
        val dao = MaiDatabase.getDatabase().downloadSongDao()
        viewModelScope.launch {
            hasDownloaded = dao.findIdBySongId(songId).isNotEmpty()
        }
        return hasDownloaded
    }
}
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
import wind.maimusic.model.LocalSong
import wind.maimusic.room.MaiDatabase
import wind.maimusic.utils.PhoneUtil
import wind.maimusic.utils.getStringRes
import wind.maimusic.utils.isNotNullOrEmpty

class LocalSongViewModel:BaseViewModel() {
    val scanMusic:MutableLiveData<MutableList<LocalSong>> = MutableLiveData()
    val dbLocalSong:MutableLiveData<MutableList<LocalSong>> = MutableLiveData()

    // TODO: 2020/11/1  添加一个扫描的动画
    /* 扫描本地音乐*/
    fun scanLocalMusic() {
        val rawSongs = PhoneUtil.scanLocalSong()
        if (isNotNullOrEmpty(rawSongs)) {
            scanMusic.value = rawSongs?.toMutableList()
            request {
                MaiDatabase.getDatabase().localSongDao().addLocalSongs(rawSongs!!)
            }
        } else {
            loadStatus.value = State(StateType.EMPTY,msg = R.string.no_local_music.getStringRes())
        }
    }

    /*从数据库查询本地音乐*/
    fun getLocalSong() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                MaiDatabase.getDatabase().localSongDao().findAllLocalSong()
            }
            if (isNotNullOrEmpty(result)) {
                dbLocalSong.value = result.toMutableList()
            } else {
                loadStatus.value = State(StateType.EMPTY,msg = R.string.no_local_music.getStringRes())
            }
        }
    }
}
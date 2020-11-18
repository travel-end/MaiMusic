package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wind.maimusic.base.BaseViewModel
import wind.maimusic.model.OnlineSong
import wind.maimusic.room.database.OnlineSongDatabase

/**
 * @By Journey 2020/11/18
 * @Description
 */
class SingerSongListViewModel:BaseViewModel() {
    val onlineSongs : MutableLiveData<MutableList<OnlineSong>> = MutableLiveData()
    fun findSingerSongs(singerId:Int) {
        if (singerId != -1) {
            val dao = OnlineSongDatabase.getDatabase().onlineSongDao()
            viewModelScope.launch {
                val result = withContext(Dispatchers.IO) {
                    dao.findSongBySingerId(singerId)
                }
                onlineSongs.value = result.toMutableList()
            }
        }

    }
}
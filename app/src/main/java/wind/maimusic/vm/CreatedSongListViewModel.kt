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
 * @By Journey 2020/11/20
 * @Description
 */
class CreatedSongListViewModel:BaseViewModel() {
    val createSongs:MutableLiveData<List<OnlineSong>> = MutableLiveData()

    fun getSongListById(listId:Int) {
        val dao = OnlineSongDatabase.getDatabase().onlineSongDao()
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                dao.findOnlineSongByMainType(listId)
            }
            createSongs.value = result
        }
    }
}
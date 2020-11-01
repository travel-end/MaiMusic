package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wind.maimusic.base.BaseViewModel
import wind.maimusic.model.HistorySong
import wind.maimusic.room.MaiDatabase

class RecentListenViewModel:BaseViewModel() {
    val recentListenSongs :MutableLiveData<List<HistorySong>> = MutableLiveData()
    val deleteAllRecentSongs:MutableLiveData<Boolean> = MutableLiveData()
    fun findAllRecentListenSongs() {
        viewModelScope.launch {
            recentListenSongs.value =  withContext(Dispatchers.IO) {
                MaiDatabase.getDatabase().historySongDao().findAllHistorySong()
            }
        }
    }
    fun deleteAllRecentSongs() {
        viewModelScope.launch {
            val result =  withContext(Dispatchers.IO) {
                MaiDatabase.getDatabase().historySongDao().deleteAllHistorySong()
            }
            deleteAllRecentSongs.value = result!=0
        }
    }
}
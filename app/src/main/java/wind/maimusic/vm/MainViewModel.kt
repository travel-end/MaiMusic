package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wind.maimusic.base.BaseViewModel
import wind.maimusic.model.firstmeet.FirstMeetSong
import wind.maimusic.room.MaiDatabase

class MainViewModel:BaseViewModel() {
    val firstMeetSongs :MutableLiveData<List<FirstMeetSong>> = MutableLiveData()
    fun findFirstMeetSongs() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                MaiDatabase.getDatabase().firstMeetSongDao().findAllFirstSong()
            }
            firstMeetSongs.value = result
        }
    }

}
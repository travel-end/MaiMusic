package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wind.maimusic.base.BaseViewModel
import wind.maimusic.model.LoveSong
import wind.maimusic.room.MaiDatabase

class LoveSongViewModel:BaseViewModel() {

    val lovedSongs:MutableLiveData<MutableList<LoveSong>> = MutableLiveData()
    fun findAllLovedSongs() {
        viewModelScope.launch {
            lovedSongs.value = withContext(Dispatchers.IO) {
                MaiDatabase.getDatabase().loveSongDao().findAllLoveSongs().toMutableList()
            }
        }
    }
}
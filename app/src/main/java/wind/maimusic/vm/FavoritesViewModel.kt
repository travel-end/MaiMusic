package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wind.maimusic.base.BaseViewModel
import wind.maimusic.room.database.MaiDatabase

class FavoritesViewModel:BaseViewModel() {
    val songNums:MutableLiveData<Array<Int?>> = MutableLiveData()
    fun getSongNums() {
        val array = arrayOfNulls<Int>(3)
        viewModelScope.launch {
            val lovedSong = MaiDatabase.getDatabase().loveSongDao().findAllLoveSongs().size
            array[0] = lovedSong
//            val poetrys = MaiDatabase.getDatabase().loveSongDao().findAllLoveSongs().size
//            array[0] = lovedSong
            songNums.value =array
        }
    }
}
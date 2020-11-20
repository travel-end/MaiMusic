package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wind.maimusic.base.BaseViewModel
import wind.maimusic.model.listensong.SongListCover
import wind.maimusic.room.database.MaiDatabase
import wind.maimusic.room.database.OnlineSongDatabase

class FavoritesViewModel:BaseViewModel() {
    val songNums:MutableLiveData<Array<Int?>> = MutableLiveData()
    val addCreatedSongList:MutableLiveData<Boolean> = MutableLiveData()
    val deleteCreatedSongList:MutableLiveData<Boolean> = MutableLiveData()
    val addAndFindThisSongList:MutableLiveData<SongListCover> = MutableLiveData()

    val allCreatedSongList :MutableLiveData<List<SongListCover>> = MutableLiveData()

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

//    fun addCreatedSongListItem(songListItem:SongListItem) {
//        viewModelScope.launch {
//            val result  = OnlineSongDatabase.getDatabase().createdSongListDao().addCreatedSongList(songListItem)
//            if (result != 0L) {
//                addCreatedSongList.value = true
//            }
//        }
//    }

    fun addAndFindThisSongList(cover:SongListCover) {
        viewModelScope.launch {
            val result  = OnlineSongDatabase.getDatabase().songListCoverDao().addSongCover(cover)
            if (result != 0L) {
                val songList = OnlineSongDatabase.getDatabase().songListCoverDao().findSongCoverById(result.toInt())
                addAndFindThisSongList.value =songList
            }
        }
    }

    fun findAllCreatedSongList() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                OnlineSongDatabase.getDatabase().songListCoverDao().findAllUserCreatedSongList()
            }
            allCreatedSongList.value =result.reversed()
        }
    }
    fun deleteCreatedSongList(item: SongListCover) {
        viewModelScope.launch {
            val result = OnlineSongDatabase.getDatabase().songListCoverDao().deleteUserCreatedSongList(item)
            if (result != 0) {
                deleteCreatedSongList.value = true
            }
        }
    }

}
package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import wind.maimusic.base.BaseViewModel
import wind.maimusic.model.listensong.ListenSong
import wind.maimusic.utils.AssetsUtil

class ListenSongViewModel :BaseViewModel() {

    val listenSong :MutableLiveData<ListenSong?> = MutableLiveData()
    fun getListenData() {
        listenSong.value = AssetsUtil.loadListenSongData()
    }


}
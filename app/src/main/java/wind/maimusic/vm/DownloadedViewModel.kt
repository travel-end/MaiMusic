package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import wind.maimusic.base.BaseViewModel
import wind.maimusic.base.state.State
import wind.maimusic.base.state.StateType
import wind.maimusic.model.download.Downloaded
import wind.maimusic.utils.DownloadedUtil
import wind.maimusic.utils.isNotNullOrEmpty

class DownloadedViewModel:BaseViewModel() {
    val downloadedSong :MutableLiveData<MutableList<Downloaded>> = MutableLiveData()

    fun getDownloadedSongs() {
        val songs = DownloadedUtil.getSongFromFile()
        if (isNotNullOrEmpty(songs)) {
            downloadedSong.value = songs
        } else {
            loadStatus.value = State(StateType.EMPTY)
        }
    }
}
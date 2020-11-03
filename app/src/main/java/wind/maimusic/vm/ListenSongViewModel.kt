package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wind.maimusic.R
import wind.maimusic.base.BaseViewModel
import wind.maimusic.model.listensong.*
import wind.maimusic.model.title.ListenSongListTitle
import wind.maimusic.model.title.PoetrySongTitle
import wind.maimusic.model.title.SingleSongTitle
import wind.maimusic.room.database.OnlineSongDatabase
import wind.maimusic.utils.getStringRes

class ListenSongViewModel :BaseViewModel() {

    val listData:MutableLiveData<MutableList<Any>> = MutableLiveData()
    fun getListenData() {
            val data = mutableListOf<Any>()
            val dbDao = OnlineSongDatabase.getDatabase()
            viewModelScope.launch {
                val randomBanner = dbDao.listenBannerDao().getRandomBanners()
                val banner = Banner(randomBanner)
                data.add(banner)
                data.add(TabMenu("", R.string.daily_recommend.getStringRes(),0))
                data.add(TabMenu("", R.string.song_list.getStringRes(),0))
                data.add(TabMenu("", R.string.singer.getStringRes(),0))
                data.add(TabMenu("", R.string.hot_songs.getStringRes(),0))
                data.add(TabMenu("", R.string.song_style.getStringRes(),0))
                data.add(ListenSongListTitle(title = "你的精选歌单",text = "查看更多"))
                val randomSongList = dbDao.songListCoverDao().getRandomCovers()
                data.add(SongListCovers(randomSongList))
                data.add(SingleSongTitle(title = "单曲推荐",text = "换一批"))
                val singleSong = dbDao.singleSongDao().getRandomSingleSongs()
                data.add(SingleSongList(singleSong))
                data.add(PoetrySongTitle(title = "诗＆歌"))
                val poetrySong = dbDao.poetrySongDao().getRandomPoetrySongs()
                data.add(PoetrySongList(poetrySong))
                listData.value = data
            }
    }
}
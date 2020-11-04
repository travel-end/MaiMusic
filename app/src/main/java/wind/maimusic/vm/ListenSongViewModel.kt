package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wind.maimusic.R
import wind.maimusic.base.BaseViewModel
import wind.maimusic.model.listensong.*
import wind.maimusic.model.title.ListenSongListTitle
import wind.maimusic.model.title.PoetrySongTitle
import wind.maimusic.model.title.SingleSongTitle
import wind.maimusic.room.database.OnlineSongDatabase
import wind.maimusic.utils.getStringRes

class ListenSongViewModel : BaseViewModel() {
    val listData: MutableLiveData<MutableList<Any>> = MutableLiveData()

    val data = mutableListOf<Any>()
    companion object {
        const val INIT_BANNER = 5
        const val INIT_SONG_LIST=5
        const val INIT_SINGLE_SONG = 3
        const val INIT_POETRY_SONG= 11
    }

    // TODO: 2020/11/4 添加参数 用于刷新 每个歌单封面类加个字段对应到这个歌单的内容（所有歌曲） 用于数据库的查询条件
    fun initListenData() {
        data.clear()
        val dbDao = OnlineSongDatabase.getDatabase()
        viewModelScope.launch {
            val randomBanner = dbDao.listenBannerDao().getStartBanners(3)
            val banner = Banner(randomBanner)
            data.add(banner)
            data.add(TabMenu("", R.string.daily_recommend.getStringRes(), 0))
            data.add(TabMenu("", R.string.song_list.getStringRes(), 0))
            data.add(TabMenu("", R.string.singer.getStringRes(), 0))
            data.add(TabMenu("", R.string.hot_songs.getStringRes(), 0))
            data.add(TabMenu("", R.string.song_style.getStringRes(), 0))
            data.add(ListenSongListTitle(title = "你的精选歌单", text = "查看更多"))
            val randomSongList = dbDao.songListCoverDao().getStartListCovers(4)
            data.add(SongListCovers(randomSongList))
            data.add(
                SingleSongTitle(
                    title = R.string.single_song_recommend.getStringRes(),
                    text = R.string.change_range.getStringRes()
                )
            )
            val singleSongList = dbDao.singleSongDao().getStartSingleSongs(3)
            for (singleSong in singleSongList) {
                data.add(singleSong)
            }
            data.add(PoetrySongTitle(title = R.string.poetry_and_song.getStringRes()))
            val poetrySongList = dbDao.poetrySongDao().getStartPoetrySongs(5)
            for (poetrySong in poetrySongList) {
                data.add(poetrySong)
            }
            listData.value = data
        }
    }

}
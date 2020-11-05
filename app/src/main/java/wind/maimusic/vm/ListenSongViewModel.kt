package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wind.maimusic.R
import wind.maimusic.base.BaseViewModel
import wind.maimusic.model.OnlineSong
import wind.maimusic.model.listensong.*
import wind.maimusic.model.title.ListenSongListTitle
import wind.maimusic.model.title.PoetrySongTitle
import wind.maimusic.model.title.SingleSongTitle
import wind.maimusic.room.database.OnlineSongDatabase
import wind.maimusic.utils.getStringRes

class ListenSongViewModel : BaseViewModel() {
    val listData: MutableLiveData<MutableList<Any>> = MutableLiveData()
    val specialSongList :MutableLiveData<List<OnlineSong>> = MutableLiveData()


    val data = mutableListOf<Any>()

    companion object {
        const val PAGE_SIZE_BANNER = 4
        const val PAGE_SIZE_SONG_LIST = 5
        const val PAGE_SIZE_SINGLE_SONG = 3
        const val PAGE_SIZE_POETRY_SONG = 11
    }

    // TODO: 2020/11/4 添加参数 用于刷新 每个歌单封面类加个字段对应到这个歌单的内容（所有歌曲） 用于数据库的查询条件
    fun getListenData() {
        data.clear()
        val dbDao = OnlineSongDatabase.getDatabase()
        viewModelScope.launch {
            val randomBanner = dbDao.listenBannerDao().getRangeBanners(0,PAGE_SIZE_BANNER)
            val banner = Banner(randomBanner)
            data.add(banner)
            data.add(TabMenu("", R.string.daily_recommend.getStringRes(), 0))
            data.add(TabMenu("", R.string.song_list.getStringRes(), 0))
            data.add(TabMenu("", R.string.singer.getStringRes(), 0))
            data.add(TabMenu("", R.string.hot_songs.getStringRes(), 0))
            data.add(TabMenu("", R.string.song_style.getStringRes(), 0))
            data.add(ListenSongListTitle(title = "你的精选歌单", text = "查看更多"))
            val randomSongList = dbDao.songListCoverDao().getRandomListCovers(PAGE_SIZE_SONG_LIST)
            data.add(SongListCovers(randomSongList))
            data.add(
                SingleSongTitle(
                    title = R.string.single_song_recommend.getStringRes(),
                    text = R.string.change_range.getStringRes()
                )
            )
            val dailySingleSong = dbDao.onlineSongDao().findRandomSingSong(PAGE_SIZE_SINGLE_SONG)
            for (song in dailySingleSong) {
                val singleSong = SingleSong().apply {
                    id = song.id ?: 0
                    songName = song.name
                    singer = song.singer
                    cover = song.imgUrl
                    desc = "dddd"
                }
                data.add(singleSong)
            }
            data.add(PoetrySongTitle(title = R.string.poetry_and_song.getStringRes()))
            val poetrySongList =
                dbDao.onlineSongDao().findOnlinePoetrySong(PAGE_SIZE_POETRY_SONG - 4)
            for (song in poetrySongList) {
                val poetrySong = PoetrySong().apply {
                    id = song.id ?: 0
                    name = song.name
                    singer = song.singer
                    cover = song.imgUrl
                    desc = "ooo"
                }
                data.add(poetrySong)
            }
            listData.value = data
        }
    }

    fun findSongListByType(type:Int) {
        viewModelScope.launch {
            specialSongList.value = withContext(Dispatchers.IO) {
                OnlineSongDatabase.getDatabase().onlineSongDao().findOnlineSongByMainType(type)
            }
        }
    }
}
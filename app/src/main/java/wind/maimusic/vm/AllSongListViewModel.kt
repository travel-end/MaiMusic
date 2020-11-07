package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseViewModel
import wind.maimusic.model.listensong.SongListCover
import wind.maimusic.model.listensong.SongListCovers
import wind.maimusic.room.database.OnlineSongDatabase
import wind.maimusic.utils.LogUtil
import wind.maimusic.utils.getStringRes

class AllSongListViewModel:BaseViewModel() {

    val recommendSongs :MutableLiveData<List<SongListCover>> = MutableLiveData()
    val allSongs:MutableLiveData<List<SongListCovers>> = MutableLiveData()
    private val allSongListType = intArrayOf(
        Constants.ST_CHINESE,
        Constants.ST_QUITE,
        Constants.ST_ANTIQUE,
        Constants.ST_ENGLISH,
        Constants.ST_JAPANESE,
        Constants.ST_CLASSIC,
        Constants.ST_PURE,
        Constants.ST_FOLK,
        Constants.ST_CURE
    )

    fun initAllSongListData() {
        val dbDao = OnlineSongDatabase.getDatabase()
        viewModelScope.launch {
            recommendSongs.value = dbDao.songListCoverDao().getRandomListCovers(6)
            val allSongList = mutableListOf<SongListCovers>()
            for (type in allSongListType) {
                val result = dbDao.songListCoverDao().findSongListByType(type)
                when(type) {
                    Constants.ST_CHINESE->{
                        val covers = SongListCovers(R.string.chinese.getStringRes(),result)
                        allSongList.add(covers)
                    }
                    Constants.ST_PURE->{
                        val covers = SongListCovers(R.string.pure_music.getStringRes(),result)
                        allSongList.add(covers)
                    }
                    Constants.ST_JAPANESE->{
                        val covers = SongListCovers(R.string.japanese.getStringRes(),result)
                        allSongList.add(covers)
                    }
                    Constants.ST_ENGLISH->{
                        val covers = SongListCovers(R.string.english.getStringRes(),result)
                        allSongList.add(covers)
                    }
                    Constants.ST_ANTIQUE->{
                        val covers = SongListCovers(R.string.antique.getStringRes(),result)
                        allSongList.add(covers)
                    }
                    Constants.ST_FOLK->{
                        val covers = SongListCovers(R.string.folk.getStringRes(),result)
                        allSongList.add(covers)
                    }
                    Constants.ST_CLASSIC->{
                        val covers = SongListCovers(R.string.classic.getStringRes(),result)
                        allSongList.add(covers)
                    }
                    Constants.ST_QUITE->{
                        val covers = SongListCovers(R.string.quite.getStringRes(),result)
                        allSongList.add(covers)
                    }
                    Constants.ST_CURE->{
                        val covers = SongListCovers(R.string.cure.getStringRes(),result)
                        allSongList.add(covers)
                    }
                }
            }
            LogUtil.e("-----AllSongListViewModel allSong size:${allSongList.size}")
            allSongs.value = allSongList
        }
    }
}
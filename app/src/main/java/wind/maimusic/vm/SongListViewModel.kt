package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wind.maimusic.Constants
import wind.maimusic.base.BaseViewModel
import wind.maimusic.model.OnlineSong
import wind.maimusic.model.songlist.SongListTop
import wind.maimusic.room.database.OnlineSongDatabase
import wind.maimusic.utils.DataUtil
import wind.maimusic.utils.LogUtil
import wind.widget.cost.Consts
import kotlin.random.Random

/**
 * @By Journey 2020/11/2
 * @Description
 */
class SongListViewModel : BaseViewModel() {
    val onlineSongs: MutableLiveData<MutableList<OnlineSong>> = MutableLiveData()
    fun getOnlineSongs(listType: Int) {
        val dbDao = OnlineSongDatabase.getDatabase().onlineSongDao()
        viewModelScope.launch {
            when (listType) {
                Constants.ST_DAILY_RECOMMEND -> {
                    /*获取每日推荐的歌曲   从曲库中随机抽取xx首歌  每天更新一此 TODO 这个算法要重新计算一下*/
                    val result = withContext(Dispatchers.IO) {
                        val startIndex =
                            DataUtil.getTheDayStartIndex(Constants.DAILY_RECOMMEND_SONG)
                        LogUtil.e("----SongListViewModel ST_DAILY_RECOMMEND startIndex:$startIndex")
                        dbDao.findRangeOnlineSongs(startIndex, Constants.PAGE_SIZE_DAILY_RECOMMEND)
                            .toMutableList()
                    }
                    onlineSongs.value = result
                }
                Constants.ST_DAILY_HOT_SONG -> {

                }

            }

        }
    }

    fun findSingerSongs(singerId: Int) {
        if (singerId != -1) {
            val dao = OnlineSongDatabase.getDatabase().onlineSongDao()
            viewModelScope.launch {
                val result = withContext(Dispatchers.IO) {
                    dao.findSongBySingerId(singerId)
                }
                onlineSongs.value = result.toMutableList()
            }
        }

    }
}
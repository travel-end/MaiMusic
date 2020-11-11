package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wind.maimusic.R
import wind.maimusic.base.BaseViewModel
import wind.maimusic.model.searchhot.HotSearchSongList
import wind.maimusic.model.searchhot.RecommendSearchList
import wind.maimusic.model.searchhot.SearchHistory
import wind.maimusic.model.title.PoetrySongTitle
import wind.maimusic.model.title.SingleSongTitle
import wind.maimusic.model.title.Title
import wind.maimusic.room.database.MaiDatabase
import wind.maimusic.utils.getStringRes

/**
 * @By Journey 2020/10/27
 * @Description
 */
class SearchHotViewModel:BaseViewModel() {
    val mData:MutableLiveData<MutableList<Any>> = MutableLiveData()
    val data = mutableListOf<Any>()
    val deleteHistory:MutableLiveData<Boolean> = MutableLiveData()
    fun initHotSearchData() {
        val searchDao = MaiDatabase.getDatabase().searchSongDao()
        viewModelScope.launch {
//            //1
//            data.add(Title(R.string.search_history.getStringRes()))
            //2
            val historyTags = searchDao.findRecentSearchHistory(5)
            data.add(SearchHistory(historyTags))
            // 3
//            data.add(PoetrySongTitle(R.string.recommend_search.getStringRes()))
            // 4
            val recommendSearch = searchDao.findRandomRecommendSearch(5)
            data.add(RecommendSearchList(recommendSearch))
            //5
            data.add(SingleSongTitle(R.string.hot_search.getStringRes()))
            // 6
            val hotSearchSongs = searchDao.findRandomHotSearchSongs(16)
            data.add(HotSearchSongList(hotSearchSongs))
            mData.value = data
        }
    }

    fun deleteSearchHistoryTag() {
        val dao = MaiDatabase.getDatabase().searchSongDao()
        viewModelScope.launch {
            val result = dao.deleteSearchHistoryTag()
            if (result!=0) {
                deleteHistory.value = true
            }
        }
    }
}
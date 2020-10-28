package wind.maimusic.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wind.maimusic.R
import wind.maimusic.base.BaseViewModel
import wind.maimusic.base.state.State
import wind.maimusic.base.state.StateType
import wind.maimusic.model.core.SearchSong
import wind.maimusic.utils.getStringRes

class SearchResultViewModel:BaseViewModel() {
    val searchResult: MutableLiveData<SearchSong> = MutableLiveData()
    fun searchSong(searchContent: String, page: Int) {
        loadStatus.value = State(StateType.LOADING_SONG)
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    apiService.search(searchContent, page)
                }
            }.onSuccess {
                val songList = it.data?.song?.list
                Log.e("JG", "关键词搜索结果：${songList}")
                Log.e("JG", "关键词搜索结果第一条：${songList!![0]}")
                searchResult.value = it
            }.onFailure {
                handleException(it, State(StateType.ERROR,msg = R.string.empty.getStringRes()))
            }
            loadStatus.value = State(StateType.DISMISSING_SONG)
        }
    }
}

class SearchAlbumResultViewModel:BaseViewModel()  {

}
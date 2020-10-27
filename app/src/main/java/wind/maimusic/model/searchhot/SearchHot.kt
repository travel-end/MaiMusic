package wind.maimusic.model.searchhot

import wind.maimusic.model.title.Title

/**
 * @By Journey 2020/10/27
 * @Description
 */
data class SearchHot(
    var searchHistoryTitle:Title?=null,
    var searchHistory:SearchHistory?=null,
    var searchHotSongTitle:Title?=null,
    var searchHotSong:SearchHotSong?=null
)
package wind.maimusic.model.searchhot



/**
 * @By Journey 2020/10/27
 * @Description
 */
data class SearchHistory(
    val historyList:MutableList<HistoryTag>?=null,
    val title:String?=null
)


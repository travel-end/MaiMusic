package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import wind.maimusic.base.BaseViewModel
import wind.maimusic.model.searchhot.HistoryTag
import wind.maimusic.model.searchhot.SearchHistory
import wind.maimusic.model.searchhot.SearchHotSong
import wind.maimusic.model.title.HotSearchTitle

/**
 * @By Journey 2020/10/27
 * @Description
 */
class SearchHotViewModel:BaseViewModel() {

    val hotSearch:MutableLiveData<MutableList<Any>> = MutableLiveData()
    fun getTempData() {
//        val historyTitle = Title(
//            "历史",
//            icon = R.drawable.delete
//        )
        val tag1 = HistoryTag(0,"dsd")
        val tag2 = HistoryTag(1,"dsddd")
        val tag3 = HistoryTag(2,"dsd")
        val tag4 = HistoryTag(3,"dsdsd")
        val tag5 = HistoryTag(4,"ddsd")
        val tag6 = HistoryTag(5,"ddddsd")
//        tag1.id = 0
//        tag1.name = "连记敌对的忆"
//        val tag2 = TagBean()
//        tag2.id = 1
//        tag2.name = "杰伦"
//        val tag3 = TagBean()
//        tag3.id = 2
//        tag3.name = "陈奕迅"
//        val tag4 = TagBean()
//        tag4.id = 3
//        tag4.name = "月天"
//        val tag5 = TagBean()
//        tag5.id = 4
//        tag5.name = "风清扬你的"
//        val tag6 = TagBean()
//        tag6.id = 5
//        tag6.name = "雨晏家"
//        val tag7 = TagBean()
//        tag7.id = 6
//        tag7.name = "雨晏dddd家"
//        val tag8 = TagBean()
//        tag8.id = 7
//        tag8.name = "ddd家"
//        val tag9 = TagBean()
//        tag9.id = 8
//        tag9.name = "雨晏ddddoooo"
//        val tag10 = TagBean()
//        tag10.id = 9
//        tag10.name = "雨晏ddddoooo"
        val historyList = mutableListOf<HistoryTag>().apply {
            add(tag1)
            add(tag2)
            add(tag3)
            add(tag4)
            add(tag5)
            add(tag6)
        }
//        val historyList = listOf(tag1,tag2,tag3,tag4,tag5,tag6,tag7,tag8,tag9)
        val historyContent = SearchHistory(historyList,"历史")
        val hotSongTitle = HotSearchTitle(
            "热搜"
        )
        val hotSongs = mutableListOf<SearchHotSong>(
            SearchHotSong("执迷不悟","爱你的我头很铁",true),
            SearchHotSong("忘川彼岸","爱你的我头很铁",true),
            SearchHotSong("笑纳","爱你的我头很铁",true),
            SearchHotSong("会不会","爱你的我头很铁",true),
            SearchHotSong("游京","爱你的我头很铁",true),
            SearchHotSong("可可托海的牧羊人哈哈哈哈","爱你的我头很铁",true),
            SearchHotSong("鹿晗","爱你的我头很铁",true),
            SearchHotSong("醉倾城","爱你的我头很铁",true),
            SearchHotSong("夜空中最亮的星","爱你的我头很铁",true),
            SearchHotSong("邓紫棋","爱你的我头很铁",true),
            SearchHotSong("飞鸟和馋","爱你的我头很铁",true),
            SearchHotSong("少尿","爱你的我头很铁",true),
            SearchHotSong("邻居节","爱你的我头很铁",true),
            SearchHotSong("飞鸟和馋","爱你的我头很铁",true),
            SearchHotSong("你笑起来真好看","爱你的我头很铁",true),
            SearchHotSong("不过人间","爱你的我头很铁",true)
        )
        val searchHot = mutableListOf<Any>()
//        searchHot.add(historyTitle)
        searchHot.add(historyContent)
        searchHot.add(hotSongTitle)
        for (song in hotSongs) {
            searchHot.add(song)
        }
        hotSearch.value = searchHot
    }
}
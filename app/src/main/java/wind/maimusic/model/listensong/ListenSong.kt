package wind.maimusic.model.listensong

import wind.maimusic.model.title.Title

/**
 * @By Journey 2020/11/3
 * @Description
 */
data class ListenSong(
    val bannerList:List<ListenBanner>,
    val tabMenuList:List<TabMenu>,
    val songListTitle:Title,
    val songList:List<SongListItem>,
    val singleSongTitle:Title,
    val singleSongList:List<SingleSong>,
    val poetrySongTitle:Title,
    val poetrySongList:List<PoetrySong>
)
data class ListenBanner(
    val imgUrl:String = "",
    val title:String = "",
    val desc:String = "",
    val type:String= ""
)
data class TabMenu(
    val imgUrl:String="",
    val menuName:String = "",
    val cover:Int
)
data class SongListItem(
    val cover:String = "",
    val listName:String = "",
    val desc:String = "",
    val type:Int
)
data class SingleSong(
    val songName:String = "",
    val singer:String = "",
    val album:String = "",
    val cover:String = "",
    val desc:String = ""
)
data class PoetrySong(
    val name:String = "",
    val singer:String = "",
    val cover:String = "",
    val desc:String = "",
    val isLoved:Boolean = false
)

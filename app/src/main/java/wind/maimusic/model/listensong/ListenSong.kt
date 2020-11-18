package wind.maimusic.model.listensong

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import wind.maimusic.model.title.Title

/**
 * @By Journey 2020/11/3
 * @Description
 */
data class ListenSong(
    val bannerList:Banner,
    val tabMenuList:TabMenuList,
    val songListTitle:Title,
    val songList:List<SongListCover>,
    val singleSongTitle:Title,
    val singleSongList:List<SingleSong>,
    val poetrySongTitle:Title,
    val poetrySongList:List<PoetrySong>
)
data class Banner(
    val bannerList:List<ListenBanner>
)

@Entity(tableName = "listen_banner")
data class ListenBanner(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @ColumnInfo
    val imgUrl:String = "",
    @ColumnInfo
    val title:String = "",
    @ColumnInfo
    val desc:String = "",
    @ColumnInfo
    val type:String= ""
)

data class TabMenuList(
    val tabMenuList: List<TabMenu>
)

data class TabMenu(
    val imgUrl:String="",
    val menuName:String = "",
    val cover:Int
)

@Entity(tableName = "song_list_cover")
data class SongListCover(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @ColumnInfo
    val cover:String = "",
    @ColumnInfo
    val listName:String = "",
    @ColumnInfo
    val desc:String = "",
    @ColumnInfo
    val type:Int,
    @ColumnInfo
    val subType:Int,
    @ColumnInfo
    val tag1:Int,
    @ColumnInfo
    val tag2:Int
)

data class SongListCovers(
    val title:String="",
    val listCovers:List<SongListCover>
)
data class ClassifySongList(
    val cure:List<SongListCover>,
    val quite:List<SongListCover>,
    val classic:List<SongListCover>,
    val folk:List<SongListCover>,
    val antique:List<SongListCover>,
    val english:List<SongListCover>,
    val japanese:List<SongListCover>,
    val pure:List<SongListCover>,
    val chinese:List<SongListCover>,
    val recommend:List<SongListCover>
)

//@Entity(tableName = "single_song")
data class SingleSong(
//    @PrimaryKey(autoGenerate = true)
    var id:Int?=0,
//    @ColumnInfo
    var songName:String? = "",
//    @ColumnInfo
    var singer:String? = "",
//    @ColumnInfo
    var album:String? = "",
//    @ColumnInfo
    var cover:String? = "",
//    @ColumnInfo
    var desc:String? = ""
)

data class SingleSongList(
    val singleSongList: List<SingleSong>
)

//@Entity(tableName = "poetry_song")
data class PoetrySong(
//    @PrimaryKey(autoGenerate = true)
    var id:Int?=0,
//    @ColumnInfo
    var name:String? = "",
//    @ColumnInfo
    var singer:String? = "",
//    @ColumnInfo
    var cover:String? = "",
//    @ColumnInfo
    var desc:String? = "",
//    @ColumnInfo
    var isLoved:Boolean = false
)
data class PoetrySongList(
    val poetrySongList: List<PoetrySong>
)

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
    val type:Int
)

data class SongListCovers(
    val listCovers:List<SongListCover>
)

@Entity(tableName = "single_song")
data class SingleSong(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @ColumnInfo
    val songName:String = "",
    @ColumnInfo
    val singer:String = "",
    @ColumnInfo
    val album:String = "",
    @ColumnInfo
    val cover:String = "",
    @ColumnInfo
    val desc:String = ""
)

data class SingleSongList(
    val singleSongList: List<SingleSong>
)

@Entity(tableName = "poetry_song")
data class PoetrySong(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @ColumnInfo
    val name:String = "",
    @ColumnInfo
    val singer:String = "",
    @ColumnInfo
    val cover:String = "",
    @ColumnInfo
    val desc:String = "",
    @ColumnInfo
    val isLoved:Boolean = false
)
data class PoetrySongList(
    val poetrySongList: List<PoetrySong>
)

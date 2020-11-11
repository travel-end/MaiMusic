package wind.maimusic.model.searchhot

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @By Journey 2020/11/11
 * @Description
 */
data class Search(
    val recommendSearch:List<RecommendSearch>,
    val hotSearchSong:List<HotSearchSong>
)

@Entity(tableName = "t_recommend_search")
data class RecommendSearch(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    @ColumnInfo
    val tagName:String,
    @ColumnInfo
    val tagId:Int
)

data class RecommendSearchList(
    val recommendSearch:List<RecommendSearch>
)

@Entity(tableName = "t_hot_search")
data class HotSearchSong(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    @ColumnInfo
    val songName:String,
    @ColumnInfo
    val desc:String,
    @ColumnInfo
    val songId:Int
)

data class HotSearchSongList(
    val hotSearchSongs:List<HotSearchSong>
)
package wind.maimusic.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "online_song")
data class OnlineSong(
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null,
    @ColumnInfo
    var songId:String? = "",
    @ColumnInfo
    var mediaId:String? = "",
    @ColumnInfo
    var qqId:String? = "",
    @ColumnInfo
    var name:String? = "",
    @ColumnInfo
    var singer:String? = "",
    @ColumnInfo
    var url :String? = "",
    @ColumnInfo
    var imgUrl: String? = null,
    @ColumnInfo
    var pic:String? = "",
    @ColumnInfo
    var lrc :String? = "",
    @ColumnInfo
    var duration:Int?=0,
    @ColumnInfo
    var isOnline:Boolean = false,
    @ColumnInfo
    var songmid:String?=null,
    @ColumnInfo
    var albumName: String? = null,
    @ColumnInfo
    var listType:Int?=null,
    @ColumnInfo
    var isDownload:Boolean = false,
    @ColumnInfo
    var mainType:Int? =0,//对应主歌单名称
    @ColumnInfo
    var secondType:Int?=0,// 对应次歌单名称
    @ColumnInfo
    var isPoetrySong:Int=0,//是否是诗歌 0不是  1是
    @ColumnInfo
    var poetrySongId:Int?=0,// 如果是诗歌，需要有一个id对应匹配的诗歌内容（文章）
    @ColumnInfo
    var singerId:Int?=null
)
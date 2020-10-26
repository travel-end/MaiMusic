package wind.maimusic.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @By Journey 2020/9/30
 * @Description
 */
@Entity(tableName = "love_song")
data class LoveSong(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    @ColumnInfo val songId:String?=null,
    @ColumnInfo val mediaId:String?=null,// 下载标识符
    @ColumnInfo val qqId:String?=null,
    @ColumnInfo var name:String?=null,
    @ColumnInfo val singer:String?=null,
    @ColumnInfo val url:String?=null,
    @ColumnInfo val pic:String?=null,
    @ColumnInfo val duration:Int?=null,
    @ColumnInfo val isOnline:Boolean?=null,
    @ColumnInfo val isDownload:Boolean?=null
)
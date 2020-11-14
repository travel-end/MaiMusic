package wind.maimusic.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_just_online_song")
data class JustOnlineSong(
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null,
    @ColumnInfo
    var songId:String?=null,
    @ColumnInfo
    var mediaId:String?=null,
    @ColumnInfo
    var qqId:String?=null,
    @ColumnInfo
    var name:String?=null,
    @ColumnInfo
    var albumName:String?=null,
    @ColumnInfo
    var singer:String?=null,
    @ColumnInfo
    var url:String?=null,
    @ColumnInfo
    var pic:String?=null,
    @ColumnInfo
    var lrc:String?=null,
    @ColumnInfo
    var duration:Long?=null,
    @ColumnInfo
    var isDownloaded:Boolean?=null,
    @ColumnInfo
    var isOnline:Boolean?=null
)
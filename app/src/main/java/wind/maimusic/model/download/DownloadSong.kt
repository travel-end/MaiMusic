package wind.maimusic.model.download

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "down_song")
data class DownloadSong(
    @PrimaryKey
    var id:Long?=null,
    @ColumnInfo var songId:String?=null,
    @ColumnInfo var songName:String?=null,
    @ColumnInfo var singer:String?=null,
    @ColumnInfo var progress:Int?=null,
    @ColumnInfo var url:String?=null,
    @ColumnInfo var pic:String?=null,
    @ColumnInfo var duration:Int?=null,
    @ColumnInfo var currentSize:Long?=null,
    @ColumnInfo var totalSize:Long?=null,
    @ColumnInfo var position:Int?=null,// 正在下載歌曲在列表中的位置
    @ColumnInfo var status:Int?=null,
    @ColumnInfo var albumName:String?=null
)
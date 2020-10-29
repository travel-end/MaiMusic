package wind.maimusic.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "online_song")
data class OnlineSong(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 1,
    @ColumnInfo
    var songId:String = "",
    @ColumnInfo
    var mediaId:String = "",
    @ColumnInfo
    var qqId:String = "",
    @ColumnInfo
    var name:String = "",
    @ColumnInfo
    var singer:String = "",
    @ColumnInfo
    var url :String = "",
    @ColumnInfo
    var pic:String = "",
    @ColumnInfo
    var lrc :String = "",
    @ColumnInfo
    var duration:Int=0,
    @ColumnInfo
    var isDownload:Boolean = false
)
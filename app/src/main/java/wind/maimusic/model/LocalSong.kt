package wind.maimusic.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_song")
data class LocalSong(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    @ColumnInfo var songId:String?=null,
    @ColumnInfo var qqId:String?=null,
    @ColumnInfo var name:String?=null,
    @ColumnInfo var singer:String?=null,
    @ColumnInfo var url:String?=null,
    @ColumnInfo var pic:String?=null,
    @ColumnInfo var duration:Long?=null
)
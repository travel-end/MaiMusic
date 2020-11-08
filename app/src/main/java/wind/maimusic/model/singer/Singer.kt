package wind.maimusic.model.singer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_singer")
data class Singer(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    @ColumnInfo
    val name:String?="",
    @ColumnInfo
    val singerId:Int?=null,
    @ColumnInfo
    var cover:String?=null,
    @ColumnInfo
    val sex:Int?=null
)
data class SingerList(
    val chineseSingers:List<Singer>,
    val otherSingers:List<Singer>
)
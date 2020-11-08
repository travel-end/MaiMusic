package wind.maimusic.model.songlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_created_song_list")
data class SongListItem(
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null,
    @ColumnInfo
    var title:String? = "",
    @ColumnInfo
    var cover:String? = "",
    @ColumnInfo
    var desc:String? = "",
    @ColumnInfo
    var type:Int=0,
    @ColumnInfo
    var content:String?="",
    @ColumnInfo
    var readNum:Int =0
)
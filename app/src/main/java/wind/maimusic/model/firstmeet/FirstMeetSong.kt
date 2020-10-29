package wind.maimusic.model.firstmeet

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * @By Journey 2020/10/24
 * @Description
 */
@Entity(tableName = "first_meet_song")
data class FirstMeetSong(
    @PrimaryKey
    var id: Int? = null,
    @ColumnInfo
    var songId: String? = null,
    @ColumnInfo
    var singer: String? = null,
    @ColumnInfo
    var songName: String? = null,
    @ColumnInfo
    var songmid:String?=null,
    @ColumnInfo
    var url:String?=null,
    @ColumnInfo
    var imgUrl: String? = null,
    @ColumnInfo
    var duration: Int? = null,
    @ColumnInfo
    var isOnline: Boolean? = null,
    @ColumnInfo
    var mediaId: String? = null,
    @ColumnInfo
    var albumName: String? = null,
    @ColumnInfo
    var isDownload: Boolean? = null
)
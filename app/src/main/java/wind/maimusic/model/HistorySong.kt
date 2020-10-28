package wind.maimusic.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_song")
data class HistorySong(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo var songId: String? = null,
    @ColumnInfo var mediaId: String? = null,// 下载标识符
    @ColumnInfo var qqId: String? = null,
    @ColumnInfo var name: String? = null,
    @ColumnInfo var singer: String? = null,
    @ColumnInfo var url: String? = null,
    @ColumnInfo var pic: String? = null,
    @ColumnInfo var duration: Int? = null,
    @ColumnInfo var isOnline: Boolean = false,
    @ColumnInfo var isDownload: Boolean = false
)
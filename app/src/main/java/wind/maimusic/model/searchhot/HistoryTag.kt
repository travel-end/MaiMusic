package wind.maimusic.model.searchhot

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @By Journey 2020/10/27
 * @Description
 */
@Entity
data class HistoryTag(
    @PrimaryKey
    val id :Int?=null,
    @ColumnInfo
    val name:String? = null
)

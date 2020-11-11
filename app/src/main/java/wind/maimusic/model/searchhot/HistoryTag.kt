package wind.maimusic.model.searchhot

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @By Journey 2020/10/27
 * @Description
 */
@Entity(tableName = "t_search_history")
data class HistoryTag(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo
    val name: String? = null,
    @ColumnInfo
    val tagId: String? = null
)

package wind.maimusic.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @By Journey 2020/11/18
 * @Description
 */
@Entity(tableName = "t_cuter")
data class Cuter(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    @ColumnInfo
    val phone:String?=null,
    @ColumnInfo
    val password:String?=null,
    @ColumnInfo
    val cuterId:Int?=null,
    @ColumnInfo
    val cuterCover:String?=null
)
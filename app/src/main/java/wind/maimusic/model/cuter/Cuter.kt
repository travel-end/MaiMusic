package wind.maimusic.model.cuter

/**
 * @By Journey 2020/11/18
 * @Description
 */
//@Entity(tableName = "t_cuter")
data class Cuter(
//    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
//    @ColumnInfo
    var phone:String?=null,
//    @ColumnInfo
    var password:String?=null,
//    @ColumnInfo
    var cuterId:Int?=null,
//    @ColumnInfo
    var cuterCover:String?=null,
//    @ColumnInfo
    var nickName:String?=null,

    var cuterBg:String?=null
)
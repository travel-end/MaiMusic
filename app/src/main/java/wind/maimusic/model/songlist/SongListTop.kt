package wind.maimusic.model.songlist

/**
 * @By Journey 2020/11/2
 * @Description
 */
data class SongListTop(
    var songListName:String?="",
    var songListDescription:String?="",
    var songListAuthor:String?="",
    var songListCoverImgUrl:String?="",
    var songListTagA:String?="",
    var songListTagB:String?="",
    var type:String?="",
    var publicTime:String?="",
    var language:String?="",
    var company:String?=""
)
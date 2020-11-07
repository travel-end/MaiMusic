package wind.maimusic.model.songlist

data class SongListItem(
    var title:String? = "",
    var cover:String? = "",
    var desc:String? = "",
    var type:Int=0,
    var content:String?="",
    var readNum:Int =0
)
package wind.maimusic.model.download

data class DownloadEvent(
    var downloadStatus:Int?=null,
    var downloadSong: DownloadSong?=null,
    var position:Int?=null
)
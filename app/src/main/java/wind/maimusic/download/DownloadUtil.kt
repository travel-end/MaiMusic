package wind.maimusic.download

import wind.maimusic.Constants
import wind.maimusic.model.Downloaded
import java.io.File
import java.util.*

object DownloadUtil {
//    fun getSongFromFile(fileName:String):MutableList<Downloaded>? {
//        //将.m4a截取掉得到singer-songName-duration-songId-size
//        val res = mutableListOf<Downloaded>()
//        val file = File(fileName)
//        if (!file.exists()) {
//            file.mkdirs()
//            return res
//        }
//        val subFiles = file.listFiles()
//        if (subFiles != null) {
////            for (value in subFiles) {
//                for (i in subFiles.indices.reversed()) {
//                    val value = subFiles[i]
//                    if (value != null) {
//                        val songFileName = value.name
//                        val songFile = songFileName.substring(0,songFileName.lastIndexOf("."))
//                        val songValue = songFile.split("-")
//                        val size = songValue[4].toLong()
//                        //如果文件的大小不等于实际大小，则表示该歌曲还未下载完成，被人为暂停，故跳过该歌曲，不加入到已下载集合
//                        if (size != value.length()) continue
//                        val downloadSong = Downloaded().apply {
//                            singer = songValue[0]
//                            name = songValue[1]
//                            duration = songValue[2].toLong()
//                            songId = songValue[3]
//                            url = "$fileName$songFileName"
//                        }
//                        res.add(downloadSong)
//                    }
//                }
////            }
//            return res
//        }
//        return null
//    }

    fun hasDownloaded(songId: String): Boolean {
        //将.m4a截取掉得到singer-songName-duration-songId-size
        val file = File(Constants.downloadSongUrl())
        if (!file.exists()) {
            file.mkdirs()
            return false
        }
        val subFile: Array<File>? = file.listFiles()
        subFile?.let {
            for (value in subFile) {
                val songFileName = value.name
                val songFile = songFileName.substring(0, songFileName.lastIndexOf("."))
                val songValue =
                    songFile.split("-".toRegex()).toTypedArray()
                //如果文件的大小不等于实际大小，则表示该歌曲还未下载完成，被人为暂停，故跳过该歌曲，不加入到已下载集合
                if (songValue[3] == songId) {
                    val size = java.lang.Long.valueOf(songValue[4])
                    return size == value.length()
                }
            }
        }
        return false
    }

    fun getDownloadedSong(fileName: String): List<Downloaded> {
        //将.m4a截取掉得到singer-songName-duration-songId-size
        val res: MutableList<Downloaded> = ArrayList()
        val file = File(fileName)
        if (!file.exists()) {
            file.mkdirs()
            return res
        }
        val subFile = file.listFiles()
        subFile?.let {
            for (value in subFile) {
                val songFileName = value.name
                val songFile = songFileName.substring(0, songFileName.lastIndexOf("."))
                val songValue =
                    songFile.split("-".toRegex()).toTypedArray()
                val size = java.lang.Long.valueOf(songValue[4])
                //如果文件的大小不等于实际大小，则表示该歌曲还未下载完成，被人为暂停，故跳过该歌曲，不加入到已下载集合
                if (size != value.length()) continue
                val downloadSong = Downloaded()
                downloadSong.singer=songValue[0]
                downloadSong.name=songValue[1]
                downloadSong.duration=java.lang.Long.valueOf(songValue[2])
                downloadSong.songId=songValue[3]
                downloadSong.url=fileName + songFileName
                res.add(downloadSong)
            }
        }
        return res
    }


    /**
     * 组装下载歌曲的文件名
     */
    fun getSaveSongFile(singer:String,songName:String,duration:Long,songId:String,size:Long):String {
        return "$singer-$songName-$duration-$songId-$size.m4a"
    }
}
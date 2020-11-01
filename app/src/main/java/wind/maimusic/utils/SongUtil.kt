package wind.maimusic.utils

import android.graphics.Bitmap
import android.widget.ImageView
import wind.maimusic.Constants
import wind.maimusic.model.HistorySong
import wind.maimusic.model.LocalSong
import wind.maimusic.model.core.ListBean
import wind.maimusic.model.firstmeet.FirstMeetSong
import wind.widget.R
import wind.widget.cost.Consts
import wind.widget.model.Song
import wind.widget.utils.loadImg
import java.io.*
import kotlin.concurrent.thread

/**
 * @By Journey 2020/10/26
 * @Description
 */
object SongUtil {
    /**
     * 将一个对象写入流中 对象必须实现Serializable接口
     * 将当前的歌曲song对象存入本地
     */
    fun saveSong(song: Song?) {
        try {
            if (song != null) {
                val file =
                    File(Constants.currentSongUrl())
                if (!file.exists()) {
                    file.mkdirs()
                }
                //写对象流的对象
                val songFile = File(file, "song.txt")
                val oos =
                    ObjectOutputStream(FileOutputStream(songFile))
                oos.writeObject(song) //将Person对象p写入到oos中
                oos.close() //关闭文件流
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            LogUtil.e("------SongUtil saveSong error:${e.message}----")
        } catch (e: IOException) {
            e.printStackTrace()
            LogUtil.e("------SongUtil saveSong error:${e.message}----")
        }
    }

    fun getSong(): Song? {
        try {
            val input = FileInputStream("${Constants.currentSongUrl()}/song.txt")
            val ois = ObjectInputStream(input)
            return ois.readObject() as Song //返回对象
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            LogUtil.e("------SongUtil getSong no local song:${e.message}----")
            return null
        } catch (e: IOException) {
            LogUtil.e("------SongUtil getSong no local song:${e.message}----")
            e.printStackTrace()
            return null
        } catch (e: ClassNotFoundException) {
            LogUtil.e("------SongUtil getSong no local song:${e.message}----")
            e.printStackTrace()
            return null
        }
    }

    /**
     * 将本地音乐的封面图片存入本地
     */
    fun saveSongCover(bitmap: Bitmap?, singer:String):Boolean {
        if (bitmap != null) {
            val file = File(Constants.coverImgUrl())
            if (!file.exists()) {
                file.mkdirs()
            }
            val singerImgFile = File(file,"$singer.jpg")
            var fos : FileOutputStream?=null
            try {
                fos = FileOutputStream(singerImgFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos)
                fos.flush()
                return true
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                return false
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            } finally {
                try {
                    fos?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return false
    }

    /**
     * 读取本地音乐的封面图片
     */
    fun loadLocalSongCover(singer: String, v: ImageView) {
        val mS:String
        mS = if (singer.contains("/")) {
            val s = singer.split("/")
            s[0].trim()
        } else {
            singer.trim()
        }
        val imgUrl = "${Constants.coverImgUrl()}$mS.jpg"
        LogUtil.e("本地音乐封面路径：$imgUrl")
        v.loadImg(imgUrl,placeholder = R.drawable.disk,error = R.drawable.disk)
    }

    /**
     * 保存歌词到本地
     */
    fun saveLrcText(lrc:String,songName:String) {
        thread {
            val file = File(Constants.lrcTextUrl())
            if (!file.exists()) {
                file.mkdirs()
            }
            val name = if (songName.isBlank()) {
                "maizi"
            } else {
                songName
            }
            val lrcFile = File(file,"$name.lrc")
            try {
                val fileWriter = FileWriter(lrcFile)
                fileWriter.write(lrc)
                fileWriter.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 读取本地歌词
     */
    fun loadLocalLrcText(songName:String) :String? {
        return try {
            val fileReader = FileReader("${Constants.lrcTextUrl()}$songName.lrc")
            val bufferReader = BufferedReader(fileReader)
            val lrc = StringBuilder()
            while(true) {
                val s = bufferReader.readLine() ?: break
                lrc.append(s).append("\n")
            }
            fileReader.close()
//            LogUtil.e("本地歌词：$lrc")
            lrc.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    fun assemblySong(s: Any,songType:Int,pos:Int=0):Song {
        var song:Song?=null
        when(songType) {
            Consts.ONLINE_LIST_TYPE_FIRST_MEET->{
               val fmSong = s as FirstMeetSong
                song = Song().apply {
                    songId = fmSong.songId //004DrG5A2nm7q2
                    singer = fmSong.singer// 鸾音社
                    songName = fmSong.songName// 夜来寒雨晓来风
                    imgUrl = fmSong.imgUrl
                    duration = fmSong.duration?:0//187  (秒)
                    isOnline = fmSong.isOnline?:false
                    mediaId = fmSong.mediaId//004DrG5A2nm7q2
                    albumName = fmSong.albumName//夜来寒雨晓来风
                    isDownload =fmSong.isDownload?:false
                    listType = Consts.LIST_TYPE_ONLINE
                    onlineSubjectType = Consts.ONLINE_LIST_TYPE_FIRST_MEET
                    position = pos
                }
            }
            Consts.LIST_TYPE_LOCAL->{
                val localSong = s as LocalSong
                song = Song().apply {
                    songName = localSong.name
                    singer = localSong.singer
                    url = localSong.url
                    duration = localSong.duration?.toInt() ?: 0
                    position = pos
                    isOnline = false
                    songId = localSong.songId
                    listType = Consts.LIST_TYPE_LOCAL
                }
            }
            Consts.LIST_TYPE_HISTORY->{
                val historySong = s as HistorySong
                song = Song().apply {
                    songId = historySong.songId
                    songName = historySong.name
                    singer = historySong.singer
                    isOnline = historySong.isOnline
                    url = historySong.url
                    imgUrl = historySong.pic
                    position = pos
                    duration = historySong.duration?:0
                    listType = Consts.LIST_TYPE_HISTORY
                    mediaId = historySong.mediaId
                }
            }
            Consts.LIST_TYPE_DOWNLOAD->{

            }
            Consts.LIST_TYPE_LOVE->{

            }
            Consts.LIST_TYPE_ONLINE->{
                val online = s as ListBean
                song = Song().apply {
                    songId = online.songmid //004DrG5A2nm7q2
                    singer = StringUtil.getSinger(s)// 鸾音社
                    songName = online.songname// 夜来寒雨晓来风
                    imgUrl = "${Consts.ALBUM_PIC}${online.albummid}${Consts.JPG}"////http://y.gtimg.cn/music/photo_new/T002R180x180M000004UvnL62KXhCQ.jpg
                    duration = online.interval//187  (秒)
                    isOnline = true
                    mediaId = online.strMediaMid//004DrG5A2nm7q2
                    albumName = online.albumname//夜来寒雨晓来风
                    isDownload = DownloadedUtil.isExistOfDownloadSong(online.songmid?:"")//003IHI2x3RbXLS  // 是否已经下载过了（初次搜索为false）
                }
            }
        }
        return song!!
    }
//    const val SONG_FIRST_MEET =0
//    const val SONG_LOCAL = 1
//    const val SONG_HISTORY = 2
//    const val SONG_DOWNLOAD =3
//    const val SONG_LOVE = 4
//    const val SONG_ONLINE =5
}
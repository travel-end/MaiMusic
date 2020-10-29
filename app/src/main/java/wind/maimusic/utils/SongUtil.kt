package wind.maimusic.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import wind.maimusic.Constants
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
    fun saveSong(song: Song) {
        try {
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
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.e("JG","写入对象error!")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("JG","写入对象error!")
        }
    }

    fun getSong(): Song? {
        try {
            val input = FileInputStream("${Constants.currentSongUrl()}/song.txt")
            val ois = ObjectInputStream(input)
            return ois.readObject() as Song //返回对象
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.e("JG","读取对象error!")
            return null
        } catch (e: IOException) {
            Log.e("JG","读取对象error!")
            e.printStackTrace()
            return null
        } catch (e: ClassNotFoundException) {
            Log.e("JG","读取对象error!")
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
                "maimusic"
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
    fun loadLrcText(songName:String) :String? {
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
    fun assemblySong(s: Any,songType:Int):Song {
        var song:Song?=null
        when(songType) {
            SONG_FIRST_MEET->{
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
                }
            }
            SONG_LOCAL->{

            }
            SONG_HISTORY->{

            }
            SONG_DOWNLOAD->{

            }
            SONG_LOVE->{

            }
            SONG_ONLINE->{
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
    const val SONG_FIRST_MEET =0
    const val SONG_LOCAL = 1
    const val SONG_HISTORY = 2
    const val SONG_DOWNLOAD =3
    const val SONG_LOVE = 4
    const val SONG_ONLINE =5
}
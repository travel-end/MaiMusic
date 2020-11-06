package wind.maimusic.utils

import android.graphics.Bitmap
import android.widget.ImageView
import wind.maimusic.Constants
import wind.maimusic.model.HistorySong
import wind.maimusic.model.LocalSong
import wind.maimusic.model.LoveSong
import wind.maimusic.model.OnlineSong
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
    fun saveSongCover(bitmap: Bitmap?, singer: String): Boolean {
        if (bitmap != null) {
            val file = File(Constants.coverImgUrl())
            if (!file.exists()) {
                file.mkdirs()
            }
            val singerImgFile = File(file, "$singer.jpg")
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(singerImgFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
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
        val mS: String
        mS = if (singer.contains("/")) {
            val s = singer.split("/")
            s[0].trim()
        } else {
            singer.trim()
        }
        val imgUrl = "${Constants.coverImgUrl()}$mS.jpg"
        LogUtil.e("本地音乐封面路径：$imgUrl")
        v.loadImg(imgUrl, placeholder = R.drawable.disk, error = R.drawable.disk)
    }

    /**
     * 保存歌词到本地
     */
    fun saveLrcText(lrc: String, songName: String) {
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
            val lrcFile = File(file, "$name.lrc")
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
    fun loadLocalLrcText(songName: String): String? {
        return try {
            val fileReader = FileReader("${Constants.lrcTextUrl()}$songName.lrc")
            val bufferReader = BufferedReader(fileReader)
            val lrc = StringBuilder()
            while (true) {
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

    fun assemblySong(s: Any, songType: Int, pos: Int = 0): Song {
        var song: Song? = null
        when (songType) {
            Consts.ONLINE_FIRST_LAUNCH -> {
                val launchSong = s as OnlineSong
                song = Song().apply {
                    songId = launchSong.songId //004DrG5A2nm7q2
                    singer = launchSong.singer// 鸾音社
                    songName = launchSong.name// 夜来寒雨晓来风
                    imgUrl = launchSong.imgUrl
                    duration = launchSong.duration ?: 0//187  (秒)
                    isOnline = launchSong.isOnline
                    mediaId = launchSong.mediaId//004DrG5A2nm7q2
                    albumName = launchSong.albumName//夜来寒雨晓来风
                    isDownload = launchSong.isDownload
                    listType = Consts.LIST_TYPE_ONLINE
                    onlineSubjectType = Consts.ONLINE_FIRST_LAUNCH
                    position = pos
                }
            }
            Constants.ST_DAILY_RECOMMEND->{
                val recommendSong = s as OnlineSong
                song = Song().apply {
                    songId = recommendSong.songId //004DrG5A2nm7q2
                    singer = recommendSong.singer// 鸾音社
                    songName = recommendSong.name// 夜来寒雨晓来风
                    imgUrl = recommendSong.imgUrl
                    duration = recommendSong.duration ?: 0//187  (秒)
                    isOnline = recommendSong.isOnline
                    mediaId = recommendSong.mediaId//004DrG5A2nm7q2
                    albumName = recommendSong.albumName//夜来寒雨晓来风
                    isDownload = recommendSong.isDownload
                    listType = Consts.LIST_TYPE_ONLINE
                    onlineSubjectType = Constants.ST_DAILY_RECOMMEND
                    position = pos
                }
            }
            Consts.LIST_TYPE_LOCAL -> {
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
            Consts.LIST_TYPE_HISTORY -> {
                val historySong = s as HistorySong
                song = Song().apply {
                    songId = historySong.songId
                    songName = historySong.name
                    singer = historySong.singer
                    isOnline = historySong.isOnline
                    url = historySong.url
                    imgUrl = historySong.pic
                    position = pos
                    duration = historySong.duration ?: 0
                    listType = Consts.LIST_TYPE_HISTORY
                    mediaId = historySong.mediaId
                }
            }
            Consts.LIST_TYPE_DOWNLOAD -> {
            }
            Consts.LIST_TYPE_LOVE -> {
                val love = s as LoveSong
                song  = Song().apply {
                    songId = love.songId
                    qqId = love.qqId
                    songName = love.name
                    singer = love.singer
                    isOnline = love.isOnline ?: false
                    url = love.url
                    imgUrl = love.pic
                    position = pos
                    duration = love.duration ?: 0
                    listType = Consts.LIST_TYPE_LOVE
                    mediaId = love.mediaId
                }
            }
            Consts.LIST_TYPE_ONLINE -> {
                val online = s as OnlineSong
                song = Song().apply {
                    songId = online.songId //004DrG5A2nm7q2
                    singer = online.singer// 鸾音社
                    songName = online.name// 夜来寒雨晓来风
                    imgUrl =online.imgUrl
                    duration = online.duration?:0//187  (秒)
                    isOnline = true
                    mediaId = online.mediaId//004DrG5A2nm7q2
                    albumName = online.albumName//夜来寒雨晓来风
                    isDownload = online.isDownload
                    listType = Consts.LIST_TYPE_ONLINE
                }
            }
            Consts.ONLINE_SEARCH->{
                val searchSong = s as ListBean
                song = Song().apply {
                    songId = searchSong.songmid
                    singer = StringUtil.getSinger(searchSong)
                    songName = searchSong.songname
                    imgUrl = "${Consts.ALBUM_PIC}${searchSong.albummid}${Consts.JPG}"
                    duration = searchSong.interval
                    isOnline = true
                    listType = Consts.LIST_TYPE_ONLINE
                    onlineSubjectType = Consts.ONLINE_SEARCH
                    mediaId = searchSong.strMediaMid
                    isDownload = DownloadedUtil.hasDownloadedSong(searchSong.songmid?:"")
                    position = 0
                }
            }
        }
        return song!!
    }
}
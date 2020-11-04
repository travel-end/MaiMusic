package wind.maimusic.utils

import com.google.gson.Gson
import wind.maimusic.MaiApp
import wind.maimusic.model.firstmeet.FirstMeet
import wind.maimusic.model.firstmeet.FirstMeetSong
import wind.maimusic.model.listensong.*
import wind.maimusic.room.database.OnlineSongDatabase
import wind.widget.cost.Consts
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * @By Journey 2020/10/16
 * @Description
 */
object AssetsUtil {
    private val gson by lazy {
        Gson()
    }

    /**
     * 读取assets目录下的任意文件
     */
    fun readAssetsFile(fileName: String): String? {
        val content: String?
        return try {
            val input = MaiApp.getInstance().applicationContext.assets.open(fileName)
            val size = input.available()
            val buffer = ByteArray(size)
            input.read(buffer)
            input.close()
            content = String(buffer)
            LogUtil.e("读取asset文件内容：$content")
            content
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtil.e("读取assets文件出错!!!--> error:${e.message}")
            null
        }
    }

    /**
     * 读取一条json
     */
    @Throws(Exception::class)
    private fun readAssetsJson(jsonName: String): String? {
        val builder = StringBuilder()
//        return try {
        val inputReader =
            InputStreamReader(MaiApp.getInstance().applicationContext.resources.assets.open(jsonName))
        val bufReader = BufferedReader(inputReader)
        // 只读一行
        var line: String?
        while (bufReader.readLine().also { line = it } != null) {
            builder.append(line)
        }
        return builder.toString()
//        } catch (e:Exception) {
//            e.printStackTrace()
//            null
//        }
    }

    fun loadListenSongData(): ListenSong? {
        val jsonData = readAssetsJson("listen_song.json")
        if (jsonData != null) {
            return gson.fromJson(jsonData, ListenSong::class.java)
        }
        return null
    }

    fun initAppData() {
        GlobalUtil.async {
            val dbDao = OnlineSongDatabase.getDatabase()
            val songs = loadFirstMeetSongs()
            if (isNotNullOrEmpty(songs)) {
                dbDao.firstMeetSongDao().addFirstMeetSongList(songs!!)
            }
            val bannerJson = readAssetsJson("banner.json")
            val banner = gson.fromJson(bannerJson, Banner::class.java)
            val songListCover =
                gson.fromJson(readAssetsJson("song_list_cover.json"), SongListCovers::class.java)
            val singleSong =
                gson.fromJson(readAssetsJson("single_song.json"), SingleSongList::class.java)
            val poetrySong =
                gson.fromJson(readAssetsJson("poetry_song.json"), PoetrySongList::class.java)
            dbDao.listenBannerDao().addListenBanners(banner.bannerList)
            dbDao.songListCoverDao().addSongCovers(songListCover.listCovers)
            dbDao.singleSongDao().addSingleSongs(singleSong.singleSongList)
            dbDao.poetrySongDao().addPoetrySongs(poetrySong.poetrySongList)
        }
    }

    /**
     * 度json 数组
     */
    fun loadFirstMeetSongs(): MutableList<FirstMeetSong>? {
        val builder = StringBuilder()
        val list = mutableListOf<FirstMeetSong>()
        try {
            val inputReader =
                InputStreamReader(MaiApp.getInstance().applicationContext.resources.assets.open("first_meet_song.json"))
            val bufReader = BufferedReader(inputReader)
            // 只读一行
            var line: String?
            while (bufReader.readLine().also { line = it } != null) {
                builder.append(line)
            }
            val result = gson.fromJson(builder.toString(), FirstMeet::class.java)
//            LogUtil.e("查询json文件的推荐歌曲数目：${result.firstMeetList.size}")
            result?.let {
                val firstMeetList = it.firstMeetList
                for (r in firstMeetList) {
                    val song = FirstMeetSong().apply {
                        songId = r.songmid
                        singer = StringUtil.getSinger(r)
                        songName = r.songname
                        imgUrl = "${Consts.ALBUM_PIC}${r.albummid}${Consts.JPG}"
                        duration = r.interval
                        isOnline = true
                        mediaId = r.strMediaMid
                        songmid = r.songmid
                        albumName = r.albumname
                        isDownload =
                            DownloadedUtil.hasDownloadedSong(r.songmid ?: "")//003IHI2x3RbXLS
                    }
                    list.add(song)
                }
                return list
            }
            return null
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
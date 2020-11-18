package wind.maimusic.utils

import com.google.gson.Gson
import wind.maimusic.MaiApp
import wind.maimusic.model.OnlineSong
import wind.maimusic.model.OnlineSongList
import wind.maimusic.model.firstmeet.FirstMeet
import wind.maimusic.model.firstmeet.FirstMeetSong
import wind.maimusic.model.listensong.*
import wind.maimusic.model.searchhot.Search
import wind.maimusic.model.singer.SingerList
import wind.maimusic.room.database.MaiDatabase
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
        val inputReader =
            InputStreamReader(MaiApp.getInstance().applicationContext.resources.assets.open(jsonName))
        val bufReader = BufferedReader(inputReader)
        // 只读一行
        var line: String?
        while (bufReader.readLine().also { line = it } != null) {
            builder.append(line)
        }
        return builder.toString()
    }
    fun initSongAndSinger() {
        GlobalUtil.async {
            val singerDao = OnlineSongDatabase.getDatabase().singerDao()
            val singList = gson.fromJson(readAssetsJson("singer.json"),SingerList::class.java)
            singerDao.addSingers(singList.chineseSingers)
            val dbDao = OnlineSongDatabase.getDatabase().onlineSongDao()
            /*所有在线音乐（app推荐）*/
            val dbSongList = mutableListOf<OnlineSong>()
            try {
                val onlineSongList = gson.fromJson(readAssetsJson("song_list.json"),OnlineSongList::class.java)
                if (onlineSongList != null) {
                    dbSongList.clear()
                    val songList = onlineSongList.maiMusicSongList
                    for (bean in songList) {
                        val onlineSong = OnlineSong().apply {
                            songId = bean.songmid
                            singer = StringUtil.getSinger(bean)
                            name = bean.songname
                            imgUrl = "${Consts.ALBUM_PIC}${bean.albummid}${Consts.JPG}"
                            duration = bean.interval
                            isOnline = true
                            mediaId = bean.strMediaMid
                            songmid = bean.songmid
                            albumName = bean.albumname
                            isDownload = DownloadedUtil.hasDownloadedSong(bean.songmid ?: "")//003IHI2x3RbXLS
                            mainType = bean.mainType
                            secondType = bean.secondType
                            isPoetrySong = bean.isPoetrySong
                            poetrySongId = bean.poetrySongId
                            singerId = bean.singerId
                        }
                        dbSongList.add(onlineSong)
                    }
                    dbDao.addOnlineSongs(dbSongList)
                }
            } catch (e:IOException) {
                e.printStackTrace()
            } catch (e:Exception) {
                e.printStackTrace()
            }
        }
    }


    // TODO: 2020/11/4 assets下的数据只在第一次登录的时候使用，考虑将其放到app之外（或者转移数据库）
    fun initOtherData() {
        GlobalUtil.execute { // TODO: 2020/11/5 分两步：先同步存储首页的数据，为了能更快的进入首页，然后后面的大数据使用异步存储
            val dbDao = OnlineSongDatabase.getDatabase()
            val searchDao = MaiDatabase.getDatabase().searchSongDao()
            try {
                /*储存的banner*/
                val banner = gson.fromJson(readAssetsJson("banner.json"), Banner::class.java)
                /*储存的歌单封面*/
                val classifySongList = gson.fromJson(readAssetsJson("song_list_cover.json"), ClassifySongList::class.java)
                val allSongList = mutableListOf<SongListCover>()
                allSongList.addAll(classifySongList.recommend)
                allSongList.addAll(classifySongList.chinese)
                allSongList.addAll(classifySongList.pure)
                allSongList.addAll(classifySongList.japanese)
                allSongList.addAll(classifySongList.english)
                allSongList.addAll(classifySongList.antique)
                allSongList.addAll(classifySongList.folk)
                allSongList.addAll(classifySongList.classic)
                allSongList.addAll(classifySongList.quite)
                allSongList.addAll(classifySongList.cure)
                dbDao.listenBannerDao().addListenBanners(banner.bannerList)
                dbDao.songListCoverDao().addSongCovers(allSongList)
                /*搜索页面内容（推荐搜索、热搜）*/
                val search = gson.fromJson(readAssetsJson("search.json"),Search::class.java)
                if (search!= null) {
                    searchDao.addRecommendSearch(search.recommendSearch)
                    searchDao.addHotSearchSongs(search.hotSearchSong)
                }
            } catch (e:IOException) {
                e.printStackTrace()
                LogUtil.e("-----AssetsUtil-----initAppData IOException:${e.message}")
            } catch (e:Exception) {
                e.printStackTrace()
                LogUtil.e("-----AssetsUtil-----initAppData Exception:${e.message}")
            }
        }
    }

    /**
     * 度json 数组
     */
    private fun loadFirstMeetSongs(): MutableList<FirstMeetSong>? {
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
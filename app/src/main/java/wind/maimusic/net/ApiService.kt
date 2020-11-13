package wind.maimusic.net

import retrofit2.http.*
import wind.maimusic.Constants
import wind.maimusic.model.core.*

/**
 * @By Journey 2020/9/26
 * @Description
 */
interface ApiService {
    /**
     * 搜索歌曲：https://c.y.qq.com/soso/fcgi-bin/client_search_cp?p=2&n=2&w=周杰伦&format=json
     * n为一页?首
     */
    @GET("soso/fcgi-bin/client_search_cp?n=${Constants.SEARCH_SONG_PAGE_SIZE}&format=json")
    suspend fun search(@Query("w") searchContent:String,@Query("p") offSet:Int): SearchSong

    /**
     * 搜索专辑：https://c.y.qq.com/soso/fcgi-bin/client_search_cp?p=1&n=2&w=林宥嘉&format=json&t=8
     * @param seek 搜索关键字
     * @param offset 页数
     */
    @GET("soso/fcgi-bin/client_search_cp?n=20&format=json&t=8")
    suspend fun searchAlbum(@Query("w") searchContent:String,@Query("p") offSet:Int):Album

    /**
     * 专辑详细：https://c.y.qq.com/v8/fcg-bin/fcg_v8_album_info_cp.fcg?albummid=004YodY33zsWTT&format=json
     * @param id 专辑mid
     */
    @GET("v8/fcg-bin/fcg_v8_album_info_cp.fcg?format=json")
    suspend fun getAlbumSong(@Query("albummid") id:String):AlbumSong

    /**
     * 得到歌曲的播放地址，变化的只有songmid，即{}所示
     * https://u.y.qq.com/cgi-bin/musicu.fcg?format=json&data=%7B%22req_0%22%3A%7B%22module%22%3A%22vkey.GetVkeyServer%22%2C%22method%22%3A%22CgiGetVkey%22%2C%22param%22%3A%7B%22guid%22%3A%22358840384%22%2C%22       +
     * songmid%22%3A%5B%22{003wFozn3V3Ra0} +
     * %22%5D%2C%22songtype%22%3A%5B0%5D%2C%22uin%22%3A%221443481947%22%2C%22loginflag%22%3A1%2C%22platform%22%3A%2220%22%7D%7D%2C%22comm%22%3A%7B%22uin%22%3A%221443481947%22%2C%22format%22%3A%22json%22%2C%22ct%22%3A24%2C%22cv%22%3A0%7D%7D
     */
    @GET("cgi-bin/musicu.fcg?format=json")
    suspend fun getSongUrl(@Query(value = "data",encoded = true) songUrl:String):SongUrl

    /**
     * 根据songmid获取歌词：https://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric_new.fcg?songmid=000wocYU11tSzS&format=json&nobase64=1
     * headers中的Referer是qq用来防盗链的
     */
    // 得到歌词需要添加Referer的表头
    @Headers("Referer:https://y.qq.com/portal/player.html")
    @GET("https://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric_new.fcg?format=json&nobase64=1")//根据qq音乐的mid获取歌词
    suspend fun getOnlineSongLrc(@Query("songmid") songId:String): OnlineSongLrc?


    @GET("soso/fcgi-bin/client_search_cp?p=1&n=30&format=json&t=7")
    suspend fun getLrc(@Query("w") lrcKeyWord:String):SongLrc

    /**
     * 得到歌手照片 主要用于本地音乐 http://music.163.com/api/search/get/web?s=刘瑞琦&type=100
     */
    @Headers("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36")
    @POST("api/search/get/web?csrf_token=&type=100")
    @FormUrlEncoded
    suspend fun getLocalSingerImg(@Field("s") singer:String):SingerImg

}
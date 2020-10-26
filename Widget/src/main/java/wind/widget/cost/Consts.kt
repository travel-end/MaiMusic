package wind.widget.cost

import android.content.Context

/**
 * @By Journey 2020/10/26
 * @Description
 */
object Consts {
    const val LOG_TAG = "maiMusic"

    // 封面图片的路径
    fun coverImgUrl(context: Context):String {
        return "${context.getExternalFilesDir("coverImg").toString()}/maimusic/coverimg/"
    }
    // 下载歌曲的路径 todo 使用共享路径 保证app卸载后歌曲不会被删除
    fun downloadSongUrl(context: Context):String {
        return "${context.getExternalFilesDir("downloadSong").toString()}/maimusic/downloadsong/"
    }
    // 保存歌词的路径
    fun lrcTextUrl(context: Context):String {
        return "${context.getExternalFilesDir("lrcText").toString()}/maimusic/lrctext"
    }

    // 当前播放歌曲的路径
    fun currentSongUrl(context: Context):String {
        return "${context.getExternalFilesDir("song").toString()}/maimusic"
    }

    //根据歌手获取歌手图片的baseUrl
    const val SINGER_PIC_BASE_URL = "http://music.163.com/"
    const val SINGER_PIC = "api/search/get/web?csrf_token=&type=100"
    // Fiddler抓包qq音乐网站后的地址
    const val FIDDLER_BASE_QQ_URL = "https://c.y.qq.com/"
    //获取播放地址的baseUrl
    const val FIDDLER_BASE_SONG_URL = "https://u.y.qq.com/"
    // 获取歌手图片需要添加user-agent的表头
    const val HEADER_USER_AGENT = "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36"

    // 专辑照片
    const val ALBUM_PIC="http://y.gtimg.cn/music/photo_new/T002R180x180M000"
    const val JPG=".jpg"
    const val SONG_URL_DATA_LEFT="%7B%22req_0%22%3A%7B%22module%22%3A%22vkey.GetVkeyServer%22%2C%22method%22%3A%22CgiGetVkey%22%2C%22param%22%3A%7B%22guid%22%3A%22358840384%22%2C%22songmid%22%3A%5B%22"
    const val SONG_URL_DATA_RIGHT="%22%5D%2C%22songtype%22%3A%5B0%5D%2C%22uin%22%3A%221443481947%22%2C%22loginflag%22%3A1%2C%22platform%22%3A%2220%22%7D%7D%2C%22comm%22%3A%7B%22uin%22%3A%221443481947%22%2C%22format%22%3A%22json%22%2C%22ct%22%3A24%2C%22cv%22%3A0%7D%7D"

    //根据qq音乐的mid获取歌词
    const val ONLINE_SONG_LRC = "https://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric_new.fcg?format=json&nobase64=1"
    const val SONG_ID_UNFIND = "暂无歌词"
    // 播放顺序
    const val KEY_PLAY_MODE = "play_mode"
    const val PLAY_ORDER = 0 // 顺序播放
    const val PLAY_RANDOM = 1// 随机播放
    const val PLAY_SINGER = 2//单曲循环
    // 播放状态
    const val SONG_STATUS_CHANGE = "song_status_change"
    const val SONG_PLAY = 0
    const val SONG_PAUSE = 1
    const val SONG_RESUME = 2// ->song start
    const val SONG_CHANGE = 3
    // 播放列表
    const val LIST_TYPE_LOCAL=1//本地列表
    const val LIST_TYPE_ONLINE=2//专辑列表
    const val LIST_TYPE_LOVE=3//我的收藏列表
    const val LIST_TYPE_HISTORY=4//最近播放列表
    const val LIST_TYPE_DOWNLOAD=5//下载列表

    const val EVENT_LIST_TYPE = "event_list_type"

    const val PLAY_STATUS = "play_status"

    // 网络与非网络歌曲
    const val SONG_ONLINE = 0
    const val SONG_LOCAL= 1

    // 最近播放最多歌曲数
    const val HISTORY_MAX_SIZE = 100

    // 正在下載歌曲列表的狀態
    const val DOWNLOAD_EVENT = "download_event"
    const val DOWNLOAD_PAUSED = 0
    const val DOWNLOAD_WAIT = 1
    const val DOWNLOAD_ING = 2
    const val DOWNLOAD_READY = 3

    const val TYPE_DOWNLOADING = 0
    const val TYPE_DOWNLOAD_PAUSED = 1
    const val TYPE_DOWNLOAD_CANCELED = 2
    const val TYPE_DOWNLOAD_SUCCESS = 3
    const val TYPE_DOWNLOAD_FAILED = 4
    const val TYPE_DOWNLOADED = 5
    const val TYPE_DOWNLOAD_ADD = 6


    // 普通事件传递
    const val EVENT_REFRESH_LOCAL_SONG = 0
    const val EVENT_APP = "event_app"
    const val FIRST_LAUNCH = "first_launch"
    const val EVENT_OPEN_DRAWER = 0
    const val EVENT_REFRESH_HOME_DATA= 1

    const val PLAY_SUCCESS = 1
    const val PLAY_FAILED = 2

    const val ADD_TO_LOVE = "add_to_love"

    // song list type
    const val SONG_LIST_TYPE_LOVE = 1
}
package wind.maimusic

import wind.maimusic.utils.getStringRes
import wind.widget.cost.Consts


/**
 * @By Journey 2020/10/25
 * @Description
 */
object Constants {
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


    // 封面图片的路径
    fun coverImgUrl():String {
        return "${MaiApp.getInstance().getExternalFilesDir("coverImg").toString()}/maimusic/coverimg/"
    }
    // 下载歌曲的路径 todo 使用共享路径 保证app卸载后歌曲不会被删除
    fun downloadSongUrl():String {
        return "${MaiApp.getInstance().getExternalFilesDir("downloadSong").toString()}/maimusic/downloadsong/"
    }
    // 保存歌词的路径
    fun lrcTextUrl():String {
        return "${MaiApp.getInstance().getExternalFilesDir("lrcText").toString()}/maimusic/lrctext"
    }

    // 当前播放歌曲的路径
    fun currentSongUrl():String {
        return "${MaiApp.getInstance().getExternalFilesDir("song").toString()}/maimusic"
    }
    const val HOT_SEARCH = "hot_search"
    const val SONG_LIST_TYPE = "song_list_type"
    const val KEY_SEARCH_CONTENT= "key_search_content"
    const val TOOLBAR_MAX_OFFSET = 150
    const val SEARCH_SONG_PAGE_SIZE = 25
    const val TEMP_SONG_COVER1_NORMAL = "http://p1.music.126.net/qTDkcmWPMK3U54RNC0IgMw==/109951163288035254.jpg"
    const val TEMP_SONG_COVER2_SMALL = "${Consts.ALBUM_PIC}001Jegbz00ePg5${Consts.JPG}"// 小图
    const val TEMP_SONG_COVER3_BIG = "https://p4.music.126.net/1XfoEJK6dQ2TEw55eXZLfA==/109951165418603915.jpg"//大图

    const val TEMP_BANNER1 = "http://p1.music.126.net/ADoIa_dS6CQ5fpUrwxkZ4g==/109951165433749064.jpg?imageView&quality=89"
    const val TEMP_BANNER2 = "http://p1.music.126.net/KYCSrOuaRhLp9-a4Dntc4g==/109951165433368451.jpg?imageView&quality=89"

    /**
     * 歌单类型 具体的名字需要在定义(名字为一句诗 配上一到两个标签,贴在每个歌单的封面 如古风、民谣等)
     * 每首歌对应两个歌单名称，比如'旧梦一场' 对应华语热歌和网络热歌
     */
    const val ST_DEFAULT= 0//默认的歌单 对于一些没有mainType和secondType的都放到默认歌单中
    const val ST_DAILY_RECOMMEND = 21// 每日推荐
    const val ST_DAILY_HOT_SONG = 20



    const val ST_CHINESE = 1// 华语精选
    const val ST_PURE = 2// 纯音乐
    const val ST_JAPANESE = 3// 日语
    const val ST_ENGLISH = 4// 英语
    const val ST_ANTIQUE = 5// 古风
    const val ST_FOLK = 6// 民谣
    const val ST_CLASSIC = 7// 经典老歌
    const val ST_QUITE = 8// 安静
    const val ST_CURE = 9// 治愈


    const val ST_TAG1 = "古风"
    const val ST_TAG2 = "民谣"
    const val ST_TAG3 = "轻音乐"
    const val ST_TAG4 = "安静"


    const val PAGE_SIZE_BANNER = 4
    const val PAGE_SIZE_SONG_LIST_COVER = 5
    const val PAGE_SIZE_SINGLE_SONG = 3
    const val PAGE_SIZE_POETRY_SONG = 6
    const val PAGE_SIZE_DAILY_RECOMMEND = 6
    const val PAGE_SIZE_RECOMMEND_BOOK = 2

    const val FIRST_LAUNCH = "first_launch"
    const val DAY_OF_WEEK ="day_of_week"
    const val THE_DAY_STEP = "the_day_step"

    /*每日可变的数据 保持一天都不变的数据 过了当天就更换*/
    const val DAILY_BANNER = 0
    const val DAILY_RECOMMEND_SONG =1
    const val DAILY_HOT_SONG = 2
    const val DAILY_RECOMMEND_BOOK = 3
    const val DAILY_LIST_COVER = 4
    const val DAILY_POETRY_SONG = 5

    val tabClassifyName = listOf(
        R.string.chinese.getStringRes(),
        R.string.quite.getStringRes(),
        R.string.antique.getStringRes(),
        R.string.english.getStringRes(),
        R.string.japanese.getStringRes(),
        R.string.classic.getStringRes(),
        R.string.pure_music.getStringRes(),
        R.string.folk.getStringRes(),
        R.string.cure.getStringRes()
    )
}
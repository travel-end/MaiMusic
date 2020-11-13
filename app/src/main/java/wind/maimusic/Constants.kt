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


    // 单曲封面图片的路径
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
    const val ALBUM_ID= "album_id"
    const val ALBUM_NAME = "album_name"
    const val ALBUM_SINGER = "album_singer"
    const val ALBUM_COVER = "album_cover"
    const val ALBUM_PUBLIC_TIME = "album_public_time"


    const val KEY_SEARCH_CONTENT= "key_search_content"
    const val TOOLBAR_MAX_OFFSET = 150
    const val SEARCH_SONG_PAGE_SIZE = 20
    const val TEMP_SONG_COVER1_NORMAL = "http://p1.music.126.net/qTDkcmWPMK3U54RNC0IgMw==/109951163288035254.jpg"
    const val TEMP_SONG_COVER2_SMALL = "${Consts.ALBUM_PIC}001Jegbz00ePg5${Consts.JPG}"// 小图
    const val TEMP_SONG_COVER3_BIG = "https://p4.music.126.net/1XfoEJK6dQ2TEw55eXZLfA==/109951165418603915.jpg"//大图


    const val TEMP_BANNER1 = "http://p1.music.126.net/ADoIa_dS6CQ5fpUrwxkZ4g==/109951165433749064.jpg?imageView&quality=89"
    const val TEMP_BANNER2 = "http://p1.music.126.net/KYCSrOuaRhLp9-a4Dntc4g==/109951165433368451.jpg?imageView&quality=89"

    const val MSG_CODE = 1
    const val MALE = 0
    const val FAMELE = 1
    const val HOT_SINGER = 2
    const val OTHER_SINGER =3

    /**
     * 歌单类型 具体的名字需要在定义(名字为一句诗 配上一到两个标签,贴在每个歌单的封面 如古风、民谣等)
     * 每首歌对应两个歌单名称，比如'旧梦一场' 对应华语热歌和网络热歌
     */
    const val ST_DEFAULT= 0//默认的歌单 对于一些没有mainType和secondType的都放到默认歌单中
    const val ST_DAILY_RECOMMEND = 21// 每日推荐
    const val ST_ALBUM_SONG_LIST = 22
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

    /*六位推荐歌手*/
    const val SINGER_JAY_CHOU = 1
    const val SINGER_XUE = 5
    const val SINGER_EASON = 2
    const val SINGER_DENG_ZQ = 100
    const val SINGER_REN_RAN = 101
    const val SINGER_WAIT_JUN = 107


    const val IMG_JAY_CHOU_1 = "https://cdnmusic.migu.cn/picture/2019/1031/1528/AM8fc7d10693eb4b658a481781cbc16d6a.jpg"
    const val IMG_JAY_CHOU_2 = "https://cdnmusic.migu.cn/picture/2020/0611/1054/ARTMefb7b84e00cfb7692ea2d3eaafd8b09f.jpg"
    const val IMG_JAY_CHOU_3 = "https://cdnmusic.migu.cn/picture/2020/0612/0000/AM977e777d850e857ef41d7a457021e6c9.jpg"
    const val IMG_JAY_CHOU_4 = "https://cdnmusic.migu.cn/picture/2019/1031/0115/AM5e4e490819324ef38e3f335b5feea38d.jpg"
    const val IMG_JAY_CHOU_5 = "https://cdnmusic.migu.cn/picture/2020/0918/1728/AM1875dd224f9487154de50fdd20137052.jpg"
    const val IMG_JAY_CHOU_6 = "https://cdnmusic.migu.cn/picture/2019/1031/0114/AM7be67f4731fd4ccb9a95b42f7875dd2b.jpg"
    val jayChouImgs = arrayOf(
        IMG_JAY_CHOU_1,
        IMG_JAY_CHOU_2,
        IMG_JAY_CHOU_3,
        IMG_JAY_CHOU_4,
        IMG_JAY_CHOU_5,
        IMG_JAY_CHOU_6
    )


    const val IMG_XUE_1 = "https://cdnmusic.migu.cn/picture/2019/1031/0324/AM6fa11f9630ec45a38d8de73d1f075f3b.jpg"
    const val IMG_XUE_2 = "https://cdnmusic.migu.cn/picture/2020/0731/0324/AMee13f50a6f9240804c1b3fe6e802aff8.jpg"
    const val IMG_XUE_3 = "https://cdnmusic.migu.cn/picture/2019/1031/0314/AM67d96d6eebf64b13acef4f9380537287.jpg"
    const val IMG_XUE_4 = "https://cdnmusic.migu.cn/picture/2019/1031/1035/AM9b799e13105e4523aa8df662fff2fdaf.jpg"
    const val IMG_XUE_5 = "https://cdnmusic.migu.cn/picture/2019/1031/0252/AM3d9e461f1dde4e829c72175fd5bef8b2.jpg"
    const val IMG_XUE_6 = "https://cdnmusic.migu.cn/picture/2020/0515/1800/AM2c2103b25dbb02b6be71801dec6e7182.jpg"
    val xueImgs = arrayOf(
        IMG_XUE_1,
        IMG_XUE_2,
        IMG_XUE_3,
        IMG_XUE_4,
        IMG_XUE_5,
        IMG_XUE_6
    )


    const val IMG_EASON_1 = "https://cdnmusic.migu.cn/picture/2019/1128/1714/AMcbe740ed5b4341f09951a17ef7d6f202.jpg"
    const val IMG_EASON_2 = "https://cdnmusic.migu.cn/picture/2019/1113/0949/AM200519e98ebe4c828edbfcf2edda85a1.jpg"
    const val IMG_EASON_3 = "https://cdnmusic.migu.cn/picture/2020/0813/0307/AM78a35c2c43f7901718275548e4d2d0d2.jpg"
    const val IMG_EASON_4 = "https://cdnmusic.migu.cn/picture/2020/1030/0321/AM84bde70f8257781831fe991060742ce8.jpg"
    const val IMG_EASON_5 = "https://cdnmusic.migu.cn/picture/2020/1031/0314/AMdcef963373f6e0d73ed582e2cb17a28c.jpg"
    const val IMG_EASON_6 = "https://cdnmusic.migu.cn/picture/2020/0926/0324/AMc0aebd3c75c3a255a8159204f3782887.jpg"
    val eaSonImgs = arrayOf(
        IMG_EASON_1,
        IMG_EASON_2,
        IMG_EASON_3,
        IMG_EASON_4,
        IMG_EASON_5,
        IMG_EASON_6
    )



    const val IMG_DENG_ZQ_1 = "https://cdnmusic.migu.cn/picture/2020/0316/1349/AMb53926db54c66c96dc26c6c4ba721c99.jpg"
    const val IMG_DENG_ZQ_2 = "https://cdnmusic.migu.cn/picture/2020/0316/1407/AMf63f25a8980d719e533d305b1de55a6e.jpg"
    const val IMG_DENG_ZQ_3 = "https://cdnmusic.migu.cn/picture/2019/0510/0856/AMdaf805b05cae4648921499075e061b28.jpg"
    const val IMG_DENG_ZQ_4 = "https://cdnmusic.migu.cn/picture/2020/0316/1349/AM0a78b1b019624c77345b78170f218399.jpg"
    const val IMG_DENG_ZQ_5 = "https://cdnmusic.migu.cn/picture/2020/0316/1407/AMcd2cb7d7f05806fdc6ce6572051e08bc.jpg"
    const val IMG_DENG_ZQ_6 = "https://cdnmusic.migu.cn/picture/2020/0519/1728/AMa95f65ad40ec17dbb01cc1dccd54e4ad.jpg"
    val dengZqImgs = arrayOf(
        IMG_DENG_ZQ_1,
        IMG_DENG_ZQ_2,
        IMG_DENG_ZQ_3,
        IMG_DENG_ZQ_4,
        IMG_DENG_ZQ_5,
        IMG_DENG_ZQ_6
    )

    const val REN_RAN_1 = "https://cdnmusic.migu.cn/picture/2019/0523/0842/AMda1880f666f249319309d0c6ccd92133.jpg"
    const val REN_RAN_2 = "https://cdnmusic.migu.cn/picture/2019/0523/0842/AM8f33663f926e4c13b9543b229ef663b8.jpg"
    const val REN_RAN_3 = "https://cdnmusic.migu.cn/picture/2019/0523/0842/AM8e157948aa1942bd9404f8b4cad0b75d.jpg"
    const val REN_RAN_4 = "https://cdnmusic.migu.cn/picture/2020/0612/1728/AM1f081629156f8fd9ca28bd5333de87f9.jpg"
    const val REN_RAN_5 = "https://cdnmusic.migu.cn/picture/2020/1009/1642/ARTMbbfe89ced045a354490ad9f2a955c3a7.jpg"
    val renRanImgs = arrayOf(
        REN_RAN_1,
        REN_RAN_2,
        REN_RAN_3,
        REN_RAN_4,
        REN_RAN_5
    )

    const val WAIT_JUN_1 = "https://cdnmusic.migu.cn/picture/2020/1023/2100/ARTM38415224de9e5d19e5f22953c45a0c3b.jpg"
    const val WAIT_JUN_2 = "https://cdnmusic.migu.cn/picture/2020/0731/0352/AM43d53eb431b1d6035d577eddcbbeece0.jpg"
    const val WAIT_JUN_3 = "https://bkimg.cdn.bcebos.com/pic/2934349b033b5bb5c9ea5c52c29ac239b6003af3164f?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxMTY=,g_7,xp_5,yp_5"
    const val WAIT_JUN_4 = "https://bkimg.cdn.bcebos.com/pic/b7003af33a87e950352a97c0e4714443fbf2b2111e4f?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxNTA=,g_7,xp_5,yp_5"
    val waitJunImgs = arrayOf(
        WAIT_JUN_1,
        WAIT_JUN_2,
        WAIT_JUN_3,
        WAIT_JUN_4
    )
}
package wind.maimusic

import wind.maimusic.utils.getStringRes
import wind.widget.cost.Consts


/**
 * @By Journey 2020/10/25
 * @Description
 */
object Constants {
    //古诗文网
    const val GUSHI_BASE_URL = "https://app.gushiwen.cn/api/"
    const val GUSHI_MINGJUF = "onehour/Default10.aspx?page=1&token=gswapi"

    //根据歌手获取歌手图片的baseUrl
    const val SINGER_PIC_BASE_URL = "http://music.163.com/"
    const val SINGER_PIC = "api/search/get/web?csrf_token=&type=100"

    // Fiddler抓包qq音乐网站后的地址
    const val FIDDLER_BASE_QQ_URL = "https://c.y.qq.com/"

    //获取播放地址的baseUrl
    const val FIDDLER_BASE_SONG_URL = "https://u.y.qq.com/"

    // 获取歌手图片需要添加user-agent的表头
    const val HEADER_USER_AGENT =
        "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36"

    // 专辑照片
    const val ALBUM_PIC = "http://y.gtimg.cn/music/photo_new/T002R180x180M000"
    const val JPG = ".jpg"
    const val SONG_URL_DATA_LEFT =
        "%7B%22req_0%22%3A%7B%22module%22%3A%22vkey.GetVkeyServer%22%2C%22method%22%3A%22CgiGetVkey%22%2C%22param%22%3A%7B%22guid%22%3A%22358840384%22%2C%22songmid%22%3A%5B%22"
    const val SONG_URL_DATA_RIGHT =
        "%22%5D%2C%22songtype%22%3A%5B0%5D%2C%22uin%22%3A%221443481947%22%2C%22loginflag%22%3A1%2C%22platform%22%3A%2220%22%7D%7D%2C%22comm%22%3A%7B%22uin%22%3A%221443481947%22%2C%22format%22%3A%22json%22%2C%22ct%22%3A24%2C%22cv%22%3A0%7D%7D"

    //根据qq音乐的mid获取歌词
    const val ONLINE_SONG_LRC =
        "https://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric_new.fcg?format=json&nobase64=1"


    // 单曲封面图片的路径
    fun coverImgUrl(): String {
        return "${MaiApp.getInstance().getExternalFilesDir("coverImg")
            .toString()}/maimusic/coverimg/"
    }


    // 下载歌曲的路径 todo 使用共享路径 保证app卸载后歌曲不会被删除
    fun downloadSongUrl(): String {
        return "${MaiApp.getInstance().getExternalFilesDir("downloadSong")
            .toString()}/maimusic/downloadsong/"
    }

    // 保存歌词的路径
    fun lrcTextUrl(): String {
        return "${MaiApp.getInstance().getExternalFilesDir("lrcText").toString()}/maimusic/lrctext"
    }

    // 当前播放歌曲的路径
    fun currentSongUrl(): String {
        return "${MaiApp.getInstance().getExternalFilesDir("song").toString()}/maimusic"
    }

    const val HOT_SEARCH = "hot_search"
    const val SONG_LIST_TYPE = "song_list_type"
    const val SINGER_ID = "singer_id"
    const val SINGER_COVER = "singer_cover"
    const val SINGER_NAME = "singer_name"
    const val ALBUM_ID = "album_id"
    const val ALBUM_NAME = "album_name"
    const val ALBUM_SINGER = "album_singer"
    const val ALBUM_COVER = "album_cover"
    const val ALBUM_PUBLIC_TIME = "album_public_time"
    const val SONG_LIST_ID = "song_list_id"


    const val KEY_SEARCH_CONTENT = "key_search_content"
    const val TOOLBAR_MAX_OFFSET = 150
    const val SEARCH_SONG_PAGE_SIZE = 20
    const val TEMP_SONG_COVER1_NORMAL =
        "http://p1.music.126.net/qTDkcmWPMK3U54RNC0IgMw==/109951163288035254.jpg"
    const val TEMP_SONG_COVER2_SMALL = "${Consts.ALBUM_PIC}001Jegbz00ePg5${Consts.JPG}"// 小图
    const val TEMP_SONG_COVER3_BIG =
        "https://p4.music.126.net/1XfoEJK6dQ2TEw55eXZLfA==/109951165418603915.jpg"//大图


    const val TEMP_BANNER1 =
        "http://p1.music.126.net/ADoIa_dS6CQ5fpUrwxkZ4g==/109951165433749064.jpg?imageView&quality=89"
    const val TEMP_BANNER2 =
        "http://p1.music.126.net/KYCSrOuaRhLp9-a4Dntc4g==/109951165433368451.jpg?imageView&quality=89"

    const val MSG_CODE = 1
    const val MALE = 0
    const val FAMELE = 1
    const val HOT_SINGER = 2
    const val OTHER_SINGER = 3

    const val LOGIN_SUCCESS = "login_success"
    const val PIC_REQUEST_CODE = 0x00000011

    /**
     * 歌单类型 具体的名字需要在定义(名字为一句诗 配上一到两个标签,贴在每个歌单的封面 如古风、民谣等)
     * 每首歌对应两个歌单名称，比如'旧梦一场' 对应华语热歌和网络热歌
     */
    const val ST_DEFAULT = 0//默认的歌单 对于一些没有mainType和secondType的都放到默认歌单中
    const val ST_DAILY_RECOMMEND = 21// 每日推荐
    const val ST_ALBUM_SONG_LIST = 22
    const val ST_DAILY_HOT_SONG = 20

    /**
     * 歌单类型
     */

    const val ST_CHINESE = 1// 华语精选
    const val ST_PURE = 2// 纯音乐
    const val ST_JAPANESE = 3// 日语
    const val ST_ENGLISH = 4// 英语
    const val ST_ANTIQUE = 5// 古风
    const val ST_FOLK = 6// 民谣
    const val ST_CLASSIC = 7// 经典老歌
    const val ST_QUITE = 8// 安静
    const val ST_CURE = 9// 治愈
    const val ST_RECOMMEND = 10// 推荐歌单（8个）

    /**
     * 具体的所有歌单id   mainType == listId 每首歌对应一个或两个歌单
     */
    /*华语*/
    const val SL_21 = 21//世间无限丹青手，一片伤心画不成1
    const val SL_26 = 26//尘世难逢开口笑，菊花须插满头归 1
    const val SL_28 = 28//身无彩凤双飞翼，心有灵犀一点通 1
    const val SL_31 = 31//得成比目何辞死，愿作鸳鸯不羡仙 1
    const val SL_50 = 50//寂寞空庭春欲晚，满地梨花不开门 1
    const val SL_51 = 51// 花径不曾缘客扫，蓬门今始为君开 1
    const val SL_53 = 53//衰兰送客咸阳道，天若有情天亦老 1
    const val SL_58 = 58//曾经沧海难为水，除却巫山不是云 1
    const val SL_61 = 61//玲珑骰子安红豆，入骨相思知不知 1
    const val SL_65 = 65//晓看天色暮看云，行也思君，坐也思君 1
    const val SL_69 = 69//泪眼问花花不语，乱红飞过秋千去 1
    const val SL_71 = 71//伤心桥下春波绿，曾是惊鸿照影来 1
    const val SL_74 = 74//侯门一入深似海，从此萧郎是路人 1
    const val SL_80 = 80//相恨不如潮有信，相思始觉海非深 1
    const val SL_93 = 93//日暮东风怨啼鸟，落花犹似坠楼人 1
    const val SL_95 = 95//多情只有春庭月 犹为离人照落花 1
    const val SL_96 = 96//早知如此绊人心，何如当初莫相识 1
    /*纯音乐*/
    const val SL_4 = 4//黄鹤楼中吹玉笛，江城五月落梅花 2
    const val SL_14 = 14// 一生几许伤心事，不向空门何处销 2
    const val SL_18 = 18//人似秋鸿来有信，事如春梦了无痕 2
    const val SL_30 = 30//行到水穷处，坐看云起时 2
    const val SL_42 = 42// 人生到处知何似，应似飞鸿踏雪泥 2
    const val SL_45 = 45//昔我往矣，杨柳依依。今我来思，雨雪霏霏 2
    const val SL_46 = 46//风雨如晦，鸡鸣不已。既见君子，云胡不喜 2
    const val SL_56 = 56//秋阴不散霜飞晚，留得枯荷听雨声 2
    const val SL_60 = 60//醉后不知天在水，满船清梦压星河 2
    const val SL_83 = 83//众里寻他千百度，蓦然回首，那人却在灯火阑珊处 2
    const val SL_87 = 87//三杯两盏淡酒，怎敌他，晚来风急 2

    /*古风*/
    const val SL_1 = 1//白头宫女在，闲坐说玄宗  5
    const val SL_2 = 2//满堂花醉三千客，一剑霜寒十四州 5
    const val SL_3 = 3//天子呼来不上船，自称臣是酒中仙 5
    const val SL_6 = 6//数声风笛离亭晚，君向潇湘我向秦 5
    const val SL_19 = 19// 正是江南好风景，落花时节又逢君 5
    const val SL_27 = 27//南风知我意，吹梦到西洲 5
    const val SL_34 = 34//秋风生渭水，落叶满长安 5
    const val SL_55 = 55//人面不知何处去，桃花依旧笑春风 5
    const val SL_36 = 36// 杨花落尽子规啼，闻道龙标过五溪 5
    const val SL_43 = 43//孰知不向边庭苦，纵死犹闻侠骨香 5
    const val SL_63 = 63//人生自是有情痴，此恨不关风与月 5
    const val SL_78 = 78//人生不相见，动如参与商 5
    const val SL_81 = 81//一川烟草，满城风絮，梅子黄时雨 5
    const val SL_88 = 88//故国三千里，深宫二十年 5

    /*民谣*/
    const val SL_9 = 9//多少长安名利客，机关算尽不如君 6
    const val SL_11 = 11//欲买桂花同载酒，终不似，少年游 6
    const val SL_13 = 13// 我未成名君未嫁，可能俱是不如人 6
    const val SL_37 = 37//还君明珠双泪垂，恨不相逢未嫁时 6
    const val SL_64 = 64//君埋泉下泥销骨，我寄人间雪满头 6
    const val SL_75 = 75//世情薄，人情恶，雨送黄昏花易落 6
    const val SL_91 = 91//春风十里扬州路，卷上珠帘总不如 6
    const val SL_94 = 94//十年一觉扬州梦，赢得青楼薄幸名 6

    /*经典老歌*/
    const val SL_8 = 8//桃李春风一杯酒，江湖夜雨十年灯 7
    const val SL_20 = 20//浮云一别后，流水十年间 7
    const val SL_23 = 23//种桃道士今何在，前度刘郎今又来 7
    const val SL_49 = 49//月落乌啼霜满天，江枫渔火对愁眠 7
    const val SL_52 = 52//愿君多采撷，此物最相思 7
    const val SL_35 = 35//此夜曲中闻折柳，何人不起故园情 7
    const val SL_59 = 59//人生若只如初见，何事秋风悲画扇 7
    const val SL_67 = 67//青山一道同云雨，明月何曾是两乡 7
    const val SL_72 = 72//物是人非事事休，欲语泪先流 7
    const val SL_79 = 79//诚知此恨人人有，贫贱夫妻百事哀 7
    const val SL_90 = 90//天地肃清堪四望，为君扶病上高台  7

    /*安静*/
    const val SL_5 = 5//柴门闻犬吠，风雪夜归人 8
    const val SL_7 = 7//连雨不知春去，一晴方觉夏深 8
    const val SL_10 = 10//晚来天欲雪，能饮一杯无 8
    const val SL_22 = 22//山僧不解数甲子，一叶落知天下秋 8
    const val SL_41 = 41//竹杖芒鞋轻胜马，谁怕？一蓑烟雨任平生 8
    const val SL_44 = 44//曲径通幽处，禅房花木深 8
    const val SL_47 = 47//不知何处吹芦管，一夜征人尽望乡 8
    const val SL_54 = 54//天阶夜色凉如水，坐看牵牛织女星 8
    const val SL_57 = 57//永忆江湖归白发，欲回天地入扁舟 8
    const val SL_82 = 82//日暮酒醒人已远，满天风雨下西楼 8
    const val SL_85 = 85//悲欢离合总无情，一任阶前，点滴到天明 8

    /*治愈*/
    const val SL_12 = 12//我有一瓢酒，可以慰风尘 9
    const val SL_15 = 15//殷勤昨夜三更雨，又得浮生一日凉 9
    const val SL_17 = 17// 此情可待成追忆，只是当时已惘然 9
    const val SL_24 = 24//清时有味是无能，闲爱孤云静爱僧 9
    const val SL_29 = 29//世界微尘里，吾宁爱与憎 9
    const val SL_33 = 33//空山新雨后，天气晚来秋 9
    const val SL_39 = 39//小舟从此逝，江海寄余生 9
    const val SL_40 = 40//试问岭南应不好，却道，此心安处是吾乡 9
    const val SL_48 = 48//孤舟蓑笠翁，独钓寒江雪 9
    const val SL_62 = 62//君问归期未有期，巴山夜雨涨秋池 9
    const val SL_68 = 68//江南无所有，聊赠一枝春 9
    const val SL_76 = 76//洛阳亲友如相问，一片冰心在玉壶 9
    const val SL_77 = 77//劝君更尽一杯酒，西出阳关无故人 9
    const val SL_97 = 97// 我有所念人，隔在远远乡 9
//    const val SL_16 = 16//一寸相思千万绪，人间没个安排处 6
//    const val SL_25 = 25//月失楼台，雾迷津渡，桃源望断无寻处 2
//    const val SL_32 = 32// 江畔何人初见月，江月和年初照人？人生代代无穷已，江月年年望相似 2
//    const val SL_38 = 38//十年生死两茫茫，不思量，自难忘 7 ///////-----
//    const val SL_66 = 66//花自飘零水自流。一种相思，两处闲愁 8
//    const val SL_73 = 73//同是天涯沦落人，相逢何必曾相识 9
//    const val SL_84 = 84//人世几回伤往事，山形依旧枕寒流 6
//    const val SL_86 = 86//若教眼底无离恨，不信人间有白头 1
//    const val SL_89 = 89//借问酒家何处有，牧童遥指杏花村 5
    const val SL_98 = 98
    const val SL_99 = 99
    const val SL_100 = 100
    const val SL_101 = 101
    const val SL_102 = 102
    const val SL_103 = 103

    const val ST_TAG1 = "华语"
    const val ST_TAG2 = "翻唱"
    const val ST_TAG3 = "轻音乐"
    const val ST_TAG4 = "安静"
    const val ST_TAG5 = "民谣"
    const val ST_TAG6 = "女声"
    const val ST_TAG7 = "节奏"


    const val PAGE_SIZE_BANNER = 4
    const val PAGE_SIZE_SONG_LIST_COVER = 5
    const val PAGE_SIZE_SINGLE_SONG = 3
    const val PAGE_SIZE_POETRY_SONG = 6
    const val PAGE_SIZE_DAILY_RECOMMEND = 6
    const val PAGE_SIZE_RECOMMEND_BOOK = 2

    const val FIRST_LAUNCH = "first_launch"
    const val DAY_OF_WEEK = "day_of_week"
    const val THE_DAY_STEP = "the_day_step"

    /*每日可变的数据 保持一天都不变的数据 过了当天就更换*/
    const val DAILY_BANNER = 0
    const val DAILY_RECOMMEND_SONG = 1
    const val DAILY_HOT_SONG = 2
    const val DAILY_RECOMMEND_BOOK = 3
    const val DAILY_LIST_COVER = 4
    const val DAILY_POETRY_SONG = 5
    val tabClassifyName = listOf(
        R.string.chinese.getStringRes(),
        R.string.quite.getStringRes(),
        R.string.antique.getStringRes(),
        R.string.folk.getStringRes(),
        R.string.cure.getStringRes(),
        R.string.classic.getStringRes(),
        R.string.english.getStringRes(),
        R.string.japanese.getStringRes(),
        R.string.pure_music.getStringRes()
    )

    /*六位推荐歌手*/
    const val SINGER_JAY_CHOU = 1
    const val SINGER_XUE = 5
    const val SINGER_EASON = 2
    const val SINGER_DENG_ZQ = 100
    const val SINGER_REN_RAN = 101
    const val SINGER_WAIT_JUN = 107
    const val IMG_JAY_CHOU_1 =
        "https://cdnmusic.migu.cn/picture/2019/1031/1528/AM8fc7d10693eb4b658a481781cbc16d6a.jpg"
    const val IMG_JAY_CHOU_2 =
        "https://cdnmusic.migu.cn/picture/2020/0611/1054/ARTMefb7b84e00cfb7692ea2d3eaafd8b09f.jpg"
    const val IMG_JAY_CHOU_3 =
        "https://cdnmusic.migu.cn/picture/2020/0612/0000/AM977e777d850e857ef41d7a457021e6c9.jpg"
    const val IMG_JAY_CHOU_4 =
        "https://cdnmusic.migu.cn/picture/2019/1031/0115/AM5e4e490819324ef38e3f335b5feea38d.jpg"
    const val IMG_JAY_CHOU_5 =
        "https://cdnmusic.migu.cn/picture/2020/0918/1728/AM1875dd224f9487154de50fdd20137052.jpg"
    const val IMG_JAY_CHOU_6 =
        "https://cdnmusic.migu.cn/picture/2019/1031/0114/AM7be67f4731fd4ccb9a95b42f7875dd2b.jpg"
    val jayChouImgs = arrayOf(
        IMG_JAY_CHOU_1,
        IMG_JAY_CHOU_2,
        IMG_JAY_CHOU_3,
        IMG_JAY_CHOU_4,
        IMG_JAY_CHOU_5,
        IMG_JAY_CHOU_6
    )


    const val IMG_XUE_1 =
        "https://cdnmusic.migu.cn/picture/2019/1031/0324/AM6fa11f9630ec45a38d8de73d1f075f3b.jpg"
    const val IMG_XUE_2 =
        "https://cdnmusic.migu.cn/picture/2020/0731/0324/AMee13f50a6f9240804c1b3fe6e802aff8.jpg"
    const val IMG_XUE_3 =
        "https://cdnmusic.migu.cn/picture/2019/1031/0314/AM67d96d6eebf64b13acef4f9380537287.jpg"
    const val IMG_XUE_4 =
        "https://cdnmusic.migu.cn/picture/2019/1031/1035/AM9b799e13105e4523aa8df662fff2fdaf.jpg"
    const val IMG_XUE_5 =
        "https://cdnmusic.migu.cn/picture/2019/1031/0252/AM3d9e461f1dde4e829c72175fd5bef8b2.jpg"
    const val IMG_XUE_6 =
        "https://cdnmusic.migu.cn/picture/2020/0515/1800/AM2c2103b25dbb02b6be71801dec6e7182.jpg"
    val xueImgs = arrayOf(
        IMG_XUE_1,
        IMG_XUE_2,
        IMG_XUE_3,
        IMG_XUE_4,
        IMG_XUE_5,
        IMG_XUE_6
    )


    const val IMG_EASON_1 =
        "https://cdnmusic.migu.cn/picture/2019/1128/1714/AMcbe740ed5b4341f09951a17ef7d6f202.jpg"
    const val IMG_EASON_2 =
        "https://cdnmusic.migu.cn/picture/2019/1113/0949/AM200519e98ebe4c828edbfcf2edda85a1.jpg"
    const val IMG_EASON_3 =
        "https://cdnmusic.migu.cn/picture/2020/0813/0307/AM78a35c2c43f7901718275548e4d2d0d2.jpg"
    const val IMG_EASON_4 =
        "https://cdnmusic.migu.cn/picture/2020/1030/0321/AM84bde70f8257781831fe991060742ce8.jpg"
    const val IMG_EASON_5 =
        "https://cdnmusic.migu.cn/picture/2020/1031/0314/AMdcef963373f6e0d73ed582e2cb17a28c.jpg"
    const val IMG_EASON_6 =
        "https://cdnmusic.migu.cn/picture/2020/0926/0324/AMc0aebd3c75c3a255a8159204f3782887.jpg"
    val eaSonImgs = arrayOf(
        IMG_EASON_1,
        IMG_EASON_2,
        IMG_EASON_3,
        IMG_EASON_4,
        IMG_EASON_5,
        IMG_EASON_6
    )


    const val IMG_DENG_ZQ_1 =
        "https://cdnmusic.migu.cn/picture/2020/0316/1349/AMb53926db54c66c96dc26c6c4ba721c99.jpg"
    const val IMG_DENG_ZQ_2 =
        "https://cdnmusic.migu.cn/picture/2020/0316/1407/AMf63f25a8980d719e533d305b1de55a6e.jpg"
    const val IMG_DENG_ZQ_3 =
        "https://cdnmusic.migu.cn/picture/2019/0510/0856/AMdaf805b05cae4648921499075e061b28.jpg"
    const val IMG_DENG_ZQ_4 =
        "https://cdnmusic.migu.cn/picture/2020/0316/1349/AM0a78b1b019624c77345b78170f218399.jpg"
    const val IMG_DENG_ZQ_5 =
        "https://cdnmusic.migu.cn/picture/2020/0316/1407/AMcd2cb7d7f05806fdc6ce6572051e08bc.jpg"
    const val IMG_DENG_ZQ_6 =
        "https://cdnmusic.migu.cn/picture/2020/0519/1728/AMa95f65ad40ec17dbb01cc1dccd54e4ad.jpg"
    val dengZqImgs = arrayOf(
        IMG_DENG_ZQ_1,
        IMG_DENG_ZQ_2,
        IMG_DENG_ZQ_3,
        IMG_DENG_ZQ_4,
        IMG_DENG_ZQ_5,
        IMG_DENG_ZQ_6
    )

    const val REN_RAN_1 =
        "https://cdnmusic.migu.cn/picture/2019/0523/0842/AMda1880f666f249319309d0c6ccd92133.jpg"
    const val REN_RAN_2 =
        "https://cdnmusic.migu.cn/picture/2019/0523/0842/AM8f33663f926e4c13b9543b229ef663b8.jpg"
    const val REN_RAN_3 =
        "https://cdnmusic.migu.cn/picture/2019/0523/0842/AM8e157948aa1942bd9404f8b4cad0b75d.jpg"
    const val REN_RAN_4 =
        "https://cdnmusic.migu.cn/picture/2020/0612/1728/AM1f081629156f8fd9ca28bd5333de87f9.jpg"
    const val REN_RAN_5 =
        "https://cdnmusic.migu.cn/picture/2020/1009/1642/ARTMbbfe89ced045a354490ad9f2a955c3a7.jpg"
    val renRanImgs = arrayOf(
        REN_RAN_1,
        REN_RAN_2,
        REN_RAN_3,
        REN_RAN_4,
        REN_RAN_5
    )

    const val WAIT_JUN_1 =
        "https://cdnmusic.migu.cn/picture/2020/1023/2100/ARTM38415224de9e5d19e5f22953c45a0c3b.jpg"
    const val WAIT_JUN_2 =
        "https://cdnmusic.migu.cn/picture/2020/0731/0352/AM43d53eb431b1d6035d577eddcbbeece0.jpg"
    const val WAIT_JUN_3 =
        "https://bkimg.cdn.bcebos.com/pic/2934349b033b5bb5c9ea5c52c29ac239b6003af3164f?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxMTY=,g_7,xp_5,yp_5"
    const val WAIT_JUN_4 =
        "https://bkimg.cdn.bcebos.com/pic/b7003af33a87e950352a97c0e4714443fbf2b2111e4f?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxNTA=,g_7,xp_5,yp_5"
    val waitJunImgs = arrayOf(
        WAIT_JUN_1,
        WAIT_JUN_2,
        WAIT_JUN_3,
        WAIT_JUN_4
    )

    /*名句*/
    /**
     * GET /api/privilege/getCustomerCreditInfo HTTP/1.1
     * GET /api/operation/getHomeOperation HTTP/1.1
     * host 13.235.69.2:8041
     *
     *
     * GET /api/onehour/Default10.aspx?page=1&token=gswapi HTTP/1.1
     * GET /api/mingju/Default10.aspx?c=%E6%8A%92%E6%83%85&t=&page=1&token=gswapi HTTP/1.1
     * GET /api/mingju/juv11.aspx?id=2EA719EF1DF020F3BCA75C9A0D7D058D&token=gswapi HTTP/1.1
     * HOST app.gushiwen.cn
     *
     *
     * {
    "sumPage": 10,
    "currentPage": 1,
    "printTime": "2020年11月16日 07:44:37",
    "gushiwens": [{
    "id": 325524,
    "idnew": "7B3F3580C65506BCA6634DA99B147558",
    "nameStr": "点绛唇·寄南海梁药亭",
    "author": "纳兰性德",
    "chaodai": "清代",
    "cont": "一帽征尘，留君不住从君去。片帆何处，南浦沈香雨。\u003cbr /\u003e回首风流，紫竹村边住。孤鸿语，三生定许，可是梁鸿侣？ ",
    "axing": 0,
    "bxing": 0,
    "cxing": 0,
    "dxing": 0,
    "exing": 0,
    "type": null,
    "tag": "",
    "langsongAuthor": "",
    "langsongAuthorPY": "",
    "yizhu": "9933003333ff",
    "yizhuAuthor": "佚名",
    "yizhuCankao": "纳兰容若．纳兰词全编笺注：湖南文艺出版社，2011-07-01：69-70\u0026纳兰性德．纳兰词：凤凰出版社，2012-05-01：33-35",
    "yizhuYuanchuang": false,
    "yizhuIspass": true,
    "shangIspass": true,
    "beijingIspass": false,
    "pinglunCount": 0,
    "idsc": "66D3735DFB74207F",
    "SCDate": "\/Date(-62135596800000)\/"
    }, {
    "id": 48798,
    "idnew": "DDF58AD83FF75C4BC5F63832C7F14CF7",
    "nameStr": "玉楼春·别后不知君远近",
    "author": "欧阳修",
    "chaodai": "宋代",
    "cont": "\u003cp\u003e别后不知君远近，触目凄凉多少闷。渐行渐远渐无书，水阔鱼沉何处问。\u003cbr /\u003e夜深风竹敲秋韵，万叶千声皆是恨。故攲单枕梦中寻，梦又不成灯又烬。\u003c/p\u003e",
    "axing": 0,
    "bxing": 0,
    "cxing": 0,
    "dxing": 0,
    "exing": 0,
    "type": null,
    "tag": "",
    "langsongAuthor": "",
    "langsongAuthorPY": "",
    "yizhu": "9933003333ff",
    "yizhuAuthor": "佚名",
    "yizhuCankao": "李静 等 ．唐诗宋词鉴赏大全集 ．北京 ：华文出版社 ，2009年11月版 ：第234页 ．",
    "yizhuYuanchuang": false,
    "yizhuIspass": true,
    "shangIspass": true,
    "beijingIspass": false,
    "pinglunCount": 0,
    "idsc": "65A9B550B26104E3",
    "SCDate": "\/Date(-62135596800000)\/"
    }, {
    "id": 1013872,
    "idnew": "0A778BD6986132494C2706530946BB24",
    "nameStr": "浪淘沙·闷自剔残灯",
    "author": "纳兰性德",
    "chaodai": "清代",
    "cont": "闷自剔残灯，暗雨空庭。潇潇已是不堪听，那更西风偏著意，做尽秋声。\u003cbr /\u003e城柝已三更，欲睡还醒。薄寒中夜掩银屏，曾染戒香消俗念，莫又多情。",
    "axing": 0,
    "bxing": 0,
    "cxing": 0,
    "dxing": 0,
    "exing": 0,
    "type": null,
    "tag": "",
    "langsongAuthor": "",
    "langsongAuthorPY": "",
    "yizhu": "9933003333ff",
    "yizhuAuthor": "佚名",
    "yizhuCankao": "（清）纳兰性德著．邢学波笺注,纳兰词笺注全编：天津人民出版社，2013.10：第135页\u0026（清）纳兰性德著．墨香斋译评,纳兰词 双色插图版：中国纺织出版社，2015.10：第131页",
    "yizhuYuanchuang": false,
    "yizhuIspass": true,
    "shangIspass": true,
    "beijingIspass": false,
    "pinglunCount": 0,
    "idsc": "DA1350D30E3097ACD64184662D2A0FC3",
    "SCDate": "\/Date(-62135596800000)\/"
    }, {
    "id": 71212,
    "idnew": "C28256709821C27A99F3D66C1906567C",
    "nameStr": "绝句",
    "author": "杜甫",
    "chaodai": "唐代",
    "cont": "迟日江山丽，春风花草香。\u003cbr /\u003e泥融飞燕子，沙暖睡鸳鸯。 ",
    "axing": 0,
    "bxing": 0,
    "cxing": 0,
    "dxing": 0,
    "exing": 0,
    "type": null,
    "tag": "",
    "langsongAuthor": "陈琅",
    "langsongAuthorPY": "chenlang",
    "yizhu": "9933003333ff",
    "yizhuAuthor": "佚名",
    "yizhuCankao": "聂巧平．唐诗三百首：崇文书局，2015：194-195\u0026邓魁英．杜甫选集：中华书局，1986：245\u0026姜海宽．杜甫诗歌选读：中州古籍出版社，2014：452-453",
    "yizhuYuanchuang": false,
    "yizhuIspass": true,
    "shangIspass": true,
    "beijingIspass": false,
    "pinglunCount": 0,
    "idsc": "1A7C1B02D97DEAC0",
    "SCDate": "\/Date(-62135596800000)\/"
    }, {
    "id": 49607,
    "idnew": "9ECB22599AA3134AB491FB511AEDDAFC",
    "nameStr": "浣溪沙·端午",
    "author": "苏轼",
    "chaodai": "宋代",
    "cont": "轻汗微微透碧纨，明朝端午浴芳兰。流香涨腻满晴川。\u003cbr /\u003e彩线轻缠红玉臂，小符斜挂绿云鬟。佳人相见一千年。 ",
    "axing": 0,
    "bxing": 0,
    "cxing": 0,
    "dxing": 0,
    "exing": 0,
    "type": null,
    "tag": "",
    "langsongAuthor": "陈琅",
    "langsongAuthorPY": "chenlang",
    "yizhu": "9933003333ff",
    "yizhuAuthor": "佚名",
    "yizhuCankao": "马奇中选编,唐诗·宋词·元曲三百首 珍藏本,四川大学出版社,2000.02,第464-465页\u0026朱靖华，饶学刚，王文龙编著,苏轼词新释辑评 （下册）,中国书店,,第1207-1209页",
    "yizhuYuanchuang": false,
    "yizhuIspass": true,
    "shangIspass": true,
    "beijingIspass": false,
    "pinglunCount": 0,
    "idsc": "17B045DD76A2D873",
    "SCDate": "\/Date(-62135596800000)\/"
    }, {
    "id": 70252,
    "idnew": "2940BC9ADB7CE9A4E0969069A0858F2B",
    "nameStr": "十二月过尧民歌·别情",
    "author": "王实甫",
    "chaodai": "元代",
    "cont": "自别后遥山隐隐，更那堪远水粼粼。\u003cbr /\u003e见杨柳飞绵滚滚，对桃花醉脸醺醺。\u003cbr /\u003e透内阁香风阵阵，掩重门暮雨纷纷。\u003cbr /\u003e怕黄昏忽地又黄昏，不销魂怎地不销魂。\u003cbr /\u003e新啼痕压旧啼痕，断肠人忆断肠人。\u003cbr /\u003e今春香肌瘦几分？缕带宽三寸。 ",
    "axing": 0,
    "bxing": 0,
    "cxing": 0,
    "dxing": 0,
    "exing": 0,
    "type": null,
    "tag": "",
    "langsongAuthor": "陈琅",
    "langsongAuthorPY": "chenlang",
    "yizhu": "9933003333ff",
    "yizhuAuthor": "佚名",
    "yizhuCankao": "关汉聊．《元曲三百首》：中国华侨出版社，2013年：第94页\u0026齐义农．《诗情画意品读元曲》：光明日报出版社，2007年9月1日",
    "yizhuYuanchuang": false,
    "yizhuIspass": true,
    "shangIspass": true,
    "beijingIspass": false,
    "pinglunCount": 0,
    "idsc": "DBCEC628130F8368",
    "SCDate": "\/Date(-62135596800000)\/"
    }, {
    "id": 132062,
    "idnew": "348D529B93E61EBDEB59FC890B030E40",
    "nameStr": "沧浪亭怀贯之",
    "author": "苏舜钦",
    "chaodai": "宋代",
    "cont": "沧浪独步亦无悰，聊上危台四望中。\u003cbr /\u003e秋色入林红黯淡，日光穿竹翠玲珑。\u003cbr /\u003e酒徒飘落风前燕，诗社凋零霜后桐。\u003cbr /\u003e君又暂来还径往，醉吟谁复伴衰翁。 ",
    "axing": 0,
    "bxing": 0,
    "cxing": 0,
    "dxing": 0,
    "exing": 0,
    "type": null,
    "tag": "",
    "langsongAuthor": "陈琅",
    "langsongAuthorPY": "chenlang",
    "yizhu": "9933003333ff",
    "yizhuAuthor": "佚名",
    "yizhuCankao": "古诗文网经典传承志愿小组．白马非马译注",
    "yizhuYuanchuang": false,
    "yizhuIspass": true,
    "shangIspass": true,
    "beijingIspass": false,
    "pinglunCount": 0,
    "idsc": "5AF7BAA9961DB2E2",
    "SCDate": "\/Date(-62135596800000)\/"
    }, {
    "id": 71199,
    "idnew": "39B4377EC42E3F96560CE3671056EF66",
    "nameStr": "过松源晨炊漆公店",
    "author": "杨万里",
    "chaodai": "宋代",
    "cont": "莫言下岭便无难，赚得行人错喜欢。(错喜欢 一作：空喜欢)\u003cbr /\u003e政入万山围子里，一山放出一山拦。(政入 一作：正入；围子 一作：圈子；放出 一作：放过) ",
    "axing": 0,
    "bxing": 0,
    "cxing": 0,
    "dxing": 0,
    "exing": 0,
    "type": null,
    "tag": "",
    "langsongAuthor": "陈琅",
    "langsongAuthorPY": "chenlang",
    "yizhu": "9933003333ff",
    "yizhuAuthor": "佚名",
    "yizhuCankao": "",
    "yizhuYuanchuang": false,
    "yizhuIspass": true,
    "shangIspass": true,
    "beijingIspass": false,
    "pinglunCount": 0,
    "idsc": "1D7CD6D186378C32",
    "SCDate": "\/Date(-62135596800000)\/"
    }, {
    "id": 63749,
    "idnew": "CB7FD56ACD21FE03CC991CB5FEC4F05D",
    "nameStr": "满江红·和王昭仪韵",
    "author": "汪元量",
    "chaodai": "宋代",
    "cont": "天上人家，醉王母、蟠桃春色。被午夜、漏声催箭，晓光侵阙。花覆千官鸾阁外，香浮九鼎龙楼侧。恨黑风吹雨湿霓裳，歌声歇。\u003cbr /\u003e人去后，书应绝。肠断处，心难说。更那堪杜宇，满山啼血。事去空流东汴水，愁来不见西湖月。有谁知、海上泣婵娟，菱花缺。",
    "axing": 0,
    "bxing": 0,
    "cxing": 0,
    "dxing": 0,
    "exing": 0,
    "type": null,
    "tag": "",
    "langsongAuthor": "",
    "langsongAuthorPY": "",
    "yizhu": "9933003333ff",
    "yizhuAuthor": "佚名",
    "yizhuCankao": "马丽选注．满江红：东方出版社，2001年01月第1版：第71-72页",
    "yizhuYuanchuang": false,
    "yizhuIspass": true,
    "shangIspass": true,
    "beijingIspass": false,
    "pinglunCount": 0,
    "idsc": "F70D571D080ECEAE",
    "SCDate": "\/Date(-62135596800000)\/"
    }, {
    "id": 5493,
    "idnew": "01F93717CA785D30CF8033F4BCECFF1B",
    "nameStr": "齐州送祖三 / 河上送赵仙舟 / 淇上别赵仙舟",
    "author": "王维",
    "chaodai": "唐代",
    "cont": "相逢方一笑，相送还成泣。\u003cbr /\u003e祖帐已伤离，荒城复愁入。\u003cbr /\u003e天寒远山净，日暮长河急。\u003cbr /\u003e解缆君已遥，望君犹伫立。 ",
    "axing": 0,
    "bxing": 0,
    "cxing": 0,
    "dxing": 0,
    "exing": 0,
    "type": null,
    "tag": "",
    "langsongAuthor": "",
    "langsongAuthorPY": "",
    "yizhu": "9933003333ff",
    "yizhuAuthor": "佚名",
    "yizhuCankao": "彭定求 等 ．全唐诗（上） ．上海 ：上海古籍出版社 ，1986 ：286 ．\u0026 邓安生 等 ．王维诗选译 ．成都 ：巴蜀书社 ，1990 ：35-36 ．",
    "yizhuYuanchuang": false,
    "yizhuIspass": true,
    "shangIspass": true,
    "beijingIspass": false,
    "pinglunCount": 0,
    "idsc": "609D32CF96FEAB85",
    "SCDate": "\/Date(-62135596800000)\/"
    }],
    "mingjus": [{
    "id": 0,
    "idnew": "BE60957A622DB0C00AB5745CBE4EAEAE",
    "nameStr": "罗襟湿未干，又是凄凉雪。",
    "classStr": null,
    "type": null,
    "shiID": 0,
    "shiIDnew": "705D1F1ECA0C985FA90A3ABACF4DCCFB",
    "exing": 0,
    "author": "张淑芳",
    "shiName": "满路花·冬",
    "ipStr": null,
    "isShiwen": false,
    "gujiyiwen": null,
    "zhangjieID": "0",
    "zhangjieNameStr": "",
    "guishu": 1,
    "tag": null,
    "chaodai": null,
    "idsc": null,
    "zhangjieIDjm": "46653FD803893E4FF99C0E013AA86B06"
    }, {
    "id": 0,
    "idnew": "E96BA96E4E9D208E6F83DAD2A770044B",
    "nameStr": "生而知之者上也，学而知之者次也；困而学之又其次也。困而不学，民斯为下矣。",
    "classStr": null,
    "type": null,
    "shiID": 0,
    "shiIDnew": "7D4CFD9939C24D4F7786A31C5E8FD314",
    "exing": 0,
    "author": "",
    "shiName": " ",
    "ipStr": null,
    "isShiwen": false,
    "gujiyiwen": null,
    "zhangjieID": "34",
    "zhangjieNameStr": "论语·季氏篇",
    "guishu": 2,
    "tag": null,
    "chaodai": null,
    "idsc": null,
    "zhangjieIDjm": "46653FD803893E4FFFD969D27F952D9D"
    }, {
    "id": 0,
    "idnew": "AFAFA983603F2C63926BEC352A16BF50",
    "nameStr": "成大功者不小苛",
    "classStr": null,
    "type": null,
    "shiID": 0,
    "shiIDnew": "7D4CFD9939C24D4F7786A31C5E8FD314",
    "exing": 0,
    "author": "",
    "shiName": " ",
    "ipStr": null,
    "isShiwen": false,
    "gujiyiwen": null,
    "zhangjieID": "10754",
    "zhangjieNameStr": "说苑·政理",
    "guishu": 2,
    "tag": null,
    "chaodai": null,
    "idsc": null,
    "zhangjieIDjm": "46653FD803893E4F5391FC9A64F2BDE7"
    }, {
    "id": 0,
    "idnew": "4DC82B3D13EC608AD4A3396B32B9D34A",
    "nameStr": "圣人自知不自见；自爱不自贵。",
    "classStr": null,
    "type": null,
    "shiID": 0,
    "shiIDnew": "7D4CFD9939C24D4F7786A31C5E8FD314",
    "exing": 0,
    "author": "",
    "shiName": " ",
    "ipStr": null,
    "isShiwen": false,
    "gujiyiwen": null,
    "zhangjieID": "3381",
    "zhangjieNameStr": "老子·德经·第七十二章",
    "guishu": 2,
    "tag": null,
    "chaodai": null,
    "idsc": null,
    "zhangjieIDjm": "46653FD803893E4FA28126D5422D0B43"
    }, {
    "id": 0,
    "idnew": "ED009DD5A42FA6122E93803AB42D27E7",
    "nameStr": "主贵多变，国贵少变。国多物，削；主少物，强。",
    "classStr": null,
    "type": null,
    "shiID": 0,
    "shiIDnew": "7D4CFD9939C24D4F7786A31C5E8FD314",
    "exing": 0,
    "author": "",
    "shiName": "",
    "ipStr": null,
    "isShiwen": false,
    "gujiyiwen": null,
    "zhangjieID": "3993",
    "zhangjieNameStr": "商君书·去强",
    "guishu": 2,
    "tag": null,
    "chaodai": null,
    "idsc": null,
    "zhangjieIDjm": "46653FD803893E4F1BA6DCC8CECA2E29"
    }, {
    "id": 0,
    "idnew": "1474134DB6515EBE551AD325BE5DCAF2",
    "nameStr": "至乐无乐，至誉无誉。",
    "classStr": null,
    "type": null,
    "shiID": 0,
    "shiIDnew": "7D4CFD9939C24D4F7786A31C5E8FD314",
    "exing": 0,
    "author": "",
    "shiName": " ",
    "ipStr": null,
    "isShiwen": false,
    "gujiyiwen": null,
    "zhangjieID": "3271",
    "zhangjieNameStr": "庄子·外篇·至乐",
    "guishu": 2,
    "tag": null,
    "chaodai": null,
    "idsc": null,
    "zhangjieIDjm": "46653FD803893E4F778C2D246BE5CAAB"
    }, {
    "id": 0,
    "idnew": "E9E14E3AA273C95075CDF9A4C0068151",
    "nameStr": "小人乐闻君子之过，君子耻闻小人之恶。",
    "classStr": null,
    "type": null,
    "shiID": 0,
    "shiIDnew": "7D4CFD9939C24D4F7786A31C5E8FD314",
    "exing": 0,
    "author": "",
    "shiName": "",
    "ipStr": null,
    "isShiwen": false,
    "gujiyiwen": null,
    "zhangjieID": "18969",
    "zhangjieNameStr": "格言联璧·接物类",
    "guishu": 2,
    "tag": null,
    "chaodai": null,
    "idsc": null,
    "zhangjieIDjm": "46653FD803893E4F389F4F67D918DA1E"
    }, {
    "id": 0,
    "idnew": "B50D55B0182AEDF030E76C808568D694",
    "nameStr": "物舍其所长，之其所短，尧亦有所不及矣",
    "classStr": null,
    "type": null,
    "shiID": 0,
    "shiIDnew": "7D4CFD9939C24D4F7786A31C5E8FD314",
    "exing": 0,
    "author": "",
    "shiName": "",
    "ipStr": null,
    "isShiwen": false,
    "gujiyiwen": null,
    "zhangjieID": "4487",
    "zhangjieNameStr": "战国策·齐三·孟尝君有舍人而弗悦",
    "guishu": 2,
    "tag": null,
    "chaodai": null,
    "idsc": null,
    "zhangjieIDjm": "46653FD803893E4FF213E88129B144A3"
    }, {
    "id": 0,
    "idnew": "7DE923CBDB1279B53FD35E216CA0967D",
    "nameStr": "臣闻君子乐得其志，小人乐得其事。",
    "classStr": null,
    "type": null,
    "shiID": 0,
    "shiIDnew": "7D4CFD9939C24D4F7786A31C5E8FD314",
    "exing": 0,
    "author": "",
    "shiName": " ",
    "ipStr": null,
    "isShiwen": false,
    "gujiyiwen": null,
    "zhangjieID": "4958",
    "zhangjieNameStr": "六韬·文韬·文师",
    "guishu": 2,
    "tag": null,
    "chaodai": null,
    "idsc": null,
    "zhangjieIDjm": "46653FD803893E4F008CEF7DF866F51C"
    }, {
    "id": 0,
    "idnew": "FD3E41FCA649360B58AB66C349BFC2FE",
    "nameStr": "老去诗篇浑漫与，春来花鸟莫深愁。",
    "classStr": null,
    "type": null,
    "shiID": 0,
    "shiIDnew": "D30A68D383AE88E62267D3899E67EF68",
    "exing": 0,
    "author": "杜甫",
    "shiName": "江上值水如海势聊短述",
    "ipStr": null,
    "isShiwen": false,
    "gujiyiwen": null,
    "zhangjieID": "0",
    "zhangjieNameStr": "",
    "guishu": 1,
    "tag": null,
    "chaodai": null,
    "idsc": null,
    "zhangjieIDjm": "46653FD803893E4FF99C0E013AA86B06"
    }],
    "faxian": {
    "id": 0,
    "idout": null,
    "keyStr": "植物|荷花",
    "nameStr": "名句",
    "shiTag": null,
    "juTag": null,
    "cont": "",
    "creTime": "\/Date(-62135596800000)\/"
    }
    }
     */
}
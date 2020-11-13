package wind.maimusic.model.core

import com.google.gson.annotations.SerializedName

data class SongLrc(
    /**
     * code : 0
     * data : {"keyword":"说谎","lyric":{"curnum":1,"curpage":1,"list":[{"albumid":54250,"albummid":"004YodY33zsWTT","albumname":"感官/世界","albumname_hilight":"感官/世界","alertid":23,"belongCD":0,"cdIdx":6,"chinesesinger":0,"content":"说谎 (거짓말) (《杜拉拉升职记》电视剧插曲|《针尖上的天使》电影主题曲) - 林宥嘉 (Yoga Lin)\\n 词：施人诚\\n 曲：李双飞\\n 是有过几个不错对象\\n 说起来并不寂寞孤单\\n 可能我浪荡让人家不安\\n 才会结果都阵亡\\n 我没有什么阴影魔障\\n 你千万不要放在心上\\n 我又不脆弱何况那算什么伤\\n 反正爱情不就都这样\\n 我没有说谎我何必说谎\\n 你懂我的我对你从来就不会假装\\n 我哪有说谎\\n 请别以为你有多难忘\\n 笑是真的不是我逞强\\n 我好久没来这间餐厅\\n 没想到已经换了装潢\\n 角落那窗口闻的到玫瑰花香\\n 被你一说是有些印象\\n 我没有说谎我何必说谎\\n 你知道的我缺点之一就是很健忘\\n 我哪有说谎\\n 是很感谢今晚的相伴\\n 但我竟然有些不习惯\\n 我没有说谎我何必说谎\\n 爱一个人没爱到难道就会怎么样\\n 别说我说谎\\n 人生已经如此的艰难\\n 有些事情就不要拆穿\\n 我没有说谎是爱情说谎\\n 它带你来骗我说\\n 渴望的有 可能有希望\\n 我没有说谎\\n 祝你做个幸福的新娘\\n 我的心事请你就遗忘","docid":"12084758858355992521","download_url":"http://soso.music.qq.com/fcgi-bin/fcg_download_lrc.q?song=说谎&singer=林宥嘉&down=1&songid=638596&docid=12084758858355992521","interval":266,"isonly":0,"lyric":" 我哪有*说谎<\/em>\\n 请别以为你有多难忘\\n 笑是真的不是我逞强\\n","media_mid":"000W95Fk3lAVxV","msgid":14,"nt":3842743252,"pay":{"payalbum":0,"payalbumprice":0,"paydownload":1,"payinfo":1,"payplay":0,"paytrackmouth":1,"paytrackprice":200},"preview":{"trybegin":73557,"tryend":104291,"trysize":0},"pubtime":1256832000,"pure":0,"singer":[{"id":11606,"mid":"001f0VyZ1hmWZ1","name":"林宥嘉","name_hilight":"林宥嘉"}],"size128":4264898,"size320":10652912,"sizeape":27291116,"sizeflac":28067207,"sizeogg":5800490,"songid":638596,"songmid":"000W95Fk3lAVxV","songname":"说谎","songname_hilight":"*说谎<\/em>","strMediaMid":"000W95Fk3lAVxV","stream":1,"switch":636675,"t":1,"tag":11,"type":0,"ver":0,"vid":""}],"totalnum":246},"priority":0,"qc":[],"tab":7,"taglist":[],"totaltime":0,"zhida":{"chinesesinger":0,"type":0}}
     * message :
     * notice :
     * subcode : 0
     * time : 1567575160
     * tips :
     ** */
    var code: Int? = null,
    var data: LrcDataBean? = null
)

data class LrcDataBean(
    /**
     * keyword : 说谎
     * lyric : {"curnum":1,"curpage":1,"list":[{"albumid":54250,"albummid":"004YodY33zsWTT","albumname":"感官/世界","albumname_hilight":"感官/世界","alertid":23,"belongCD":0,"cdIdx":6,"chinesesinger":0,"content":"说谎 (거짓말) (《杜拉拉升职记》电视剧插曲|《针尖上的天使》电影主题曲) - 林宥嘉 (Yoga Lin)\\n 词：施人诚\\n 曲：李双飞\\n 是有过几个不错对象\\n 说起来并不寂寞孤单\\n 可能我浪荡让人家不安\\n 才会结果都阵亡\\n 我没有什么阴影魔障\\n 你千万不要放在心上\\n 我又不脆弱何况那算什么伤\\n 反正爱情不就都这样\\n 我没有说谎我何必说谎\\n 你懂我的我对你从来就不会假装\\n 我哪有说谎\\n 请别以为你有多难忘\\n 笑是真的不是我逞强\\n 我好久没来这间餐厅\\n 没想到已经换了装潢\\n 角落那窗口闻的到玫瑰花香\\n 被你一说是有些印象\\n 我没有说谎我何必说谎\\n 你知道的我缺点之一就是很健忘\\n 我哪有说谎\\n 是很感谢今晚的相伴\\n 但我竟然有些不习惯\\n 我没有说谎我何必说谎\\n 爱一个人没爱到难道就会怎么样\\n 别说我说谎\\n 人生已经如此的艰难\\n 有些事情就不要拆穿\\n 我没有说谎是爱情说谎\\n 它带你来骗我说\\n 渴望的有 可能有希望\\n 我没有说谎\\n 祝你做个幸福的新娘\\n 我的心事请你就遗忘","docid":"12084758858355992521","download_url":"http://soso.music.qq.com/fcgi-bin/fcg_download_lrc.q?song=说谎&singer=林宥嘉&down=1&songid=638596&docid=12084758858355992521","interval":266,"isonly":0,"lyric":" 我哪有*说谎<\/em>\\n 请别以为你有多难忘\\n 笑是真的不是我逞强\\n","media_mid":"000W95Fk3lAVxV","msgid":14,"nt":3842743252,"pay":{"payalbum":0,"payalbumprice":0,"paydownload":1,"payinfo":1,"payplay":0,"paytrackmouth":1,"paytrackprice":200},"preview":{"trybegin":73557,"tryend":104291,"trysize":0},"pubtime":1256832000,"pure":0,"singer":[{"id":11606,"mid":"001f0VyZ1hmWZ1","name":"林宥嘉","name_hilight":"林宥嘉"}],"size128":4264898,"size320":10652912,"sizeape":27291116,"sizeflac":28067207,"sizeogg":5800490,"songid":638596,"songmid":"000W95Fk3lAVxV","songname":"说谎","songname_hilight":"*说谎<\/em>","strMediaMid":"000W95Fk3lAVxV","stream":1,"switch":636675,"t":1,"tag":11,"type":0,"ver":0,"vid":""}],"totalnum":246}
     * priority : 0
     * qc : []
     * tab : 7
     * taglist : []
     * totaltime : 0
     * zhida : {"chinesesinger":0,"type":0}
     ** */
    var lyric: LyricBean? = null
)

data class LyricBean(
    /**
     * curnum : 1
     * curpage : 1
     * list : [{"albumid":54250,"albummid":"004YodY33zsWTT","albumname":"感官/世界","albumname_hilight":"感官/世界","alertid":23,"belongCD":0,"cdIdx":6,"chinesesinger":0,"content":"说谎 (거짓말) (《杜拉拉升职记》电视剧插曲|《针尖上的天使》电影主题曲) - 林宥嘉 (Yoga Lin)\\n 词：施人诚\\n 曲：李双飞\\n 是有过几个不错对象\\n 说起来并不寂寞孤单\\n 可能我浪荡让人家不安\\n 才会结果都阵亡\\n 我没有什么阴影魔障\\n 你千万不要放在心上\\n 我又不脆弱何况那算什么伤\\n 反正爱情不就都这样\\n 我没有说谎我何必说谎\\n 你懂我的我对你从来就不会假装\\n 我哪有说谎\\n 请别以为你有多难忘\\n 笑是真的不是我逞强\\n 我好久没来这间餐厅\\n 没想到已经换了装潢\\n 角落那窗口闻的到玫瑰花香\\n 被你一说是有些印象\\n 我没有说谎我何必说谎\\n 你知道的我缺点之一就是很健忘\\n 我哪有说谎\\n 是很感谢今晚的相伴\\n 但我竟然有些不习惯\\n 我没有说谎我何必说谎\\n 爱一个人没爱到难道就会怎么样\\n 别说我说谎\\n 人生已经如此的艰难\\n 有些事情就不要拆穿\\n 我没有说谎是爱情说谎\\n 它带你来骗我说\\n 渴望的有 可能有希望\\n 我没有说谎\\n 祝你做个幸福的新娘\\n 我的心事请你就遗忘","docid":"12084758858355992521","download_url":"http://soso.music.qq.com/fcgi-bin/fcg_download_lrc.q?song=说谎&singer=林宥嘉&down=1&songid=638596&docid=12084758858355992521","interval":266,"isonly":0,"lyric":" 我哪有*说谎<\/em>\\n 请别以为你有多难忘\\n 笑是真的不是我逞强\\n","media_mid":"000W95Fk3lAVxV","msgid":14,"nt":3842743252,"pay":{"payalbum":0,"payalbumprice":0,"paydownload":1,"payinfo":1,"payplay":0,"paytrackmouth":1,"paytrackprice":200},"preview":{"trybegin":73557,"tryend":104291,"trysize":0},"pubtime":1256832000,"pure":0,"singer":[{"id":11606,"mid":"001f0VyZ1hmWZ1","name":"林宥嘉","name_hilight":"林宥嘉"}],"size128":4264898,"size320":10652912,"sizeape":27291116,"sizeflac":28067207,"sizeogg":5800490,"songid":638596,"songmid":"000W95Fk3lAVxV","songname":"说谎","songname_hilight":"*说谎<\/em>","strMediaMid":"000W95Fk3lAVxV","stream":1,"switch":636675,"t":1,"tag":11,"type":0,"ver":0,"vid":""}]
     * totalnum : 246
     ** */
    var curnum: Int? = null,
    var curpage: Int? = null,
    var totalnum: Int? = null,
    var list: List<LrcListBean>? = null
)

data class LrcListBean(
    /**
     * albumid : 54250
     * albummid : 004YodY33zsWTT
     * albumname : 感官/世界
     * albumname_hilight : 感官/世界
     * alertid : 23
     * belongCD : 0
     * cdIdx : 6
     * chinesesinger : 0
     * content : 说谎 (거짓말) (《杜拉拉升职记》电视剧插曲|《针尖上的天使》电影主题曲) - 林宥嘉 (Yoga Lin)\n 词：施人诚\n 曲：李双飞\n 是有过几个不错对象\n 说起来并不寂寞孤单\n 可能我浪荡让人家不安\n 才会结果都阵亡\n 我没有什么阴影魔障\n 你千万不要放在心上\n 我又不脆弱何况那算什么伤\n 反正爱情不就都这样\n 我没有说谎我何必说谎\n 你懂我的我对你从来就不会假装\n 我哪有说谎\n 请别以为你有多难忘\n 笑是真的不是我逞强\n 我好久没来这间餐厅\n 没想到已经换了装潢\n 角落那窗口闻的到玫瑰花香\n 被你一说是有些印象\n 我没有说谎我何必说谎\n 你知道的我缺点之一就是很健忘\n 我哪有说谎\n 是很感谢今晚的相伴\n 但我竟然有些不习惯\n 我没有说谎我何必说谎\n 爱一个人没爱到难道就会怎么样\n 别说我说谎\n 人生已经如此的艰难\n 有些事情就不要拆穿\n 我没有说谎是爱情说谎\n 它带你来骗我说\n 渴望的有 可能有希望\n 我没有说谎\n 祝你做个幸福的新娘\n 我的心事请你就遗忘
     * docid : 12084758858355992521
     * download_url : http://soso.music.qq.com/fcgi-bin/fcg_download_lrc.q?song=说谎&singer=林宥嘉&down=1&songid=638596&docid=12084758858355992521
     * interval : 266
     * isonly : 0
     * lyric :  我哪有*说谎*\n 请别以为你有多难忘\n 笑是真的不是我逞强\n
     * media_mid : 000W95Fk3lAVxV
     * msgid : 14
     * nt : 3842743252
     * pay : {"payalbum":0,"payalbumprice":0,"paydownload":1,"payinfo":1,"payplay":0,"paytrackmouth":1,"paytrackprice":200}
     * preview : {"trybegin":73557,"tryend":104291,"trysize":0}
     * pubtime : 1256832000
     * pure : 0
     * singer : [{"id":11606,"mid":"001f0VyZ1hmWZ1","name":"林宥嘉","name_hilight":"林宥嘉"}]
     * size128 : 4264898
     * size320 : 10652912
     * sizeape : 27291116
     * sizeflac : 28067207
     * sizeogg : 5800490
     * songid : 638596
     * songmid : 000W95Fk3lAVxV
     * songname : 说谎
     * songname_hilight : *说谎*
     * strMediaMid : 000W95Fk3lAVxV
     * stream : 1
     * switch : 636675
     * t : 1
     * tag : 11
     * type : 0
     * ver : 0
     * vid :
     */
    var chinesesinger: Int? = null,
    var content: String? = null,
    var interval: Int? = null,
    var pubtime: Int? = null,
    var sizeogg: Int? = null,
    var songid: Int? = null,
    var songmid: String? = null,
    var songname: String? = null,
    @SerializedName("switch")
    var switchX: Int? = null,
    var t: Int? = null,
    var tag: Int? = null,
    var type: Int? = null,
    var ver: Int? = null,
    var vid: String? = null,
    var singer: List<LrcSingerBean>? = null
)


data class LrcSingerBean(
    /**
     * id : 11606
     * mid : 001f0VyZ1hmWZ1
     * name : 林宥嘉
     * name_hilight : 林宥嘉
     */
    var id: Int? = null,
    var mid: String? = null,
    var name: String? = null,
    var name_hilight: String? = null

)
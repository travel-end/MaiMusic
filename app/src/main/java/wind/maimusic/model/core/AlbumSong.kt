package wind.maimusic.model.core

import com.google.gson.annotations.SerializedName


data class AlbumSong(
    /**
     * code : 0
     * data : {"aDate":"2019-07-21","albumTips":"","color":1704579,"company":"G Nation","company_new":{"brief":"","headPic":"","id":343411,"is_show":1,"name":"G Nation"},"cur_song_num":1,"desc":"差 不 多 姑 娘\nG.E.M. 邓紫棋\n在物欲横流的现实世界，随波逐流的意识形态愈演愈烈。姑娘们被差不多的愿望所牵引，\n像孔雀一样渴望展示漂亮的皮囊，追逐差不多的浮华，迷失于差不多的诱惑。\n\n\u201c差不多姑娘\u201d该如何打破\u201c差不多\u201d的枷锁？面临无形之手的操控，她们又将何去何从？\n\n同样活在现代都市的邓紫棋，用音乐为每位差不多姑娘说出心声，以她的力量鼓励姑娘们重新找到自己。\n\n坚持用音乐表达观点，是邓紫棋的创作初心。她在「差不多姑娘」中注入了对现实敏锐的洞察，和其对女性群体的观照。因为曾几何时，她也陷入过「差不多」困境，对镜子里的自己失望，而后终于挣脱泥淖，接受没有武装的模样。邓紫棋把自己的感同身受写进音乐里，警醒那些被欲望绑架的「差不多姑娘」，告诉她们「人生真的不该这样过」。歌曲中不加修饰的歌词不断敲打人心，每一句都似在叩问，意图击碎代表虚荣假象的泡沫。\n\n每个女生都是独特的宇宙，拥有自己的光芒万丈。\n邓紫棋这颗运转中的小小宇宙，此刻正在用音乐释放出惊人能量。","genre":"流行","id":7215891,"lan":"国语","list":[{"albumdesc":"","albumid":7215891,"albummid":"002N84iF3QxG1c","albumname":"差不多姑娘","alertid":21,"belongCD":1,"cdIdx":0,"interval":230,"isonly":1,"label":"0","msgid":14,"pay":{"payalbum":0,"payalbumprice":0,"paydownload":1,"payinfo":1,"payplay":0,"paytrackmouth":1,"paytrackprice":200,"timefree":0},"preview":{"trybegin":0,"tryend":0,"trysize":960887},"rate":23,"singer":[{"id":13948,"mid":"001fNHEf1SFEFN","name":"G.E.M. 邓紫棋"}],"size128":3682018,"size320":9204722,"size5_1":0,"sizeape":0,"sizeflac":26527121,"sizeogg":5277229,"songid":234824638,"songmid":"000wocYU11tSzS","songname":"差不多姑娘","songorig":"差不多姑娘","songtype":0,"strMediaMid":"000wocYU11tSzS","stream":13,"switch":17413891,"type":0,"vid":"b0031kyhy1p"}],"mid":"002N84iF3QxG1c","name":"差不多姑娘","radio_anchor":0,"singerid":13948,"singermblog":"gemtang","singermid":"001fNHEf1SFEFN","singername":"G.E.M. 邓紫棋","song_begin":0,"total":1,"total_song_num":1}
     * message : succ
     * subcode : 0
     */
    var code: Int? = null,
    var data: AlbumDataBean? = null,
    var message: String? = null,
    var subcode: Int? = null
)

data class AlbumDataBean(
    /**
     * aDate : 2019-07-21
     * albumTips :
     * color : 1704579
     * company : G Nation
     * company_new : {"brief":"","headPic":"","id":343411,"is_show":1,"name":"G Nation"}
     * cur_song_num : 1
     * desc : 差 不 多 姑 娘
     * G.E.M. 邓紫棋
     * 在物欲横流的现实世界，随波逐流的意识形态愈演愈烈。姑娘们被差不多的愿望所牵引，
     * 像孔雀一样渴望展示漂亮的皮囊，追逐差不多的浮华，迷失于差不多的诱惑。
     *
     * “差不多姑娘”该如何打破“差不多”的枷锁？面临无形之手的操控，她们又将何去何从？
     *
     * 同样活在现代都市的邓紫棋，用音乐为每位差不多姑娘说出心声，以她的力量鼓励姑娘们重新找到自己。
     *
     * 坚持用音乐表达观点，是邓紫棋的创作初心。她在「差不多姑娘」中注入了对现实敏锐的洞察，和其对女性群体的观照。因为曾几何时，她也陷入过「差不多」困境，对镜子里的自己失望，而后终于挣脱泥淖，接受没有武装的模样。邓紫棋把自己的感同身受写进音乐里，警醒那些被欲望绑架的「差不多姑娘」，告诉她们「人生真的不该这样过」。歌曲中不加修饰的歌词不断敲打人心，每一句都似在叩问，意图击碎代表虚荣假象的泡沫。
     *
     * 每个女生都是独特的宇宙，拥有自己的光芒万丈。
     * 邓紫棋这颗运转中的小小宇宙，此刻正在用音乐释放出惊人能量。
     * genre : 流行
     * id : 7215891
     * lan : 国语
     * list : [{"albumdesc":"","albumid":7215891,"albummid":"002N84iF3QxG1c","albumname":"差不多姑娘","alertid":21,"belongCD":1,"cdIdx":0,"interval":230,"isonly":1,"label":"0","msgid":14,"pay":{"payalbum":0,"payalbumprice":0,"paydownload":1,"payinfo":1,"payplay":0,"paytrackmouth":1,"paytrackprice":200,"timefree":0},"preview":{"trybegin":0,"tryend":0,"trysize":960887},"rate":23,"singer":[{"id":13948,"mid":"001fNHEf1SFEFN","name":"G.E.M. 邓紫棋"}],"size128":3682018,"size320":9204722,"size5_1":0,"sizeape":0,"sizeflac":26527121,"sizeogg":5277229,"songid":234824638,"songmid":"000wocYU11tSzS","songname":"差不多姑娘","songorig":"差不多姑娘","songtype":0,"strMediaMid":"000wocYU11tSzS","stream":13,"switch":17413891,"type":0,"vid":"b0031kyhy1p"}]
     * mid : 002N84iF3QxG1c
     * name : 差不多姑娘
     * radio_anchor : 0
     * singerid : 13948
     * singermblog : gemtang
     * singermid : 001fNHEf1SFEFN
     * singername : G.E.M. 邓紫棋
     * song_begin : 0
     * total : 1
     * total_song_num : 1
     */
    var aDate: String? = null,
    var albumTips: String? = null,
    var color: Int? = null,
    var company: String? = null,
    var company_new: CompanyNewBean? = null,
    var cur_song_num: Int? = null,
    var desc: String? = null,
    var genre: String? = null,
    var id: Int? = null,
    var lan: String? = null,
    var mid: String? = null,
    var name: String? = null,
    var radio_anchor: Int? = null,
    var singerid: Int? = null,
    var singermblog: String? = null,
    var singermid: String? = null,
    var singername: String? = null,
    var song_begin: Int? = null,
    var total: Int? = null,
    var total_song_num: Int? = null,
    var list: List<AlbumListBean>? = null


)

data class AlbumListBean(
    /**
     * albumdesc :
     * albumid : 7215891
     * albummid : 002N84iF3QxG1c
     * albumname : 差不多姑娘
     * alertid : 21
     * belongCD : 1
     * cdIdx : 0
     * interval : 230
     * isonly : 1
     * label : 0
     * msgid : 14
     * pay : {"payalbum":0,"payalbumprice":0,"paydownload":1,"payinfo":1,"payplay":0,"paytrackmouth":1,"paytrackprice":200,"timefree":0}
     * preview : {"trybegin":0,"tryend":0,"trysize":960887}
     * rate : 23
     * singer : [{"id":13948,"mid":"001fNHEf1SFEFN","name":"G.E.M. 邓紫棋"}]
     * size128 : 3682018
     * size320 : 9204722
     * size5_1 : 0
     * sizeape : 0
     * sizeflac : 26527121
     * sizeogg : 5277229
     * songid : 234824638
     * songmid : 000wocYU11tSzS
     * songname : 差不多姑娘
     * songorig : 差不多姑娘
     * songtype : 0
     * strMediaMid : 000wocYU11tSzS
     * stream : 13
     * switch : 17413891
     * type : 0
     * vid : b0031kyhy1p
     */
    var albumdesc: String? = null,
    var albumid: Int? = null,
    var albummid: String? = null,
    var albumname: String? = null,
    var alertid: Int? = null,
    var belongCD: Int? = null,
    var cdIdx: Int? = null,
    var interval: Int? = null,
    var isonly: Int? = null,
    var label: String? = null,
    var msgid: Int? = null,
    var pay: PayBean? = null,
    var preview: PreviewBean? = null,
    var rate: Int? = null,
    var size128: Int? = null,
    var size320: Int? = null,
    var size5_1: Int? = null,
    var sizeape: Int? = null,
    var sizeflac: Int? = null,
    var sizeogg: Int? = null,
    var songid: Int? = null,
    var songmid: String? = null,
    var songname: String? = null,
    var songorig: String? = null,
    var songtype: Int? = null,
    var strMediaMid: String? = null,
    var stream: Int? = null,
    @SerializedName("switch")
    var switchX: Int? = null,
    var type: Int? = null,
    var vid: String? = null,
    var singer: List<AlbumSingerBean>? = null
)


class PreviewBean(
    /**
     * trybegin : 0
     * tryend : 0
     * trysize : 960887
     */
    var trybegin: Int? = null,
    var tryend: Int? = null,
    var trysize: Int? = null

)

class PayBean(
    /**
     * payalbum : 0
     * payalbumprice : 0
     * paydownload : 1
     * payinfo : 1
     * payplay : 0
     * paytrackmouth : 1
     * paytrackprice : 200
     * timefree : 0
     */
    var payalbum: Int? = null,
    var payalbumprice: Int? = null,
    var paydownload: Int? = null,
    var payinfo: Int? = null,
    var payplay: Int? = null,
    var paytrackmouth: Int? = null,
    var paytrackprice: Int? = null,
    var timefree: Int? = null

)

data class AlbumSingerBean(
    /**
     * id : 13948
     * mid : 001fNHEf1SFEFN
     * name : G.E.M. 邓紫棋
     */
    var id: Int? = null,
    var mid: String? = null,
    var name: String? = null

)

class CompanyNewBean(
    /**
     * brief :
     * headPic :
     * id : 343411
     * is_show : 1
     * name : G Nation
     */
    var brief: String? = null,
    var headPic: String? = null,
    var id: Int? = null,
    var is_show: Int? = null,
    var name: String? = null
)
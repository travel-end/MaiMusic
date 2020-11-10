package wind.maimusic.model.singer

/**
 * @By Journey 2020/11/10
 * @Description
 */
data class SingerData(
    var recommendSinger:RecommendSingers,
    var sexClassify:SingerSexClassify,
    var allSinger:AllSingers
)

data class RecommendSingers(
    val recomSingers:List<Singer>
)
data class SingerSexClassify(
    val maleSinger:String,
    val famaleSinger:String
)
data class AllSingers(
    val allSingers:List<Singer>
)
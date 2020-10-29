package wind.maimusic.utils

import wind.maimusic.model.core.ListBean

/**
 * @By Journey 2020/10/28
 * @Description
 */
object StringUtil {
    fun formatSinger(singer:String):String {
        return if (singer.contains("/")) singer.split("/")[0] else singer.trim()
    }
    fun formatProgress(time:Long):String {//263593
        val min = "${time / 60}"
        var sec = "${time % 60}"
        if (sec.length < 2) {
            sec = "0$sec"
        }
        return "$min:$sec"
    }

    fun getSinger(data: ListBean):String? {
        val singerList = data.singer
        singerList?.let {
            if (it.isNotEmpty()) {
                val singer = StringBuilder(it[0].name?:"")
                if (it.size > 1) {
                    for (i in it.indices+1) {
                        val bean = it[i]
                        singer.append("、").append(bean.name)
                    }
                }
                return singer.toString()
            }
        }
        return null
    }
}
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
    fun getSinger(data: ListBean):String? {
        val singerList = data.singer
        singerList?.let {
            if (it.isNotEmpty()) {
                val singer = StringBuilder(it[0].name?:"")
                if (it.size > 1) {
                    for (bean in it) {
                        singer.append("、").append(bean.name)
                    }
                }
                return singer.toString()
            }
        }
        return null
    }
}
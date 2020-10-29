package wind.maimusic.vm

import wind.maimusic.base.BaseViewModel
import wind.maimusic.utils.SpUtil
import wind.widget.cost.Consts
import wind.widget.model.Song

/**
 * @By Journey 2020/10/29
 * @Description
 */
class PlayViewModel : BaseViewModel() {

    fun getSingerName(song: Song): String? {
        val singer = song.singer
        if (singer != null) {
            return if (singer.contains("/")) {
                val s = singer.split("/")
                s[0].trim()
            } else {
                singer.trim()
            }
        }
        return null
    }


    fun setPlayMode(mode: Int) {
        SpUtil.saveValue(Consts.KEY_PLAY_MODE, mode)
    }

    fun getPlayMode(): Int {
        return SpUtil.getInt(Consts.KEY_PLAY_MODE, Consts.PLAY_ORDER)
    }
}
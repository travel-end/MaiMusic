package wind.maimusic

import androidx.multidex.MultiDexApplication
import wind.maimusic.room.database.OnlineSongDatabase
import wind.maimusic.utils.AssetsUtil
import wind.maimusic.utils.GlobalUtil
import wind.maimusic.utils.SpUtil
import wind.maimusic.utils.isNotNullOrEmpty
import wind.widget.cost.Consts

/**
 * @By Journey 2020/10/25
 * @Description
 */
class MaiApp:MultiDexApplication() {
    companion object {
        private var instances:MaiApp?=null
        fun getInstance():MaiApp {
            if (instances == null) {
                synchronized(MaiApp::class.java) {
                    if (instances == null) {
                        instances = MaiApp()
                    }
                }
            }
            return instances!!
        }
    }
    override fun onCreate() {
        super.onCreate()
        instances= this
        initData()
    }

    private fun initData() {
        if (SpUtil.getString(Consts.FIRST_LAUNCH).isEmpty()) {
            GlobalUtil.async {
                val songs =AssetsUtil.loadFirstMeetSongs()
                if (isNotNullOrEmpty(songs)) {
                    SpUtil.saveValue(Consts.FIRST_LAUNCH,"has_launch")
                    OnlineSongDatabase.getDatabase().firstMeetSongDao().addFirstMeetSongList(songs!!)
                }
            }
        }
    }
}
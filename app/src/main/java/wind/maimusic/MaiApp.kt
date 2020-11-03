package wind.maimusic

import androidx.multidex.MultiDexApplication

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
    }
}
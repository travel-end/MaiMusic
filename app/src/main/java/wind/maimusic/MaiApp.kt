package wind.maimusic

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        GlobalScope.launch(Dispatchers.IO) {
            // todo
        }
    }
}
package wind.maimusic

import androidx.multidex.MultiDexApplication
import com.danikula.videocache.HttpProxyCacheServer
import wind.maimusic.utils.FileUtil
import wind.maimusic.utils.cache.CacheFileNameGenerator
import java.io.File

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

        fun getProxy():HttpProxyCacheServer {
            return if (getInstance().proxy == null) {
                getInstance().proxy = getInstance().newProxy()
                getInstance().proxy!!
            } else {
                getInstance().proxy!!
            }
        }
    }
    override fun onCreate() {
        super.onCreate()
        instances= this
    }

    private var proxy:HttpProxyCacheServer?=null

    private fun newProxy():HttpProxyCacheServer {
        return HttpProxyCacheServer
            .Builder(this)
            .cacheDirectory(File(FileUtil.getMusicCacheDir()))
            .fileNameGenerator(CacheFileNameGenerator())
            .build()
    }


}
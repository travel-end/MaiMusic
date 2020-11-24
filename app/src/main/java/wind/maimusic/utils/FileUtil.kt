package wind.maimusic.utils

import android.os.Environment
import wind.maimusic.MaiApp
import java.io.File

/**
 * @By Journey 2020/11/24
 * @Description
 */
object FileUtil {

    /**
     * cacheDir:'内部缓存'，程序卸载的时候将会被删除，当系统运行在低运行空间中也可能会被删除，属于临时存储
     */
    private val appDir get() =
        "${MaiApp.getInstance().cacheDir.absolutePath}/maimusic/"

    /*音乐缓存路径*/
    fun getMusicCacheDir():String {
        val dir = "$appDir/musicCache"
        return mkDirs(dir)
    }

    private fun mkDirs(dir:String):String {
        val file = File(dir)
        if (!file.exists()) {
            file.mkdirs()
        }
        return dir
    }
}
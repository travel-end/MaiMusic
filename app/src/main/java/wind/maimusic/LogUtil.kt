package wind.maimusic

import android.util.Log

/**
 * @By Journey 2020/10/25
 * @Description
 */
object LogUtil {
    private const val isPrintLog = BuildConfig.LOG_DEBUG
    private const val  TAG=  Constants.LOG_TAG
    fun i(tag:String=TAG,msg:String) {
        if (isPrintLog) {
            Log.i(tag,msg)
        }
    }
    fun v(tag:String=TAG,msg:String) {
        if (isPrintLog) {
            Log.v(tag,msg)
        }
    }
    fun d(tag:String=TAG,msg:String) {
        if (isPrintLog) {
            Log.d(tag,msg)
        }
    }
    fun w(tag:String=TAG,msg:String) {
        if (isPrintLog) {
            Log.w(tag,msg)
        }
    }
    fun e(tag:String=TAG,msg:String) {
        if (isPrintLog) {
            Log.e(tag,msg)
        }
    }
}
package wind.maimusic.utils

import com.google.gson.Gson
import wind.maimusic.model.cuter.Cuter

/**
 * @By Journey 2020/11/19
 * @Description
 */
object CuterManager {
    private const val CUTER_INFO = "cuter_info"
    private const val CUTER_AVATAR = "cuter_avatar"
    private const val CUTER_NICKNAME = "cuter_nickname"
    private val mGson by lazy {
        Gson()
    }

    fun isLogin():Boolean {
        return SpUtil.getString(CUTER_INFO).isNotEmpty()
    }
    fun getCuterInfo(): Cuter? {
        val cuter = SpUtil.getString(CUTER_INFO)
        return if (cuter.isNotEmpty()) {
            mGson.fromJson(cuter, Cuter::class.java)
        } else {
            null
        }
    }

    fun setCuter(cuter: Cuter) {
        SpUtil.saveValue(CUTER_INFO, mGson.toJson(cuter))
    }

    fun setAvatar(avatar:String) {
        SpUtil.saveValue(CUTER_AVATAR,avatar)
    }

    val  avatar get() = SpUtil.getString(CUTER_AVATAR)


    fun setNickname(nickname:String) {
        SpUtil.saveValue(CUTER_NICKNAME,nickname)
    }

    val nickname get() = SpUtil.getString(CUTER_NICKNAME)

    fun clearCuterInfo() {
        SpUtil.remove(CUTER_INFO)
    }
}
package wind.maimusic.utils

import wind.maimusic.Constants
import java.util.*

/**
 * @By Journey 2020/11/5
 * @Description 首页刷新需求描述：
 * 新的一天的数据不会变 每次进来都是一样的  但是如果到第二天数据又是新的，但是同一天的数据不会变
 * 如果刷新的话，随机获取pageSize数量的数据。但是如果退出后再次进入，获取的数据还是第二天固定的数据
 * 每天以此类推
 */
object DataUtil {
    fun isNewDay():Boolean {
        val lastDay = SpUtil.getString(Constants.DAY_OF_WEEK)
        val calender = Calendar.getInstance()
        if (lastDay.isBlank()) {
            SpUtil.saveValue(Constants.DAY_OF_WEEK,calender.get(Calendar.DAY_OF_WEEK))
            return true
        }
        return if (lastDay==calender.get(Calendar.DAY_OF_WEEK).toString()) {
            false
        } else {
            SpUtil.saveValue(Constants.DAY_OF_WEEK,calender.get(Calendar.DAY_OF_WEEK))
            true
        }
    }

}
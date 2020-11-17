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
    private fun isNewDay():Boolean {
        val lastDay = SpUtil.getString(Constants.DAY_OF_WEEK)
        val calender = Calendar.getInstance()
        if (lastDay.isBlank()) {
            SpUtil.saveValue(Constants.DAY_OF_WEEK,calender.get(Calendar.DAY_OF_WEEK).toString())
            return false
        }
        return if (lastDay==calender.get(Calendar.DAY_OF_WEEK).toString()) {
            false
        } else {
            SpUtil.saveValue(Constants.DAY_OF_WEEK,calender.get(Calendar.DAY_OF_WEEK).toString())
            true
        }
    }

    /**
     * 获取查询当日数据的起始下标
     * todo 需要添加所有类型的总的数量 如果起始下标+步长>总数   将起始下标重置为0
     * 最好保证总的数据可以整除步长，这样数据就不会有查询不到的
     */
    fun getTheDayStartIndex(listType:Int):Int {
        var startIndex = SpUtil.getInt(Constants.THE_DAY_STEP,0)
        var step = 0
        when(listType) {
            Constants.DAILY_BANNER->step=Constants.PAGE_SIZE_BANNER
            Constants.DAILY_RECOMMEND_SONG->step=Constants.PAGE_SIZE_DAILY_RECOMMEND
            Constants.DAILY_HOT_SONG->step=16
            Constants.DAILY_RECOMMEND_BOOK->step=Constants.PAGE_SIZE_RECOMMEND_BOOK
            Constants.DAILY_LIST_COVER->step=Constants.PAGE_SIZE_SONG_LIST_COVER
            Constants.DAILY_POETRY_SONG->step=Constants.PAGE_SIZE_POETRY_SONG
        }
        if (isNewDay()) {
            startIndex += step
            SpUtil.saveValue(Constants.THE_DAY_STEP,startIndex)
        }

// TODO: 2020/11/5 这里的8、、要设置成各个类型的总数
        when(listType) {
            Constants.DAILY_BANNER->{
                if ((startIndex + step) > 8) {
                    startIndex = 0
                }
            }
            Constants.DAILY_RECOMMEND_SONG,Constants.DAILY_HOT_SONG->{
                if ((startIndex + step) > 16) {
                    startIndex = 0
                }
            }
            Constants.DAILY_RECOMMEND_BOOK->{
                if ((startIndex + step) > 20) {
                    startIndex = 0
                }
            }
            Constants.DAILY_LIST_COVER->{
                if ((startIndex + step) > 20) {
                    startIndex = 0
                }
            }
            Constants.DAILY_POETRY_SONG->{
                if ((startIndex + step) > 16) {
                    startIndex = 0
                }
            }
        }

        return startIndex
    }

    private fun compareDay(c1:Calendar,c2:Calendar):Boolean {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)&& c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
    }

    fun isSameDay(time:Long):Boolean {
        val c1 = Calendar.getInstance()
        c1.time = Date()
        val c2 = Calendar.getInstance()
        c2.timeInMillis = time
        return compareDay(c1,c2)
    }

}